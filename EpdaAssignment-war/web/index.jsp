<%--
    Document   : index
    Created on : Feb 27, 2024, 5:50:50 PM
    Author     : USER
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="service.Util"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%-- Check if authorized --%>
<%@include file="checkSession.jsp"%>
<c:if test = "${sessionScope.user.getRole().getDescription() eq 'customer'}">
    <script async>
        (async () => {
            await alert(" Customer account cannot used to login.");
            window.location.href = "Logout";
        })();
    </script>
</c:if>


<script>
    // Check if set parameter
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('updateSuccess')) {
        alert('Update Success');
    } else if (urlParams.has("addCustomerSuccess")) {
        alert("New customer is added");
    } else if (urlParams.has("addPetSuccess")) {
        alert("New pet is added");
    }
</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
    </head>
    <body>
        <h1><%= Util.generateGreeting()%> ${sessionScope.user.getName()} </h1>
        <p><%= Util.randomGenerateWelcomeMessage()%>  </p>
        <br/>
        <%-- Check if user is a Vet --%>
        <c:if test="${sessionScope.user.getRole().getDescription() eq 'vet'}">
            <p>Welcome, Vet ~</p>
            <p>What would you like to do today?</p>
            <ul>
                <li><a href="editProfile.jsp">Edit Personal Profile</a></li>
                <li><a href="viewMedicalReport.jsp">View Diagnosis and Prognosis</a></li>
                <li><a href="viewAppointment.jsp">View My Appointments</a></li>
                <li><a href="Logout">Logout</a></li>
            </ul>
        </c:if>

        <%-- Check if user is a Receptionist --%>
        <c:if test="${sessionScope.user.getRole().getDescription() eq 'receptionist'}">
            <p>Welcome, Receptionist ~</p>
            <p>What would you like to do today?</p>
            <ul>
                <li><a href="editProfile.jsp">Edit Personal Profile</a></li>
                <li><a href="manageAppointment.jsp">Manage Appointments</a></li>
                <li><a href="addCustomerPet.jsp">Add Customer and Pet Profile</a></li>
                <li><a href="Logout">Logout</a></li>
            </ul>
        </c:if>
        <%-- Check if user is a manager --%>
        <c:if test="${sessionScope.user.getRole().getDescription() eq 'managing staff'}">
            <p>Welcome, Manager ~</p>
            <p>What would you like to do today?</p>
            <ul>
                <li><a href="editProfile.jsp">Edit Personal Profile</a></li>
                <li><a href="manageStaff.jsp">Manage Staffs</a></li>
                <li><a href="createSchedule.jsp">Create Working Rota</a></li>
                <li><a href="report.jsp">Generate Report</a></li>
                <li><a href="Logout">Logout</a></li>
            </ul>
        </c:if>
    </body>
</html>
