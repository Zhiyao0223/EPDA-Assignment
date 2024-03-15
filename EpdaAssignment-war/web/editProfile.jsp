<%--
    Document   : editProfile
    Created on : Mar 11, 2024, 1:53:33 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="service.Util"%>
<%@page import="model.Users"%>

<%@include file="checkSession.jsp"%>
<script>
    function openPasswordPopup() {
        document.getElementById("passwordPopup").style.display = "block";
    }

    var urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has("changePassSuccess")) {
        alert("Password change successfully");
    }

    var isChangePass = "${requestScope.isChangePass}";
    if (isChangePass === "true") {
        openPasswordPopup();
    }


</script>

<%
    // Get the gender value from the session scope
    Users currentUser = (session != null) ? (Users) session.getAttribute("user") : null;

    // Define gender options
    String[][] genderOptions = {
        {"", "Please Select"},
        {"m", "Male"},
        {"f", "Female"}
    };
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Profile</title>
    </head>
    <body>
        <a href='index.jsp'>< Back</a>
        <h1>Edit Personal Profile</h1>
        <form action="editProfile" method="post">
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" style="background-color:  #f0f0f0;font-style: italic; cursor: auto"
                   value=${sessionScope.user.getName()} readonly>
            <br><br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value=${sessionScope.user.getEmail()} required>
            <br><br>

            <label for="dob">Date of Birth:</label>
            <input type="date" id="dob" name="dob" value=${sessionScope.user.getDateOfBirth()} required>
            <br><br>

            <label for="gender">Gender:</label>
            <select id="gender" name="gender" required>
                <% for (String[] option : genderOptions) {%>
                <option value="<%= option[0]%>"
                        <%= option[0].equals(currentUser.getGender()) ? "selected" : ""%>
                        <%= option[0].equals("") ? "disabled" : ""%>><%= option[1]%></option> <!-- Disable placeholder selection -->
                <% }%>
            </select>
            <br/><br/>

            <label for="phone" >Phone Number:</label>
            <input type="tel" id="phone" name="phone" value=${sessionScope.user.getPhoneNo()} required><br><br>

            Password: <button type="button" onclick="openPasswordPopup()">Edit Password</button>
            <br/><br/>

            <input type="submit" value="Save Changes">
            <br/>
        </form>

        <!-- Password Popup -->
        <div id="passwordPopup" style="display: none;">
            <h2>Change Password</h2>
            <form action="ChangePassword" method="post">
                <label for="currentPassword">Current Password:</label>
                <input type="password" id="currentPassword" name="currentPassword" required><br><br>

                <label for="newPassword">New Password:</label>
                <input type="password" id="newPassword" name="newPassword" required><br><br>

                <label for="confirmPassword">Confirm New Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                <br><br>

                <input type="submit" value="Change Password">
            </form>
        </div>


    </body>
</html>
