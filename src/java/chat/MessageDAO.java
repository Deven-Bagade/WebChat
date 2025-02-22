/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

/**
 *
 * @author DELL
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


        
       
public class MessageDAO {
     String getUserIdQuery = "SELECT USER_ID FROM USR WHERE username = ?";
 String insertMessageQuery = "INSERT INTO MESSAGE (SENDER_ID, RECEIVER_ID, MSG) VALUES (?, ?, ?)";
String unseenquery = "UPDATE MESSAGE SET is_seen = TRUE " +
                   "WHERE RECEIVER_ID = (SELECT USER_ID FROM USR WHERE username = ?) " +
                   "AND SENDER_ID = (SELECT USER_ID FROM USR WHERE username = ?) " +
                   "AND is_seen = FALSE";

          Connection conn = DatabaseConnection.getConnection();

    public boolean sendMessage(Message message){
 try{
            
             int senderId = getUserId(conn, getUserIdQuery, message.getSender());
        if (senderId == -1) {
            System.out.println("Sender not found: " + message.getSender());
            return false;
        }
         int receiverId = getUserId(conn, getUserIdQuery, message.getReceiver());
        if (receiverId == -1) {
            System.out.println("Receiver not found: " + message.getReceiver());
            return false;
        }
        PreparedStatement stmt = conn.prepareStatement(insertMessageQuery);
        
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, message.getMessage());
            int rowsaffected = stmt.executeUpdate();
           return rowsaffected > 0;
        }
        catch(Exception e){
        e.printStackTrace();
        }
        return false;
    }
    
    

  
    
   public List<Message> getMessages(String sender, String receiver) {
    List<Message> messages = new ArrayList<>();

    // Update the query to also fetch filePath from the FILE table
    String query = "SELECT m.MSG_ID, m.SENDER_ID, m.RECEIVER_ID, m.MSG, m.TIME_STAMP, m.HAS_FILE, "
                 + "f.FILE_ID, f.FILE_NAME, f.FILE_TYPE, f.FILE_SIZE, f.FILE_PATH, "
                 + "u.username AS sender_name "
                 + "FROM MESSAGE m "
                 + "JOIN USR u ON m.SENDER_ID = u.USER_ID "
                 + "LEFT JOIN FILE f ON m.MSG_ID = f.MSG_ID "
                 + "WHERE (m.SENDER_ID = ? AND m.RECEIVER_ID = ?) "
                 + "OR (m.SENDER_ID = ? AND m.RECEIVER_ID = ?) "
                 + "ORDER BY m.MSG_ID ASC";

    try {
        int senderId = getUserId(conn, getUserIdQuery, sender);
        int receiverId = getUserId(conn, getUserIdQuery, receiver);

        if (senderId == -1 || receiverId == -1) {
            System.out.println("Invalid sender or receiver username.");
            return messages;
        }

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, senderId);
        stmt.setInt(2, receiverId);
        stmt.setInt(3, receiverId);
        stmt.setInt(4, senderId);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Message message = new Message();
            message.setMessageId(rs.getInt("MSG_ID"));
            message.setSender(rs.getString("sender_name")); // Get the sender's name
            message.setReceiver(String.valueOf(rs.getInt("RECEIVER_ID")));
            message.setMessage(rs.getString("MSG"));
            message.setTime(rs.getTimestamp("TIME_STAMP").toString());

            // If the message has a file, retrieve file details
            if (rs.getBoolean("HAS_FILE")) {
                message.setFileId(rs.getInt("FILE_ID"));
                message.setFileName(rs.getString("FILE_NAME"));
                message.setFileType(rs.getString("FILE_TYPE"));
                message.setFileSize(rs.getInt("FILE_SIZE"));
                message.setFilePath(rs.getString("FILE_PATH")); // Set file path
            }

            messages.add(message);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return messages;
}



    private int getUserId(Connection conn, String query, String username) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("USER_ID");
        }
    }
    return -1; // Return -1 if user is not found
}
    
    public List<Message> getNewMessages(String sender, String receiver, String lastTimestamp) {
    List<Message> messages = new ArrayList<>();
    String query = "SELECT MSG_ID, SENDER_ID, RECEIVER_ID, MSG, TIME_STAMP, u.username AS sender_name "
                 + "FROM MESSAGE m "
                 + "JOIN USR u ON m.SENDER_ID = u.USER_ID "
                 + "WHERE ((SENDER_ID = ? AND RECEIVER_ID = ?) OR (SENDER_ID = ? AND RECEIVER_ID = ?)) "
                 + "AND TIME_STAMP > ? "
                 + "ORDER BY MSG_ID ASC";

    try {
        int senderId = getUserId(conn, getUserIdQuery, sender);
        int receiverId = getUserId(conn, getUserIdQuery, receiver);
        if (senderId == -1 || receiverId == -1) {
            System.out.println("Invalid sender or receiver username.");
            return messages;
        }  
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, senderId);
        stmt.setInt(2, receiverId);
        stmt.setInt(3, receiverId);
        stmt.setInt(4, senderId);
        stmt.setString(5, lastTimestamp);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Message message = new Message();
            message.setMessageId(rs.getInt("MSG_ID"));
            message.setSender(rs.getString("sender_name"));  // Get the sender's name
            message.setReceiver(String.valueOf(rs.getInt("RECEIVER_ID")));
            message.setMessage(rs.getString("MSG"));
            message.setTime(rs.getTimestamp("TIME_STAMP").toString());
            messages.add(message);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        System.out.println("Messages fetched: " + messages.size());

    return messages;
}
 
 public void markMessagesAsSeen(String sender, String receiver) {
    try (        

            PreparedStatement stmt = conn.prepareStatement(unseenquery))
{
        // Log the operation
        System.out.println("Marking messages as seen between " + sender + " and " + receiver);
        
        // Your database query to update messages
        // Execute the query here

            stmt.setString(1, sender); // The receiver's username
            stmt.setString(2, receiver);   // The sender's username

            stmt.executeUpdate();
        
        System.out.println("Messages marked as seen.");
    } catch (Exception e) {
        // Log any errors
        System.err.println("Error while marking messages as seen: " + e.getMessage());
        e.printStackTrace();
    }
}

    
}


