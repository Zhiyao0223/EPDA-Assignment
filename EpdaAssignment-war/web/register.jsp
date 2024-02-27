<%--
    Document   : register
    Created on : Feb 27, 2024, 5:38:41 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    <body>
        <a href="login.jsp">< Back</a>
        <br><br><br>
        <form action="Register" method="POST">
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" size="20"></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="text" name="password" size="20"></td>
                </tr>
                <tr>
                    <td>Confirm Password:</td>
                    <td><input type="text" name="confirmPass" size="20"></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td><input type="text" name="role" size="20"></td>
                </tr>
            </table>
            <p><input type="submit" value="Register"></p>
        </form>
    </body>
</html>
