/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package chat;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author DELL
 */
public class ServletLR extends HttpServlet {

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
               String action = request.getParameter("action");
               if("loginForm".equals(action)){
                  
                   UserDAO userDAO = new UserDAO();
                    String username = request.getParameter("usernamelogin");
                    String password = request.getParameter("passwordlogin");
                                                  
                    int id = -1;
                    id=userDAO.authenticateUser(username,password);

                   if(id != -1){

                   request.getSession().setAttribute("username", username);  // Store username in session
                                 request.getSession().setAttribute("id",id);

                    RequestDispatcher dispatcher = request.getRequestDispatcher("Chat.jsp");
                    dispatcher.forward(request, response);
                    
                   }
                   else{
                   out.println("error");
                   }
                    
               }
               else if("registerForm".equals(action)){
                    UserDAO userDAO = new UserDAO();
                    User user = new User();
                    String username = request.getParameter("usernameregister");
                    String password = request.getParameter("passwordregister");
                    String email = request.getParameter("emailregister");
                   
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    
                    int id = -1;
                    id=userDAO.userRegister(user);
                    
                    if(id != 1){
                     request.getSession().setAttribute("username", username);  
                                     request.getSession().setAttribute("id",id);

                     RequestDispatcher dispatcher = request.getRequestDispatcher("Chat.jsp");
        dispatcher.forward(request, response); 
                    }
                    else{
                     out.println("error");
                    }
                    
                    
               }
               else{
                out.println("invalid Action");
               }
            /* TODO output your page here. You may use following sample code. */
           
        }
        catch(Exception e){
        e.printStackTrace();
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
