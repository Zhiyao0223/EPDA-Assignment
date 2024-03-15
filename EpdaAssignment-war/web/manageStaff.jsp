<%--
    Document   : manageStaff
    Created on : Mar 11, 2024, 9:55:21 PM
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
    if (urlParams.has('editSuccess')) {
        alert('Record modified successfully');
    }

    // Function to refetch database and filter by search
    function refreshContent() {
        var searchQuery = $('#searchInput').val().trim().toLowerCase();
        $.ajax({
            url: "ManageStaff",
            method: "GET",
            success: function (response) {
                // Clear existing table rows
                $("#staffTableBody").empty();

                // Flag to track if any rows were added
                var rowsAdded = false;

                // Iterate over JSON data and populate table
                $.each(response, function (index, staff) {
                    // Filter based on name
                    if (staff.name.toLowerCase().includes(searchQuery)) {
                        // Check for undefined values and replace them with "-"
                        var email = staff.email !== undefined ? staff.email : "-";
                        var gender = staff.gender !== undefined ? staff.gender : "-";
                        var phoneNo = staff.phoneNo !== undefined ? staff.phoneNo : "-";
                        var dateOfBirth = staff.dateOfBirth !== undefined ? staff.dateOfBirth : "-";
                        var status = staff.status == 0 ? "Active" :
                                staff.status == 1 ? "Pending Approval" :
                                staff.status == 2 ? "Suspended" : "Deleted";

                        // Create a delete button for each row with a confirmation dialog
                        var editButton = "<button type='submit'><i class='fa fa-edit' style='font-size:20px;'></i></button>";
                        var deleteButton = "<button onclick='confirmDelete(" + staff.id + ")'><i class='fa fa-trash' style='font-size:20px;'></i></button>";
                        // Create a table row and append it to the table body
                        var row = "<tr>" +
                                "<td>" + staff.id + "</td>" +
                                "<td>" + staff.name + "</td>" +
                                "<td>" + email + "</td>" +
                                "<td>" + gender + "</td>" +
                                "<td>" + phoneNo + "</td>" +
                                "<td>" + dateOfBirth + "</td>" +
                                "<td>" + staff.role.description + "</td>" +
                                "<td>" + status + "</td>" +
                                "<td>" + staff.createdDate + "</td>" +
                                "<td>" + staff.updatedDate + "</td>" +
                                "<td><form action='editStaff' method='GET'><input type='hidden' name='userId' value='" + staff.id + "'/>" + editButton + "</form></td>" +
                                "<td>" + deleteButton + "</td>" +
                                "</tr>";

                        $("#staffTableBody").append(row);
                        rowsAdded = true;
                    }
                });
                // If no rows were added, display a message indicating no results found
                if (!rowsAdded) {
                    $("#staffTableBody").append("<tr><td colspan='11'>No results found</td></tr>");
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

    function confirmDelete(staffId) {
        if (confirm("Are you sure you want to delete this user?")) {
            $.ajax({
                url: "ManageStaff",
                method: "POST",
                data: {id: staffId},
                success: function (response) {
                    // Check if response contains an error message
                    if (response) {
                        // Check which error
                        switch (response[0]) {
                            case "0":
                                alert("Delete success.");
                                break;
                            case "-1":
                                alert("Cannot delete self.");
                                break;
                            case "-2":
                                alert("Cannot delete someone same level with you.");
                                break;
                            default:
                                alert("Unknown error when deleting data.");
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
            <input type="text" id="searchInput" placeholder="Search by name...">
            <button value="searchSubmit" type="button" onclick=refreshContent()>Search</button>
            <button value="searchReset" type="reset" onclick=resetContent()()>Reset</button>
            <br/><br/>

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
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody id="staffTableBody"></tbody>
            </table>
        </div>
    </body>
</html>
