<%--
    Document   : login
    Created on : Feb 27, 2024, 5:37:52 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Check if set parameters -->
<% if (request.getParameter("registrationSuccess") != null) {%>
<script>
    alert("Registration Success");
</script>
<% }%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <p>Welcome to Paws & Care Veterinary Clinic</p>
        <br/>
        <form action="Login" method="POST">
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" size="20"></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" size="20"></td>
                </tr>
            </table>
            <p><input type="submit" value="Login"></p>
        </form>
        <p>Don't have an account? <a href="Register">Sign up</a> </p>
    </body>
</html>
