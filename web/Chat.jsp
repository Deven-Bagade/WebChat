<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="chat.Message" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat Page</title>
    
    <!-- Link to external Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&family=Poppins:wght@400;500&display=swap" rel="stylesheet">
    
    <!-- Link to external icon library (Font Awesome) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <!-- Link to external CSS file for custom styles -->
    <link rel="stylesheet" type="text/css" href="Chatnew.css">
    <style>
        
        #groupsubmitbutton{
            width: 100%;
    padding: 6px 6px; /* Reduced top and bottom padding to reduce height */
    background-color: #2196F3;
    border: none;
    color: white;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
        }
    </style>
    
    <script>
        function loadUnreadMessages() {
            const xhr = new XMLHttpRequest();
            xhr.open('GET', 'Sendmsg', true);
            xhr.onload = function () {
                const messageList = document.getElementById('unread-message-list');
                messageList.innerHTML = '';
                
                try {
                    const response = JSON.parse(this.responseText);
                    if (response.length === 0) {
                        const para = document.createElement('p');
                        para.innerHTML = 'No New Messages';
                        messageList.appendChild(para);
                        return;
                    }
                    response.forEach(function (unreadMessage) {
                        const form = document.createElement('form');
                        form.action = 'Sendmsg';
                        form.method = 'POST';

                        const button = document.createElement('button');
                        button.name = 'receiverjsp';
                        button.value = unreadMessage.unreadSender;
                        button.type = 'submit';
                        
                        // Adding Font Awesome icon to button
                        button.innerHTML = `<i class="fas fa-comment-dots"></i>` + ` `+ unreadMessage.unreadSender;

                        form.appendChild(button);
                        messageList.appendChild(form);
                    });
                } catch (e) {
                    console.error('Failed to process messages:', e);
                }
            };

            xhr.onerror = function () {
                console.error('Failed to load messages from the server.');
            };

            xhr.send();
        }

        setInterval(function () {
            loadUnreadMessages();
        }, 500);

        window.onload = function () {
            loadUnreadMessages();
        };
    </script>
</head>

<body>
    <header class="site-header">
        <h1><i class="fas fa-comments"></i> Welcome to the Chat, ${sessionScope.username}!</h1>
    </header>

    <div class="chat-container" style="max-width:90%; position: relative">
        <section class="chat-form">
            <form action="Sendmsg" method="POST">
               
                <input id="receiverjsp" name="receiverjsp" type="text" placeholder="Enter username" required>
                <!-- Add an icon to the button -->
                <button type="submit" style="width:60px; height:60px"><i class="fas fa-search"></i>
</button>
            </form>
        </section>
<form action="Grp_chat_servlet" method="POST">
            <button type="Submit" id="groupsubmitbutton"style="padding: 10px; padding-left: 20px; padding-right: 20px; margin-left: 10px; width:auto; position: relative">Group Chat</button>
        </form>
        <section class="chat-messages">
            <h3><i class="fas fa-inbox"></i> Unread Messages</h3>
            <div class="chatmessagesdiv">
                <ul id="unread-message-list"></ul>
            </div>
        </section>
    </div>
</body>
</html>
