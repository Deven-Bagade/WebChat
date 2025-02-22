<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="chat.Message" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat Messages</title>
    <link rel="stylesheet" type="text/css" href="CM.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script>

        function triggerFileInput() {
    // Trigger the hidden file input click event
    document.getElementById('fileInput').click();
}

function handleFileSelection(input) {
    const file = input.files[0];
    if (file) {
        // Set the file name or custom message in the textarea
        const messageTextarea = document.getElementById('messagejsp');
        messageTextarea.value = `File selected: ` + file.name;
    }
}

function send() {
    const messageTextarea = document.getElementById('messagejsp');
    const fileInput = document.getElementById('fileInput');
    const message = messageTextarea.value.trim();
    const file = fileInput.files[0];

    if (file) {
        // File is selected, submit file upload form
        uploadFile(file);
    } else if (message) {
        // No file, send a text message
        sendMessage();
    } else {
        alert("Please enter a message or select a file to send.");
    }
}

function uploadFile(file) {
    const formData = new FormData();
    formData.append('file', file);

    fetch('UploadFile', {
        method: 'POST',
        body: formData,
    })
    .then(response => response.text())
    .then(data => {
        console.log('File uploaded:', data);
        // Reset the input fields
        document.getElementById('messagejsp').value = '';
        document.getElementById('fileInput').value = '';
    })
    .catch(error => {
        console.error('Error uploading file:', error);
    });
}




        let autoScrollEnabled = true;

        function scrollToBottom() {
            const chatMessages = document.querySelector('.chat-messages');
            if (chatMessages && autoScrollEnabled) {
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        }

        function detectManualScroll() {
            const chatMessages = document.querySelector('.chat-messages');
            if (!chatMessages) return;

            const isAtBottom = chatMessages.scrollHeight - chatMessages.scrollTop <= chatMessages.clientHeight + 10;
            autoScrollEnabled = isAtBottom;
        }

        function loadMessagesAndMarkAsSeen(receiver) {
            if (!receiver) return;

            const url = "MessageHandling?receiverjsp=" + encodeURIComponent(receiver) + "&markAsSeen=true";
            fetch(url)
                .then(response => response.json())
                .then(data => displayMessages(data))
                .catch(console.error);
        }

        function displayMessages(messages) {
            const messageList = document.getElementById('message-list');
            messageList.innerHTML = ''; // Clear previous messages

            messages.forEach(message => {
                const li = document.createElement('li');
                const username = "${sessionScope.username}";

                li.className = message.sender === username ? 'message-right' : 'message-left';

                const messageText = message.message;
                const timeText = message.time || "Unknown time";

                // Create message bubble and timestamp elements
                const messageBubble = document.createElement('div');
                messageBubble.className = 'message-bubble';

                // If the message has a file, add file details
                if (message.fileName) {
                           console.log("File ID: " + message.fileId);

                   const fileLink = document.createElement('a');
console.log("Download URL: FileDownloadServlet?fileId=" + message.fileId);
fileLink.href = "FileDownloadServlet?fileId=" + message.fileId;
fileLink.target = '_blank';


                    if (message.fileType.startsWith('image/')) {
                        const fileThumbnail = document.createElement('img');
                        fileThumbnail.src = `FileDownloadServlet?fileId=${message.fileId}`;
                        fileThumbnail.alt = message.fileName;
                        fileThumbnail.className = 'file-thumbnail';
                        fileLink.appendChild(fileThumbnail);
                    } else {
                        fileLink.innerText = message.fileName;
                        fileLink.className = 'file-link';
                    }

                    messageBubble.appendChild(fileLink);
                }

                // Add message text if present
                if (messageText) {
                    const textParagraph = document.createElement('p');
                    textParagraph.textContent = messageText;
                    messageBubble.appendChild(textParagraph);
                }

                const timestamp = document.createElement('span');
                timestamp.className = 'timestamp';
                timestamp.innerHTML = timeText;

                li.appendChild(messageBubble);
                li.appendChild(timestamp);

                messageList.appendChild(li);
            });

            scrollToBottom();
        }

        function sendMessage() {
            const messageField = document.getElementById('messagejsp');
            const message = messageField.value.trim();

            if (message === '') return alert('Message cannot be empty!');

            const xhr = new XMLHttpRequest();
            xhr.open('POST', 'MessageHandling', true);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
            xhr.onload = () => {
                if (xhr.status === 200) {
                    messageField.value = '';
                    loadMessagesAndMarkAsSeen("${sessionScope.receiver}");
                }
            };
            xhr.send('messagejsp=' + encodeURIComponent(message));
        }
        

setInterval(() => loadMessagesAndMarkAsSeen("${sessionScope.receiver}"), 1000);

        window.onload = function () {
            const chatMessages = document.querySelector('.chat-messages');
            if (chatMessages) chatMessages.addEventListener('scroll', detectManualScroll);
            loadMessagesAndMarkAsSeen("${sessionScope.receiver}");
        };
    </script>
</head>
<body>
    <header class="header">
        <div class="chat-title">
            <i class="fas fa-comments"></i> Chat with ${sessionScope.receiver}
        </div>
    </header>

    <div class="chat-container">
        <div class="chat-messages">
            <ul id="message-list"></ul>
        </div>
    </div>

   <footer>
    <div class="chat-input">
        <!-- Textarea for typing messages -->
        <textarea id="messagejsp" name="messagejsp" placeholder="Type a message..."></textarea>

        <!-- Hidden file input for file selection -->
        <input type="file" name="file" id="fileInput" style="display: none;" onchange="handleFileSelection(this)">

        <!-- Attach File Button -->
        <button type="button" id="btn"onclick="triggerFileInput()" class="attach-button">
            <i class="fa fa-paperclip" aria-hidden="true"></i>
        </button>

        <!-- Send Button -->
        <button type="button" id="sendbtn" onclick="send()" class="send-button">
            <i class="fa fa-paper-plane" aria-hidden="true"></i>
        </button>
    </div>
</footer>




</body>
</html>
