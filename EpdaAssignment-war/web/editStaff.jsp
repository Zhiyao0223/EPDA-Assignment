<%--
    Document   : editStaff
    Created on : Mar 11, 2024, 10:29:39 PM
    Author     : USER
--%>

<%@page import="model.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Get the gender value from the session scope
    Users currentUser = (Users) session.getAttribute("user");
    Users editUser = (Users) application.getAttribute("editUser");

    // Define gender options
    String[][] genderOptions = {
        {"m", "Male"},
        {"f", "Female"},
        {"-", "Please Select"}
    };

    String[][] statusOptions = {
        {"0", "Active"},
        {"1", "Pending Approval"},
        {"2", "Suspended"},
        {"3", "Deleted"}
    };
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Staff Profile</title>
    </head>
    <body>
        <a href='manageStaff.jsp'>< Back</a>
        <h1>Edit Staff Profile</h1>
        <form action="editStaff" method="post">
            <label for="name">Name:</label>
            <input type="text" id="userid" name="userid" value=${applicationScope.editUser.getUserID()} hidden>
            <input type="text" id="name" name="name" value=${applicationScope.editUser.getName()} readonly>
            <br><br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value=${applicationScope.editUser.getEmail()}>
            <br><br>

            <label for="dob">Date of Birth:</label>
            <input type="date" id="dob" name="dob" value=${applicationScope.editUser.getDateOfBirth()}>
            <br><br>

            <label for="gender">Gender:</label>
            <select id="gender" name="gender">
                <% for (String[] option : genderOptions) {%>
                <option value="<%= option[0]%>"
                        <%= option[0].equals(editUser.getGender()) ? "selected" : ""%>
                        <%= option[0].equals("-") ? "disabled" : ""%>><%= option[1]%></option> <!-- Disable placeholder selection -->
                <% }%>
            </select>
            <br/><br/>

            <label for="phone" >Phone Number:</label>
            <input type="tel" id="phone" name="phone" value=${applicationScope.editUser.getPhoneNo()}>
            <br/><br/>

            <label for="status" >Status:</label>
            <select id="status" name="status">
                <% for (String[] option : statusOptions) {%>
                <option value="<%= option[0]%>"
                        <%= option[0].equals(String.valueOf(editUser.getStatus())) ? "selected" : ""%>
                        <%= option[0].equals("1") ? "disabled" : ""%>><%= option[1]%></option> <!-- Disable placeholder selection -->
                <% }%>
            </select>
            <br/><br/>

            <input type="submit" value="Save Changes">
            <br/>
        </form>
    </body>
</html>
