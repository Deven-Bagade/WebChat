package chat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;  // Make sure to include the Gson library for JSON conversion

public class GrpChatMessageHandling extends HttpServlet {
            GMessageDAO messagedao = new GMessageDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");  // Set content type to JSON
        
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();        
            String sender = (String) session.getAttribute("username"); 
            String receiver = (String) session.getAttribute("n"); 
            String messageContent = request.getParameter("messagejsp");
            
            
            // Redirect if no sender or receiver is found in the session
            if (sender == null || receiver == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Validate message input
            if (messageContent != null && !messageContent.trim().isEmpty()) {
                // Create a message object to store the message details
                Message message = new Message();
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setMessage(messageContent);

                // Send the message to the database
                boolean isMessageSent = messagedao.sendMessage(message);
                
                // If the message is sent, fetch updated messages
                    List<Message> messages = messagedao.getMessages(sender, receiver);
                    
                    // Convert the list of messages to JSON format and write it to the response
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(messages);
                    out.print(jsonResponse);
                
            } 
        }
    }

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String sender = (String) session.getAttribute("username");
        String receiver = request.getParameter("receiverjsp");

        if (sender == null || receiver == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Sender or receiver not specified.\"}");
            out.flush();
            return;
        }

        try {
            // Fetch messages between sender and receiver
            List<Message> messages = messagedao.getMessages(sender, receiver);
           System.out.print(messages);
           for(Message m : messages){
               System.out.print(m.getSender());
               System.out.print(m.getMessage());

           }

            // Mark messages as seen if sender is the receiver
 /*String markAsSeen = request.getParameter("markAsSeen");
        if ("true".equals(markAsSeen)) {
            // Mark messages as seen if sender is the receiver
            messagedao.markMessagesAsSeen(sender, receiver);
        }*/

            // Convert messages to JSON and send as response
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(messages);

            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Failed to load messages.\"}");
            out.flush();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles sending and receiving messages in a chat system.";
    }
}