<%--
    Document   : managePersonalAppointment
    Created on : Mar 12, 2024, 8:08:48 PM
    Author     : USER
--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"  />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="model.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="checkSession.jsp"%>

<script>
    // Function to refetch database and filter by search
    function refreshContent() {
        var searchQuery = $('#searchInput').val().trim().toLowerCase();
        $.ajax({
            url: "ViewAppointment",
            method: "GET",
            success: function (response) {
                console.log(response);

                // Clear existing table rows
                $("#appointmentTableBody").empty();
                // Flag to track if any rows were added
                var rowsAdded = false;

                // Iterate over JSON data and populate table
                $.each(response, function (index, appointment) {
                    // Filter based on pet name or cust name
                    if (appointment.petID.custID.name.toLowerCase().includes(searchQuery) ||
                            appointment.petID.name.toLowerCase().includes(searchQuery)) {
                        // Check for undefined values and replace them with "-"
                        var petName = appointment.petID.name !== undefined ? appointment.petID.name : "-";
                        var ownerName = appointment.petID.custID.name !== undefined ? appointment.petID.custID.name : "-";
                        var timeslot = appointment.schedule.timeslot !== undefined ? appointment.schedule.timeslot : "-";
                        var createdDate = appointment.createdDate !== undefined ? appointment.createdDate : "-";
                        var updatedDate = appointment.updatedDate !== undefined ? appointment.updatedDate : "-";

                        var status = appointment.status == 0 ? "Complete" :
                                appointment.status == 2 ? "Scheduled" : "Cancelled";

                        // Create a table row and append it to the table body
                        var row = "<tr>" +
                                "<td>" + appointment.id + "</td>" +
                                "<td>" + petName + "</td>" +
                                "<td>" + ownerName + "</td>" +
                                "<td>" + timeslot + "</td>" +
                                "<td>" + status + "</td>" +
                                "<td>" + createdDate + "</td>" +
                                "<td>" + updatedDate + "</td>" +
                                "</tr>";

                        $("#appointmentTableBody").append(row);
                        rowsAdded = true;
                    }
                });
                // If no rows were added, display a message indicating no results found
                if (!rowsAdded) {
                    $("#appointmentTableBody").append("<tr><td colspan='11'>No results found</td></tr>");
                }
            },
            error: function (_, __, error) {
                alert("Unknown error:" + error);
            }
        });
    }

    // Reset search
    function resetContent() {
        $('#searchInput').val("");
        refreshContent();
    }

    $(document).ready(function () {
        refreshContent();
    })
</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Staff Management</title>
    </head>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Appointment Dashboard</h1>
        <div>
            <!-- Description -->
            <p>This is the appointment dashboard. You can view your appointments here.</p>
        </div>
        <hr/>
        <div>
            <input type="text" id="searchInput" placeholder="Search by pet or customer name...">
            <button value="searchSubmit" type="button" onclick=refreshContent()>Search</button>
            <button value="searchReset" type="reset" onclick=resetContent()()>Reset</button>
            <br/><br/>

            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Pet Name</th>
                        <th>Owner Name</th>
                        <th>Time Slot</th>
                        <th>Appointment Status</th>
                        <th>Created Date</th>
                        <th>Updated Date</th>
                    </tr>
                </thead>
                <tbody id="appointmentTableBody"></tbody>
            </table>
        </div>
    </body>
</html>