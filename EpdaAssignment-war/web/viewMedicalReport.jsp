<%--
    Document   : viewMedicalReport
    Created on : Mar 13, 2024, 2:34:23 AM
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
    // Get the current URL
    const urlParams = new URLSearchParams(window.location.search);
    // Check if the specific params exists in the URL
    if (urlParams.has('addPrognosisSuccess')) {
        alert('Prognosis added succesfully!');
    }

    // Open pronogsis popup
    function openPrognosisPopup(tmpId) {
        document.getElementById("prognosisPopup").style.display = "block";
        document.getElementById("reportId").value = tmpId;
    }

    // Function to refetch database and filter by search
    function refreshContent() {
        var searchQuery = $('#searchInput').val().trim().toLowerCase();
        $.ajax({
            url: "ViewMedicalReport",
            method: "GET",
            success: function (response) {
                console.log(response);

                // Clear existing table rows
                $("#medicalReportTableBody").empty();
                // Flag to track if any rows were added
                var rowsAdded = false;

                // Iterate over JSON data and populate table
                $.each(response, function (index, report) {
                    // Filter based on pet name or cust name
                    if (report.appointment.petID.custID.name.toLowerCase().includes(searchQuery) ||
                            report.appointment.petID.name.toLowerCase().includes(searchQuery)) {
                        // Check for undefined values and replace them with "-"
                        var appointmentTimeslot = report.appointment.schedule.timeslot !== undefined ? report.appointment.schedule.timeslot : "-";
                        var petName = report.appointment.petID.name !== undefined ? report.appointment.petID.name : "-";
                        var petType = report.appointment.petID.type.description !== undefined ? report.appointment.petID.type.description : "-";
                        var ownerName = report.appointment.petID.custID.name !== undefined ? report.appointment.petID.custID.name : "-";
                        var diagnosisDetail = report.diagnosisDetail !== "" ? report.diagnosisDetail : "-";
                        var prognosisDetail = report.prognosisDetail !== "" ? report.prognosisDetail : "-";
                        var createdDate = report.createdDate !== undefined ? report.createdDate : "-";
                        var updatedDate = report.updatedDate !== undefined ? report.updatedDate : "-";
                        var status = report.status === 0 ? "Completed" : "Pending Prognosis";

                        // Create a add button for each row to add prognosis (Only show if need prognosis
                        var addButton = (report.status === 1)
                                ? "<button type='submit' onclick=openPrognosisPopup(" + report.id + ")><i class='fa fa-plus' style='font-size:20px;'></i></button>"
                                : "-";

                        // Create a table row and append it to the table body
                        var row = "<tr>" +
                                "<td>" + report.id + "</td>" +
                                "<td>" + appointmentTimeslot + "</td>" +
                                "<td>" + ownerName + "</td>" +
                                "<td>" + petName + "</td>" +
                                "<td>" + petType + "</td>" +
                                "<td>" + diagnosisDetail + "</td>" +
                                "<td>" + prognosisDetail + "</td>" +
                                "<td>" + status + "</td>" +
                                "<td>" + createdDate + "</td>" +
                                "<td>" + updatedDate + "</td>" +
                                "<td>" + addButton + "</td>" +
                                "</tr>";

                        $("#medicalReportTableBody").append(row);
                        rowsAdded = true;
                    }
                });
                // If no rows were added, display a message indicating no results found
                if (!rowsAdded) {
                    $("#medicalReportTableBody").append("<tr><td colspan='11'>No results found</td></tr>");
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
        <title>Diagnosis and Prognosis</title>
    </head>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Medical Report Dashboard</h1>
        <div>
            <!-- Description -->
            <p>This is the medical report dashboard. You can view your report history for pets' diagnosis and prognosis here.</p>
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
                        <th>Appointment Timeslot</th>
                        <th>Owner Name</th>
                        <th>Pet Name</th>
                        <th>Pet Type</th>
                        <th>Diagnosis Detail</th>
                        <th>Prognosis Detail</th>
                        <th>Status</th>
                        <th>Created Date</th>
                        <th>Updated Date</th>
                        <th>Add Prognosis</th>
                    </tr>
                </thead>
                <tbody id="medicalReportTableBody"></tbody>
            </table>
        </div>
        <br/><br/><br/>

        <!-- Password Popup -->
        <div id="prognosisPopup" style="display: none;">
            <h2>Add Prognosis Details</h2>
            <form action="ViewMedicalReport" method="POST">
                <label for="prognosisLabel">Prognosis:</label><br/>
                <textarea rows="4" cols="50" placeholder="Enter your prognosis here..." name="prognosis" id="prognosis" required="true"></textarea>
                <br/><br/>

                <input type="text" id="reportId" name="reportId" value="" hidden>
                <input type="submit" value="Save">
            </form>
        </div>
    </body>
</html>
