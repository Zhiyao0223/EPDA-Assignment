<%--
    Document   : diagnosis
    Created on : Mar 11, 2024, 9:24:37 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Diagnosis and Prognosis</title>
    </head>
    <body>
        <h1>Report</h1>
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th></th>
                        <c:forEach items="${applicationScope.cachedMedicalReportHeader}" var="columnName">
                        <th>${columnName}</th>
                        </c:forEach>
                </tr>
            </thead>
            <tbody>
                <!-- Iterate over the report data and generate table rows -->
                <c:forEach items="${applicationScope.cachedAllMedicalReports}" var="data">
                    <tr>
                        <td>${data.column1}</td>
                        <!-- Add more cells as needed -->
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
