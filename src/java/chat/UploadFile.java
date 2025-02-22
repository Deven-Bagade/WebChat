package chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

@MultipartConfig
public class UploadFile extends HttpServlet {

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");

    try (PrintWriter out = response.getWriter()) {
        // Retrieve the uploaded file part
        Part part = request.getPart("file");
        String fileName = part.getSubmittedFileName();
        String fileType = part.getContentType();
        long fileSize = part.getSize();

        // Get the msg_id from the request (ensure the client sends this)
        int msgId = createMessage(request);  // Step 1: Create a message and get the msg_id

        if (msgId > 0) {
            // Step 2: Save the file on the server and get the file path
            String filePath = saveFileOnServer(part, fileName);

            if (filePath != null) {
                // Step 3: Insert file details into the file table including the file path
                boolean fileSaved = saveFileDetails(msgId, fileName, fileType, fileSize, filePath);
                if (fileSaved) {
                    // Step 4: Mark the message as having a file
                    updateMessageHasFile(msgId);

                    // Redirect to ChatMessages.jsp with success
                    response.sendRedirect("ChatMessages.jsp?status=success");
                    return;
                } else {
                    // Redirect to ChatMessages.jsp with error
                    response.sendRedirect("ChatMessages.jsp?status=error&msg=File details save failed");
                    return;
                }
            } else {
                // Redirect to ChatMessages.jsp with error
                response.sendRedirect("ChatMessages.jsp?status=error&msg=Failed to save file on server");
                return;
            }
        } else {
            // Redirect to ChatMessages.jsp with error
            response.sendRedirect("ChatMessages.jsp?status=error&msg=Failed to create message");
            return;
        }
    } catch (Exception e) {
        e.printStackTrace();
        // Redirect to ChatMessages.jsp with error
        response.sendRedirect("ChatMessages.jsp?status=error&msg=Unexpected error occurred");
    }
}

    private int createMessage(HttpServletRequest request) {
        int msgId = -1;
        HttpSession session = request.getSession();        

        String senderUsername = (String) session.getAttribute("username"); 
        String receiverUsername = (String) session.getAttribute("receiver");

        // Debugging: Print the usernames
        System.out.println("Sender Username: " + senderUsername);
        System.out.println("Receiver Username: " + receiverUsername);

        String senderIdQuery = "SELECT user_id FROM usr WHERE username = ?";
        String receiverIdQuery = "SELECT user_id FROM usr WHERE username = ?";

        int senderId = 0;
        int receiverId = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get sender_id
            try (PreparedStatement psSender = conn.prepareStatement(senderIdQuery)) {
                psSender.setString(1, senderUsername);
                ResultSet rsSender = psSender.executeQuery();
                if (rsSender.next()) {
                    senderId = rsSender.getInt("user_id");
                }
            }

            // Debugging: Check sender_id
            System.out.println("Sender ID: " + senderId);

            // Get receiver_id
            try (PreparedStatement psReceiver = conn.prepareStatement(receiverIdQuery)) {
                psReceiver.setString(1, receiverUsername);
                ResultSet rsReceiver = psReceiver.executeQuery();
                if (rsReceiver.next()) {
                    receiverId = rsReceiver.getInt("user_id");
                }
            }

            // Debugging: Check receiver_id
            System.out.println("Receiver ID: " + receiverId);

            // Proceed if both sender and receiver ids are valid
            if (senderId > 0 && receiverId > 0) {
                String query = "INSERT INTO message (sender_id, receiver_id, msg, time_stamp, has_file) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement psMessage = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psMessage.setInt(1, senderId);
                    psMessage.setInt(2, receiverId);
                    psMessage.setString(3, request.getParameter("msg")); // Message content
                    psMessage.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis())); // Current timestamp
                    psMessage.setBoolean(5, false); // Initially no file

                    int rowsAffected = psMessage.executeUpdate();
                    if (rowsAffected > 0) {
                        ResultSet generatedKeys = psMessage.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            msgId = generatedKeys.getInt(1); // Get the auto-generated msg_id
                        }
                    }
                }
            } else {
                System.out.println("Invalid sender or receiver ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return msgId;
    }

private String saveFileOnServer(Part part, String fileName) {
    String uploadDir = "uploads"; // Define the directory to save files
    Path uploadPath = Paths.get(uploadDir);

    // Ensure the upload directory exists
    if (!Files.exists(uploadPath)) {
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Add a unique timestamp to the file name to avoid conflicts
    String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
    Path filePath = uploadPath.resolve(uniqueFileName);

    try (InputStream fileContent = part.getInputStream()) {
        // Save the file to the server
        Files.copy(fileContent, filePath);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }

    return filePath.toString(); // Return the file path
}


    private boolean saveFileDetails(int msgId, String fileName, String fileType, long fileSize, String filePath) {
        String query = "INSERT INTO file (msg_id, file_name, file_type, file_size, file_path) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, msgId);
            ps.setString(2, fileName);
            ps.setString(3, fileType);
            ps.setLong(4, fileSize);
            ps.setString(5, filePath);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateMessageHasFile(int msgId) {
        String query = "UPDATE message SET has_file = true WHERE msg_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, msgId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {    
        return "Servlet for file uploads with message creation and database integration";
    }
}
