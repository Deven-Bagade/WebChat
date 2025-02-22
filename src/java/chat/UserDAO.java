/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

/**
 *
 * @author DELL
 */
import java.sql.*;

public class UserDAO {
    public int userRegister(User user){
        String query="INSERT INTO USR (username,password,email)VALUES(?,?,?)";
        int id=-1;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);){
            
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPassword());
            stmt.setString(3,user.getEmail());
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected>0){
                String query1 = "Select User_ID FROM USR WHERE username=?";
                 PreparedStatement ps = conn.prepareStatement(query1);
                 ps.setString(1,user.getUsername());
                 ResultSet rs1 = ps.executeQuery();
                 if(rs1.next()){
                      id = rs1.getInt("User_ID");
                 }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }
public int authenticateUser(String username,String password){
        String query="SELECT * FROM USR WHERE username=? AND password=?";
        int id = -1;
        try(Connection conn=DatabaseConnection.getConnection();
            PreparedStatement stmt=conn.prepareStatement(query);){
            
            stmt.setString(1,username);
            stmt.setString(2,password);
            
            ResultSet rs=stmt.executeQuery();
            if( rs.next()){
                String query1 = "Select User_ID FROM USR WHERE username=?";
                 PreparedStatement ps = conn.prepareStatement(query1);
                 ps.setString(1,username);
                 ResultSet rs1 = ps.executeQuery();
                 if(rs1.next()){
                      id = rs1.getInt("User_ID");
                 }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
            return id;
    }
}
