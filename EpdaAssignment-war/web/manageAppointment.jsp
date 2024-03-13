<%--
    Document   : manageAppointment
    Created on : Mar 13, 2024, 2:31:26 AM
    Author     : USER
--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"  />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="model.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script>
    // Get the current URL
    const urlParams = new URLSearchParams(window.location.search);
    // Check if the specific params exists in the URL
    if (urlParams.has('editSuccess')) {
        alert('Record modified successfully');
    } else if (urlParams.has('addAppointmentSuccess')) {
        alert('Appointment add successfully');
    }

    // Function to refetch database and filter by search
    function refreshContent() {
        var searchQuery = $('#searchInput').val().trim().toLowerCase();
        $.ajax({
            url: "ManageAppointment",
            method: "GET",
            success: function (response) {
                console.log(response);

                // Clear existing table rows
                $("#appointmentTableBody").empty();

                // Flag to track if any rows were added
                var rowsAdded = false;

                // Iterate over JSON data and populate table
                $.each(response, function (index, appointment) {
                    // Filter based on customer and pet name
                    if (appointment.petID.name.toLowerCase().includes(searchQuery) ||
                            appointment.petID.custID.name.toLowerCase().includes(searchQuery)) {
                        // Check for undefined values and replace them with "-"
                        var ownerName = appointment.petID.custID.name !== undefined ? appointment.petID.custID.name : "-";
                        var petName = appointment.petID.name !== undefined ? appointment.petID.name : "-";
                        var petType = appointment.petID.type.description !== undefined ? appointment.petID.type.description : "-";
                        var timeslot = appointment.schedule.timeslot !== undefined ? appointment.schedule.timeslot : "-";
                        var staffName = appointment.schedule.staffId.name !== undefined ? appointment.schedule.staffId.name : "-";
                        var status = appointment.status == 0 ? "Complete" :
                                appointment.status == 1 ? "Pending vet" :
                                appointment.status == 2 ? "Scheduled" : "Cancelled";

                        // Create a edit and delete button
                        var editButton = (appointment.status != 3)
                                ? "<button type='submit'><i class='fa fa-edit' style='font-size:20px;'></i></button>"
                                : "-";
                        var cancelButton = (appointment.status != 3)
                                ? "<button onclick='confirmDelete(" + appointment.id + ")'><i class='fa fa-times' style='font-size:20px;'></i></button>"
                                : "-";

                        // Create a table row and append it to the table body
                        var row = "<tr>" +
                                "<td>" + appointment.id + "</td>" +
                                "<td>" + ownerName + "</td>" +
                                "<td>" + petName + "</td>" +
                                "<td>" + petType + "</td>" +
                                "<td>" + timeslot + "</td>" +
                                "<td>" + staffName + "</td>" +
                                "<td>" + status + "</td>" +
                                "<td>" + appointment.createdDate + "</td>" +
                                "<td>" + appointment.updatedDate + "</td>" +
                                "<td><form action='editAppointment' method='GET'><input type='hidden' name='userId' value='" + appointment.id + "'/>" + editButton + "</form></td>" +
                                "<td>" + cancelButton + "</td>" +
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
            error: function (xhr, status, error) {
                alert("Unknown error");
            }
        });
    }

    // Reset search
    function resetContent() {
        $('#searchInput').val("");
        refreshContent();
    }

    function confirmDelete(appointmentId) {
        if (confirm("Are you sure you want to cancel this appointment?")) {
            $.ajax({
                url: "ManageAppointment",
                method: "POST",
                data: {id: appointmentId},
                success: function (response) {
                    // Check if response contains an error message
                    if (response) {
                        // Check which error
                        switch (response[0]) {
                            case "0":
                                alert("Cancel success.");
                                break;
                            case "-1":
                                alert("Unable to process request. Appointment is already complete.");
                                break;
                            case "-2":
                                alert("Unable to process request. Appointment is already cancelled.");
                                break;
                            default:
                                alert("Unknown error when cancelling appointment.");
                        }
                    }
                    // Refresh table
                    refreshContent();
                },
                error: function (xhr, status, error) {
                    alert("Error deleting record");
                }
            });
        }
    }

    $(document).ready(function () {
        refreshContent();
    })
</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Appointment Management</title>
    </head>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Appointment Dashboard</h1>
        <div>
            <!-- Description -->
            <p>This is the appointment management dashboard. You can manage vets' appointments here.</p>
            <!-- Add button -->
            <button onclick="location.href = 'addAppointment.jsp'">Add Appointment</button>
        </div>
        <hr/>
        <div>
            <input type="text" id="searchInput" placeholder="Search by customer or pet name...">
            <button value="searchSubmit" type="button" onclick=refreshContent()>Search</button>
            <button value="searchReset" type="reset" onclick=resetContent()()>Reset</button>
            <br/><br/>

            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Owner Name</th>
                        <th>Pet Name</th>
                        <th>Pet Type</th>
                        <th>Time Slot</th>
                        <th>Staff Name</th>
                        <th>Status</th>
                        <th>Created Date</th>
                        <th>Updated Date</th>
                        <th>Edit</th>
                        <th>Cancel</th>
                    </tr>
                </thead>
                <tbody id="appointmentTableBody"></tbody>
            </table>
        </div>
    </body>
</html>