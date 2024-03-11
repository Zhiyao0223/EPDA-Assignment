<%--
    Document   : manageStaff
    Created on : Mar 11, 2024, 9:55:21 PM
    Author     : USER
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="model.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Staff Management</title>
    </head>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Staff Management Dashboard</h1>
        <div>
            <!-- Description -->
            <p>This is the staff management dashboard. You can manage staff members here.</p>
            <!-- Add button -->
            <button onclick="location.href = 'Register?addStaff=true'">Add Staff</button>
        </div>
        <hr/>
        <div>
            <!-- Table of staff members -->
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Gender</th>
                        <th>Phone No</th>
                        <th>Date of Birth</th>
                        <th>Role ID</th>
                        <th>Status</th>
                        <th>Created Date</th>
                        <th>Updated Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${applicationScope.staffList}" var="staff">
                        <tr>
                            <td>${staff.getUserID()}</td>
                            <td>${staff.getName()}</td>
                            <td>${staff.getEmail()}</td>
                            <td>${staff.getGender()}</td>
                            <td>${staff.getPhoneNo()}</td>
                            <td>${staff.getDateOfBirth()}</td>
                            <td>${staff.getRoleID().getDescription()}</td>
                            <td>${staff.getStatus() == '0'? "Active" : "Pending Approval"}</td>
                            <td>${staff.getCreatedDate()}</td>
                            <td>${staff.getUpdatedDate()}</td>
                            <td>
                                <form action="editStaff" method="GET">
                                    <input type="hidden" name="userId" value="${staff.getUserID()}"/>
                                    <button type="submit">Edit</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
