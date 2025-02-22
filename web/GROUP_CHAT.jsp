<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ page import="chat.Message" %>
<%@ page import="chat.DatabaseConnection"%>

<html>
<head>
    <title>Group Chat</title>
    <script type="text/javascript">
        function setGroupAndSubmit(groupName) {
            // Set the group name into the "ChatWith" input field
            document.getElementById("ChatWith").value = groupName;
            // Submit the form programmatically
            document.getElementById("chatForm").submit();
        }
    </script>
</head>
<body style="font-family: 'Arial', sans-serif; background-color: #f4f7fa; margin: 0; padding: 0;">
    <div class="container" style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-top: 30px; max-width: 800px; margin-left: auto; margin-right: auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
        <h2 class="heading" style="color:#0078d7;  font-size: 36px; font-weight: 600; margin-bottom: 20px; text-align: center;">Group Chat</h2>

        <!-- Form to create group -->
        <form action="groupchat" method="post" style="width: 100%; margin-bottom: 20px; ">
            <input type="hidden" name="action" value="createGroup">
            <label class="label" for="groupName" style="display: block; font-size: 18px; font-weight: bold; margin-bottom: 8px; color: #333;">Group Name:</label>
            <input type="text" id="groupName" name="groupName" required style="width: 100%; padding: 12px; font-size: 16px; border: 2px solid #ccc; border-radius: 6px; box-sizing: border-box; margin-bottom: 16px; background-color: #f9f9f9; transition: border-color 0.3s ease;">
            <input type="hidden" name="creatorId" value="1">
            <button type="submit" style="padding: 12px 20px; background-color: #0078d7; color: #fff; font-size: 18px; border: none; border-radius: 6px; cursor: pointer; transition: background-color 0.3s ease;">Create Group</button>
        </form>

        <!-- Form to chat with a group -->
        <form id="chatForm" action="groupchat" method="post" style="width: 100%; margin-bottom: 20px;display: none;">
            <input type="hidden" name="action" value="chatWith">
            <label class="label" for="ChatWith" >Chat With:</label>
            <input type="text" id="ChatWith" name="ChatWith" required style="width: 100%; padding: 12px; font-size: 16px; border: 2px solid #ccc; border-radius: 6px; box-sizing: border-box; margin-bottom: 16px; background-color: #f9f9f9; transition: border-color 0.3s ease;">
            <input type="hidden" name="creatorId" value="1">
            <button type="button" onclick="document.getElementById('chatForm').submit();" style="padding: 12px 20px; background-color: #4CAF50; color: #fff; font-size: 18px; border: none; border-radius: 6px; cursor: pointer; transition: background-color 0.3s ease;">Enter</button>
        </form>

        <h1 style="font-size: 24px; color: #333; margin-top: 20px; margin-bottom: 12px; font-weight: bold;">Groups:</h1>
        <ul style="list-style-type: none; padding: 0; margin: 0;">
            <% 
                Connection con = DatabaseConnection.getConnection();  
                List<String> Groupnames = new ArrayList<>();
                int id = (int) request.getSession().getAttribute("id");
                String username = (String) request.getSession().getAttribute("username");               
                String query = "SELECT Group_name FROM GRP WHERE User_ID=? OR JSON_CONTAINS(Members, JSON_ARRAY(?), '$')";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, id);
                ps.setString(2, username);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Groupnames.add(rs.getString("Group_name"));
                }
            %>
            <% 
                for (String Groupname : Groupnames) {
            %>
                <button  onclick="setGroupAndSubmit('<%= Groupname %>');" 
                        style="background-color: #add8e6 ; padding: 10px;
                        display:flex;
                        margin-left: 0;
                         margin-right: 0;
                         width:50vh;
                         
                         justify-content: center;
                         
                        margin-bottom: 8px; border-radius: 6px; font-size: 18px; color: #333; transition: background-color 0.3s ease; cursor: pointer;" 
                        ">
                    <%= Groupname %>
                </button>
            <% 
                }
            %>
        </ul>
    </div>
</body>
</html>
