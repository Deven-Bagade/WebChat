/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package chat;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import com.google.gson.Gson;  // Make sure to include the Gson library for JSON conversion




/**
 *
 * @author DELL
 */
public class Sendmsg extends HttpServlet {
                  Connection conn = DatabaseConnection.getConnection();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
             HttpSession session = request.getSession();        
             String sender = (String) session.getAttribute("username"); 
            
            if (sender == null) {
                response.sendRedirect("login.jsp");
                return; 
            }

            // Get the receiver from the form and store in session
            String receiver = request.getParameter("receiverjsp");
            session.setAttribute("receiver", receiver);  // Storing receiver in session

            // Retrieve messages between sender and receiver
            MessageDAO messagedao = new MessageDAO();
            List<Message> messages = messagedao.getMessages(sender, receiver);

            // Forward to the ChatMessages.jsp page with the messages
            request.setAttribute("messages", messages);
            RequestDispatcher dispatcher = request.getRequestDispatcher("ChatMessages.jsp");
            dispatcher.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                  
     String query = "SELECT DISTINCT u.username AS sender_name " +
                           "FROM message m " +
                           "JOIN USR u ON m.sender_id = u.user_id " +
                           "WHERE m.receiver_id = (SELECT user_id FROM USR WHERE username = ?) " +
                           "AND m.is_seen = 0";
     try{
           PreparedStatement pstmt = conn.prepareStatement(query);
        HttpSession session = request.getSession();        
        String sender = (String) session.getAttribute("username"); 
            pstmt.setString(1, sender);
            ResultSet rs = pstmt.executeQuery();
           
            
            List<UnreadMessage> unreadmessages = new ArrayList<>();

            // Process the result set
                while (rs.next()) {
            UnreadMessage unreadmessage = new UnreadMessage();
            
            unreadmessage.setUnreadSender(rs.getString("sender_name"));  
           
            unreadmessages.add(unreadmessage);   
            
            }
                System.out.println("Unread Messages:");
for (UnreadMessage message : unreadmessages) {
    System.out.println(message.getUnreadSender());  // Print each sender's name
}
                Gson unreadgson = new Gson();
            String unreadjsonResponse = unreadgson.toJson(unreadmessages);
            if (unreadjsonResponse == null) {
    unreadjsonResponse = "[]";  // Return an empty array
}

            PrintWriter out = response.getWriter();
            out.print(unreadjsonResponse);
            out.flush();
            
            
     }
     catch(Exception e){
     e.printStackTrace();
     }
}


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description"; 
    }// </editor-fold>

}
