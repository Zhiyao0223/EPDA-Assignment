<%--
    Document   : register
    Created on : Feb 27, 2024, 5:38:41 PM
    Author     : USER
--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
    function onRoleChange() {
        var firstExpertiseSelect = document.getElementById("animalType1");

        // Only execute if is vet
        if ($("#role").val() == "Vet") {
            $("#expertiseField1").show();
            $("#expertiseField2").show();
            firstExpertiseSelect.setAttribute("required", "true");
        } else {
            $("#expertiseField1").hide();
            $("#expertiseField2").hide();
            firstExpertiseSelect.removeAttribute("required");
        }
    }

</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    <body>
        <a href="${applicationScope.isAddStaff != null ?'manageStaff.jsp' : 'login.jsp'}">< Back</a>
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
                        <select id="role" name="role" onchange=onRoleChange() required>
                            <c:forEach items="${applicationScope.cachedRoles}" var="role">
                                <option value="${role.getDescription()}">${role.getDescription()}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr id="expertiseField1" hidden>
                    <td>Expertise 1: </td>
                    <td>
                        <select id="animalType1" name="animalType1">
                            <option value="" selected disabled>Please Select</option>
                            <c:forEach items="${applicationScope.cacheAnimalTypes}" var="types">
                                <option value="${types.getId()}">${types.getDescription()}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr id="expertiseField2" hidden>
                    <td>Expertise 2: </td>
                    <td>
                        <select id="animalType2" name="animalType2">
                            <option value="" selected disabled>Please Select</option>
                            <c:forEach items="${applicationScope.cacheAnimalTypes}" var="types">
                                <option value="${types.getId()}">${types.getDescription()}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
            <p><input type="submit" value="Register"></p>
        </form>
    </body>
</html>
