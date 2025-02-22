package chat;

/**
 *
 * @author DELL
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
        
       
public class GMessageDAO {
     String getUserIdQuery = "SELECT USER_ID FROM USR WHERE username = ?";
          String getGrpIdQuery = "SELECT Group_ID FROM GRP WHERE Group_name = ?";

     
 String insertMessageQuery = "INSERT INTO MESSAGE (SENDER_ID, Group_ID, MSG) VALUES (?, ?, ?)";
/*String unseenquery = "UPDATE MESSAGE SET is_seen = TRUE " +
                   "WHERE RECEIVER_ID = (SELECT USER_ID FROM USR WHERE username = ?) " +
                   "AND SENDER_ID = (SELECT USER_ID FROM USR WHERE username = ?) " +
                   "AND is_seen = FALSE";*/

          Connection conn = DatabaseConnection.getConnection();

    public boolean sendMessage(Message message){
 try{
            
             int senderId = getUserId(conn, getUserIdQuery, message.getSender());
        if (senderId == -1) {
            System.out.println("Sender not found: " + message.getSender());
            return false;
        }
         int Group_id = getGrpId(conn, getGrpIdQuery, message.getReceiver());
        if (Group_id == -1) {
            System.out.println("Receiver not found: " + message.getReceiver());
            return false;
        }
        PreparedStatement stmt = conn.prepareStatement(insertMessageQuery);
        
               stmt.setInt(1, senderId);
            stmt.setInt(2, Group_id);
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
    
    String query = "SELECT MSG_ID, SENDER_ID, Group_ID, MSG, TIME_STAMP, u.username AS sender_name "
                 + "FROM MESSAGE m "
                 + "JOIN USR u ON m.SENDER_ID = u.USER_ID "
                 + "WHERE (SENDER_ID = ? AND Group_ID = ?) "
                 + "   OR (Group_ID = ?) "
            +  "ORDER BY MSG_ID ASC";

    try {
        int senderId = getUserId(conn, getUserIdQuery, sender);
        int Group_Id = getGrpId(conn, getGrpIdQuery, receiver);
                    System.out.println(senderId);
                                        System.out.println(Group_Id);


        if (senderId == -1 || Group_Id == -1) {
            System.out.println("Invalid sender or receiver username.");
            return messages;
        }
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, senderId);
        stmt.setInt(2, Group_Id);
        stmt.setInt(3, Group_Id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Message message = new Message();
            message.setMessageId(rs.getInt("MSG_ID"));
            message.setSender(rs.getString("sender_name"));  // Get the sender's name
            message.setReceiver(String.valueOf(rs.getInt("Group_ID")));
            message.setMessage(rs.getString("MSG"));
            message.setTime(rs.getTimestamp("TIME_STAMP").toString());
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
    private int getGrpId(Connection conn, String query, String username) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("Group_ID");
        }
    }
    
    return -1; // Return -1 if user is not found
}
    
    public List<Message> getNewMessages(String sender, String receiver, String lastTimestamp) {
    List<Message> messages = new ArrayList<>();
    String query = "SELECT MSG_ID, SENDER_ID, Group_ID, MSG, TIME_STAMP, u.username AS sender_name "
                 + "FROM MESSAGE m "
                 + "JOIN USR u ON m.SENDER_ID = u.USER_ID "
                + "WHERE (SENDER_ID = ? AND Group_ID = ?) "
                 + "   OR (Group_ID = ?) "
            + "ORDER BY MSG_ID ASC";

    try {
        int senderId = getUserId(conn, getUserIdQuery, sender);
        int Group_Id = getGrpId(conn, getGrpIdQuery, receiver);
        if (senderId == -1 || Group_Id == -1) {
            System.out.println("Invalid sender or receiver username.");
            return messages;
        }
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, senderId);
        stmt.setInt(2, Group_Id);
        stmt.setInt(3, Group_Id);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Message message = new Message();
            message.setMessageId(rs.getInt("MSG_ID"));
            message.setSender(rs.getString("sender_name"));  // Get the sender's name
            message.setReceiver(String.valueOf(rs.getInt("Group_ID")));
            message.setMessage(rs.getString("MSG"));
            message.setTime(rs.getTimestamp("TIME_STAMP").toString());
            messages.add(message);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return messages;
}
 
   
 /*public void markMessagesAsSeen(String sender, String receiver) {
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
}*/

    
}