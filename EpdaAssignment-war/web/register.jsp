<%--
    Document   : register
    Created on : Feb 27, 2024, 5:38:41 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    <body>
        <a href="${applicationScope.isAddStaff != null ?'manageStaff.jsp' : 'login.jsp'}">< Back</a>
        <br><br><br>
        <h1>Registration</h1>
        <form action="Register" method="POST">
            <input type="text" hidden name="isAddStaff" value="${applicationScope.isAddStaff}">
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" size="20" required></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" size="20" required></td>
                </tr>
                <tr>
                    <td>Confirm Password:</td>
                    <td><input type="password" name="confirmPass" size="20" required></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td>
                        <select name="role">
                            <c:forEach items="${applicationScope.cachedRoles}" var="role">
                                <option value="${role.getDescription()}">${role.getDescription()}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
            <p><input type="submit" value="Register"></p>
        </form>
    </body>
</html>
