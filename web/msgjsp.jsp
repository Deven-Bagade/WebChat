<%-- 
    Document   : Chat
    Created on : Nov 24, 2024, 3:23:50 PM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="chat.Message" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chat Page</title>
    <link rel="stylesheet" type="text/css" href="msgstyle.css">
    
    <script>
    function loadUnreadMessages() {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', 'Sendmsg', true);  // Call the Sendmsg servlet to get unread messages
        xhr.onload = function () {
    const messageList = document.getElementById('unread-message-list');
    messageList.innerHTML = ''; // Clear the current message list

    // Check if the response is empty
    

    try {
        const response = JSON.parse(this.responseText); // Parse the JSON response
if (response.length === 0) {

    // Create a paragraph to display the fallback message
    const para = document.createElement('p');
    para.innerHTML = 'No New Messages';
   

    messageList.appendChild(para); // Append the message to the list container

    return; // Exit if the response is empty
}

    else{
        // Populate the message list with sender names
        response.forEach(function (unreadMessage) {
                        // Create a form element for each unread sender
                        const form = document.createElement('form');
                        form.action = 'Sendmsg';  // Set form action to Sendmsg servlet
                        form.method = 'POST';     // POST method for form submission

                        // Create a button for each unread sender
                        const button = document.createElement('button');
                        button.name = 'receiverjsp';  // Set the button name to 'receiverjsp' for sending the receiver
                        button.value = unreadMessage.unreadSender;  // Set the sender's username as the value
                        button.type = 'submit';  // Button will submit the form
                        button.innerHTML = unreadMessage.unreadSender;  // Display the sender's name

                        // Append the button to the form and the form to the message list
                        form.appendChild(button);
                        messageList.appendChild(form);
                    });
                }

       

    } catch (e) {
        console.error('Failed to process messages:', e);
    }
};

        xhr.onerror = function () {
            console.error('Failed to load messages from the server.');
        };

        xhr.send();
    }
setInterval(function() {
                loadUnreadMessages();
    }, 500);

    window.onload = function () {
        loadUnreadMessages();
    };
    </script>
    
    
    
</head>
<body>
    <div class="chat-container">
        <h1>Welcome to the Chat, ${sessionScope.username}!</h1>
        <form action="Sendmsg" method="POST">
            <label for="receiverjsp">Receiver:</label>
            <input id="receiverjsp" name="receiverjsp" type="text" required><br><br>
            
            <button type="submit">Let's Chat</button>
        </form>
        <div class="chatmessagesdiv">
            <h3>New Messages Will Appear Here</h3>
            <ul id="unread-message-list">
                
            </ul>
     <form action="Grp_chat_servlet" method="POST">
            <button type="Submit" style="margin-top: 20px;">Group Chat</button>
        </form>
      
        </div>
        

       
    </div>
</body>
</html>