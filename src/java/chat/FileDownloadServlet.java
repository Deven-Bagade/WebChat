package chat;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class FileDownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileId = request.getParameter("fileId");

        // Log the received fileId
        System.out.println("Received fileId: " + fileId);

        if (fileId == null || fileId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File ID is missing.");
            return;
        }

        Connection conn = null; // Replace with your database connection logic
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Get the file details from the database
            String query = "SELECT FILE_NAME, FILE_PATH, FILE_TYPE FROM FILE WHERE FILE_ID = ?";
            conn = DatabaseConnection.getConnection(); // Replace with your database connection method
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(fileId));
            rs = stmt.executeQuery();

            if (rs.next()) {
                String fileName = rs.getString("FILE_NAME");
                String filePath = rs.getString("FILE_PATH");
                String fileType = rs.getString("FILE_TYPE");

                // Set response headers
                response.setContentType(fileType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                // Read the file and write it to the response output stream
                File file = new File(filePath);
                if (!file.exists()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
                    return;
                }

                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = response.getOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found in database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving file.");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
