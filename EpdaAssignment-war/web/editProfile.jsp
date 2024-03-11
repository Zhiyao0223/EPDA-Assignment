<%--
    Document   : editProfile
    Created on : Mar 11, 2024, 1:53:33 AM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="service.Util"%>
<%@page import="model.Users"%>

<script>
    function openPasswordPopup() {
        document.getElementById("passwordPopup").style.display = "block";
    }

    document.addEventListener("DOMContentLoaded", function () {
        // Function to check if a query parameter is present in the URL
        function hasQueryParam(paramName) {
            var urlParams = new URLSearchParams(window.location.search);
            return urlParams.has(paramName);
        }

        // Function to check if a parameter is present in the session
        function hasSessionParam(paramName) {
            var paramValue = sessionStorage.getItem(paramName);
            return paramValue !== null && paramValue !== undefined;
        }

        // Check if change password
        if (hasQueryParam("changePassSuccess")) {
            alert("Password Changed");
        }
        // Check if change pasword error
        else if (hasSessionParam("isChangePass")) {
            openPasswordPopup();
        }
    });
</script>

<%
    // Get the gender value from the session scope
    Users currentUser = (Users) session.getAttribute("user");

    // Define gender options
    String[][] genderOptions = {
        {"m", "Male"},
        {"f", "Female"},
        {"-", "Please Select"}
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
            <input type="text" id="name" name="name" value=${sessionScope.user.getName()} readonly>
            <br><br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value=${sessionScope.user.getEmail()}>
            <br><br>

            <label for="dob">Date of Birth:</label>
            <input type="date" id="dob" name="dob" value=${sessionScope.user.getDateOfBirth()}>
            <br><br>

            <label for="gender">Gender:</label>
            <select id="gender" name="gender">
                <% for (String[] option : genderOptions) {%>
                <option value="<%= option[0]%>"
                        <%= option[0].equals(currentUser.getGender()) ? "selected" : ""%>
                        <%= option[0].equals("-") ? "disabled" : ""%>><%= option[1]%></option> <!-- Disable placeholder selection -->
                <% }%>
            </select>
            <br/><br/>

            <label for="phone" >Phone Number:</label>
            <input type="tel" id="phone" name="phone" value=${sessionScope.user.getPhoneNo()}><br><br>

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
                <input type="password" id="currentPassword" name="currentPassword"><br><br>

                <label for="newPassword">New Password:</label>
                <input type="password" id="newPassword" name="newPassword"><br><br>

                <label for="confirmPassword">Confirm New Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword">
                <br><br>

                <input type="submit" value="Change Password">
            </form>
        </div>


    </body>
</html>
