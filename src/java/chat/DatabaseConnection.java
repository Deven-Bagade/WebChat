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
public class DatabaseConnection {
    public static Connection getConnection(){
        
         Connection conn = null;
    
    try{
    Class.forName("com.mysql.cj.jdbc.Driver");
        String username = "root";
        String password = "Tala@123";
        String url = "jdbc:mysql://localhost:3306/CHAT_APP";
       conn = DriverManager.getConnection(url, username, password);    
}
    catch(Exception e){    
}
return conn;
}
}

