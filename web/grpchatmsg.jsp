<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="chat.Message" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chat Messages</title>
    <link rel="stylesheet" type="text/css" href="CM.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    
    <style>
            .overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
            z-index: 1000; /* Ensure it overlays everything */
            justify-content: center;
            align-items: center;
        }

        .overlay.show {
            display: flex; /* Make the overlay visible */
        }

        #textbox {
            width: 300px;
            padding: 10px;
            font-size: 16px;
            border: 2px solid #ccc;
            border-radius: 5px;
            background: #fff;
        }

        #back {
            margin-right: 10px;
            padding: 5px 10px;
            background-color: #ff5555;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        #back:hover {
            background-color: #ff3333;
        }
        #addButton{
             margin-right: 10px;
            padding: 5px 10px;
            background: #0078d7;
            color: white;           
            border: none;
            border-radius: 5px;
            cursor: pointer;           
        }
        #addButton:hover {
            background: #005cbf;
        }
        
    </style>

</head>
<body>
    <header class="header">
        <div class="chat-title">
            <i class="fas fa-comments"></i> <%=session.getAttribute("n")%>
        </div>
    </header>
   



    <script>
        
        function displayOverlay() {
            const overlay = document.getElementById("overlay");
            overlay.classList.toggle("show"); // Toggle the visibility of the overlay
        }

        function add() {
            const overlay = document.getElementById("overlay");
            const textbox = document.getElementById("textbox");
            const name = textbox.value.trim();

            if (name) {
                window.location.href = "MemberAddition?Mname=" + encodeURIComponent(name);
            }

            textbox.value = ''; // Clear the textbox
            overlay.classList.remove("show"); // Hide the overlay after adding
        }

   
    let autoScrollEnabled = true; // To track whether the user has scrolled manually

    // Scroll to the bottom of the chat container
    function scrollToBottom() {
        const chatMessages = document.querySelector('.chat-messages');
        if (chatMessages && autoScrollEnabled) {
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    }

    // Detect if the user scrolls manually
    function detectManualScroll() {
        const chatMessages = document.querySelector('.chat-messages');
        if (!chatMessages) return;

        // Check if the user has scrolled near the bottom
        const isAtBottom =
            chatMessages.scrollHeight - chatMessages.scrollTop <= chatMessages.clientHeight + 10;

        if (isAtBottom) {
            autoScrollEnabled = true; // Enable auto-scrolling when user is near the bottom
        } else {
            autoScrollEnabled = false; // Disable auto-scrolling if the user manually scrolls up
        }
    }

    // Function to load messages and mark them as seen
    function loadMessagesAndMarkAsSeen(receiver) {
        // Ensure the receiver is defined
        if (!receiver) {
            console.log("Receiver not specified.");
            return;
        }

        // Construct the URL with the receiver and markAsSeen parameter
        const url = "GrpChatMessageHandling?receiverjsp=" + encodeURIComponent(receiver) + "&markAsSeen=true";

        // Send the fetch request
        fetch(url)
            .then(response => response.json()) // Parse the response as JSON
            .then(data => {
                if (data.error) {
                    console.error("Error:", data.error);
                } else {
                    // Process and display the received messages
                    displayMessages(data); // Display the messages
                }
            })
            .catch(error => {
                console.error("Error fetching messages:", error);
            });
    }

    // Function to display messages in the UI
   function displayMessages(messages) {
    const messageList = document.getElementById('message-list');
    messageList.innerHTML = ''; // Clear the current message list

    // Get the logged-in user's username from the session
    const username = "${sessionScope.username}"; // Ensure this is correctly populated on the server side

    // Loop through the messages and append them to the list
    messages.forEach(function (message) {
        const li = document.createElement('li');
        const messageBubble = document.createElement('div');
        messageBubble.className = 'message-bubble';

        const timeText = message.time || "Unknown time";

        if (message.sender === username) {
            li.className = 'message-right';
            messageBubble.innerHTML = message.message; // Assign message content
        } else {
            li.className = 'message-left';
            messageBubble.innerHTML = message.sender +":<br>" +message.message;
        }

        // Add timestamp
        const timestamp = document.createElement('span');
        timestamp.className = 'timestamp';
        timestamp.innerHTML = timeText;

        // Append elements
        li.appendChild(messageBubble);
        messageBubble.appendChild(timestamp); // Add timestamp inside the bubble
        messageList.appendChild(li);
    });

    // Scroll to the bottom after displaying messages
    scrollToBottom();
}


    // Function to send a message via AJAX
    function sendMessage() {
        const messageField = document.getElementById('messagejsp');
        const message = messageField.value.trim();

        if (message === '') {
            alert('Message cannot be empty!');
            return;
        }

        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'GrpChatMessageHandling', true);
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhr.onload = function () {
            if (this.status === 200) {
                console.log("Message sent successfully!");
                messageField.value = ''; // Clear the textarea
                loadMessagesAndMarkAsSeen("${sessionScope.n}"); // Reload messages after sending
                scrollToBottom(); // Scroll to the bottom after new message
            }
        };
        xhr.send('messagejsp=' + encodeURIComponent(message));
    }

    // Automatically refresh messages every 2 seconds
    setInterval(function() {
        loadMessagesAndMarkAsSeen("${sessionScope.n}"); // Periodically fetch new messages
    }, 500);

    // Load messages initially when the page loads
    window.onload = function () {
        const chatMessages = document.querySelector('.chat-messages');
        if (chatMessages) {
            chatMessages.addEventListener('scroll', detectManualScroll);
        }

        // Initial load of messages and mark them as seen
        loadMessagesAndMarkAsSeen("${sessionScope.n}");
    };
</script>
<div class="chat-container">
<div class="chat-messages">
       
            <ul id="message-list">
                <!-- Messages will be dynamically loaded here -->
            </ul>
        </div>
</div>
            
        
            
            
        <div class="chat-input">
            <textarea id="messagejsp" name="messagejsp" placeholder="Enter your message here..." required></textarea><br>
            <button type="button" id="btn"onclick="sendMessage()"><i class="fa fa-paper-plane"></i></button>
            <button type="button" id="sendbtn" onclick="displayOverlay()"><i class="fas fa-plus"></i></button>
        </div> 
    </div>

    <!-- Overlay -->
    <div class="overlay" id="overlay">
        <input type="button" id="back" value="Close" onclick="displayOverlay()">
        <input type="text" id="textbox" placeholder="Enter member name">
        <button type="button" onclick="add()" id="addButton">Add</button>
    </div>
        
    </div>
</body> 
</html>