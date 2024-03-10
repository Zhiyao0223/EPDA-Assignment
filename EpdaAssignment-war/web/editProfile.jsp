<%--
    Document   : editProfile
    Created on : Mar 11, 2024, 1:53:33 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Profile</title>
    </head>
    <body>
        <a href='index.jsp'>< Back</a>
        <h1>Edit Personal Profile</h1>
        <form action="updateProfile.jsp" method="post">
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" value="John Doe" readonly><br><br>

            <label for="dob">Date of Birth:</label>
            <input type="date" id="dob" name="dob"><br><br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email"><br><br>

            <label for="gender">Gender:</label>
            <select id="gender" name="gender">
                <option value="m">Male</option>
                <option value="f">Female</option>
                <option value="o">Other</option>
            </select><br><br>

            <label for="phone">Phone Number:</label>
            <input type="text" id="phone" name="phone"><br><br>

            Password: <button type="button" onclick="openPasswordPopup()">Edit Password</button>
            <br/><br/>
            <input type="submit" value="Save Changes">
        </form>

        <!-- Password Popup -->
        <!--        <div id="passwordPopup" style="display: none;">
                    <h2>Change Password</h2>
                    <form action="changePassword.jsp" method="post">
                        <label for="currentPassword">Current Password:</label>
                        <input type="password" id="currentPassword" name="currentPassword"><br><br>

                        <label for="newPassword">New Password:</label>
                        <input type="password" id="newPassword" name="newPassword"><br><br>

                        <label for="confirmPassword">Confirm New Password:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword"><br><br>

                        <input type="submit" value="Change Password">
                    </form>
                </div>-->

        <script>
            function openPasswordPopup() {
                document.getElementById("passwordPopup").style.display = "block";
            }
        </script>
    </body>
</html>
