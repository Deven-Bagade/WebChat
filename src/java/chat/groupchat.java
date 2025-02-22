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
import jakarta.servlet.http.HttpSession;
import java.sql.*;
import java.util.List;
import org.json.JSONArray;
/**
 *
 * @author speed
 */

public class groupchat extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch(action){
                case "createGroup":
                createGroup(request,response);
                break;
                
                case "chatWith":
                    chatWith(request,response);
                    break;
                
            }
        }
         
    }

    // Create a new group
    private void createGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupName = request.getParameter("groupName");        
        // Insert group into the database
        Connection con = DatabaseConnection.getConnection();
        String query = "INSERT INTO GRP(Group_name,User_ID) VALUES (?,?)";
        int id = (int)request.getSession().getAttribute("id");
        
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            String query1= "SELECT * FROM GRP WHERE Group_name = ?";
            PreparedStatement ps1 = con.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1,groupName);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next() && rs1.getInt(1) > 0){
                //group exists
                PrintWriter pw=response.getWriter();
                response.setContentType("text/html");

               pw.println("<script type='text/javascript'>"); 
            pw.println("alert('Group Name Exists')");
            pw.println("location='GROUP_CHAT.jsp'");
            pw.println("</script>");
            pw.close();

                  //RequestDispatcher rd1=request.getRequestDispatcher("error.jsp");
                  //rd1.forward(request, response);
            }
            else{
            ps.setString(1, groupName);
            ps.setInt(2, id);
            

            
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int groupId = rs.getInt(1);
                    
                    
                    
                    JSONArray currentArray = new JSONArray(); 
                    HttpSession session =  request.getSession();
                    String n = (String) session.getAttribute("n");
                    String mname = (String) session.getAttribute("username");
                    currentArray.put(mname);

                     String Query1 = "UPDATE GRP SET MEMBERS = ? WHERE Group_name = ?";
                     PreparedStatement ps2 = con.prepareStatement(Query1);
                     ps2.setString(1,currentArray.toString());
                     ps2.setString(2,groupName);
                     ps2.executeUpdate();
                     
                     System.out.println("hiii");
                     
                                         request.setAttribute("receiver",groupName);

                    RequestDispatcher rd=request.getRequestDispatcher("GROUP_CHAT.jsp");
                    rd.forward(request,response);
                    
                }
            } else {
                response.getWriter().write("Failed to create group.");
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   private void chatWith(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupName = request.getParameter("ChatWith");  
        try{
         
            HttpSession session = request.getSession();  
            session.setAttribute("n",groupName);
             String un = (String)session.getAttribute("username");
            
                    RequestDispatcher rd=request.getRequestDispatcher("grpchatmsg.jsp");
                    rd.forward(request,response);
            }
           
                     catch (Exception e) {
            e.printStackTrace();
        }
    }


}
