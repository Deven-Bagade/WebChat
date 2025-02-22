/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package chat;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import org.json.*;
/**
 *
 * @author speed
 */
public class MemberAddition extends HttpServlet {

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
        try{
            
        add(request , response);
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
}
        public void add(HttpServletRequest request, HttpServletResponse response){
        try  {
            
            
             
            System.out.println("entered");
        Connection conn = DatabaseConnection.getConnection();
        String mname = request.getParameter("Mname");
        request.setAttribute("m",mname);
        String query = "SELECT COUNT(*) FROM USR WHERE username = ?";
        PreparedStatement ps2 = conn.prepareStatement(query);
        ps2.setString(1, mname);
        ResultSet rs1 = ps2.executeQuery();
        
        
        //user exists in db
        if(rs1.next()&& rs1.getInt(1) > 0){
                     String Query = "SELECT Members FROM GRP WHERE Group_name=?";
                     PreparedStatement ps = conn.prepareStatement(Query);
                     String n =(String)request.getSession().getAttribute("n");
                     ps.setString(1,n);
                     ResultSet rs = ps.executeQuery();
                     JSONArray ARR = new JSONArray();
                     JSONArray currentArray = new JSONArray(); 

                     if(rs.next()){
                         String jsonData = rs.getString("Members");
                         if (jsonData != null) {
                         currentArray = new JSONArray(jsonData); 
                        }
                     }
                     String query3 = "SELECT Members FROM GRP WHERE GROUP_NAME = ?";
                     PreparedStatement ps3 = conn.prepareStatement(query3);
                     ps3.setString(1,n);
                     ResultSet rs3 = ps3.executeQuery();
                    JSONArray currentArray1 = new JSONArray(); 
            if(rs3.next()){
                String jsonData = rs.getString("Members");
                if (jsonData != null) {
                    currentArray1 = new JSONArray(jsonData); 
                }
            }
            
            //  mname already exists in the array
            if (currentArray1.toList().contains(mname)) {
                            PrintWriter pw=response.getWriter();
                response.setContentType("text/html");

            pw.println("<script type='text/javascript'>"); 
            pw.println("alert('Member already exists')");
            pw.println("location='grpchatmsg.jsp'");
            pw.println("</script>");
            pw.flush();
            pw.close();
            return;
                     }
                     else{
                     currentArray.put(mname);
                     String Query1 = "UPDATE GRP SET MEMBERS = ? WHERE Group_name = ?";
                     PreparedStatement ps1 = conn.prepareStatement(Query1);
                     ps1.setString(1,currentArray.toString());
                     ps1.setString(2,n);
                     ps1.executeUpdate();
                       RequestDispatcher dispatcher = request.getRequestDispatcher("grpchatmsg.jsp");
                     dispatcher.forward(request, response);
                     }
                   
                     
        }
        //user doesnot exists
        else{
            PrintWriter pw1=response.getWriter();
                response.setContentType("text/html");

            pw1.println("<script type='text/javascript'>"); 
            pw1.println("alert('Member doesn\\'t exists')");
            pw1.println("location='grpchatmsg.jsp'");
            pw1.println("</script>");
            pw1.flush();
            pw1.close();
            return;
        }
         
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
