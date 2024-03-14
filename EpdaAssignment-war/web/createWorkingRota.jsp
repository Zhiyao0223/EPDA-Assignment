<%--
    Document   : createWorkingRota
    Created on : Mar 14, 2024, 2:04:52 AM
    Author     : USER
--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script>
    function addRota() {
        $.ajax({
            url: "CreateWorkingRota",
            method: "POST",
            success: function (response) {
                console.log(response);

                // Hide search box
                $("#searchBox").hide();

                // Change table labe
                $("#tableLabel").text("New Working Rota");

                // Clear table row
                $("#rotaTableBody").empty();

                // Clear table header and append new
                $("#rotaTableHeader").empty();
                $("#rotaTableHeader").append("<th>Day</th>");
                $("#rotaTableHeader").append("<th>Vet 1</th>");
                $("#rotaTableHeader").append("<th>Vet 2</th>");
                $("#rotaTableHeader").append("<th>Vet 3</th>");
                $("#rotaTableHeader").append("<th>Vet 4</th>");

                // Iterate over each day in the JSON object
                $.each(response, function (day, vets) {
                    var row = $("<tr>");
                    row.append($("<td>").text(day));

                    // Iterate over each vet for the current day
                    $.each(vets, function (_, vet) {
                        // Format timeslot Mar 18, 2024 3:00:00 PM
                        let tmpPart = vet.timeslots[0].split(":");
                        var firstTimeslot = tmpPart[0] + ":" + tmpPart[1] + ":" + tmpPart[2].slice(-2);

                        tmpPart = vet.timeslots[1].split(":");
                        var secondTimeslot = tmpPart[0] + ":" + tmpPart[1] + ":" + tmpPart[2].slice(-2);

                        var cell = $("<td>");
                        cell.append($("<div>").text(vet.name));
                        cell.append($("<div>").text("Expertise: " + vet.expertiseAreas.join(", ")));
                        cell.append($("<div>").text("Timeslot 1: " + firstTimeslot));
                        cell.append($("<div>").text("Timeslot 2: " + secondTimeslot));
                        row.append(cell);
                    });

                    // Ensure there are at least 4 <td> elements
                    if (vets.length < 4) {
                        var placeholderCell = $("<td style='text-align: center;'>").text("No vet available");
                        row.append(placeholderCell);
                    }

                    // Append the row to the table body
                    $("#rotaTableBody").append(row);

                });
                alert("Working rota successfully generate.");
            },
            error: function (_, __, error) {
                alert("Unknown error: " + error);
            }
        });
    }

    function viewRota() {
        var searchQuery = $('#searchInput').val().trim().toLowerCase();

        $.ajax({
            url: "CreateWorkingRota",
            method: "GET",
            success: function (responses) {
                console.log(responses);

                var rowAdded = false;

                // Show search box
                $("#searchBox").show();

                // Change table label
                $("#tableLabel").text("Existing Working Rota");

                // Clear table header and append new
                $("#rotaTableHeader").empty();
                $("#rotaTableHeader").append("<th>Staff ID</th>");
                $("#rotaTableHeader").append("<th>Staff Name</th>");
                $("#rotaTableHeader").append("<th>Expertises</th>");
                $("#rotaTableHeader").append("<th>Timeslot</th>");

                // Object to hold vets' data grouped by staffId
                var vetsData = {};

                // Group schedule by staffId
                responses.schedule.forEach(function (schedule) {
                    var staffId = schedule.staffId;
                    if (!vetsData[staffId]) {
                        vetsData[staffId] = {"name": schedule.staffName, "timeslots": []};
                    }
                    vetsData[staffId].timeslots.push(schedule.timeslots);
                });

                // Add expertise to vetsData
                responses.expertise.forEach(function (expertise) {
                    var staffId = expertise.staffId;
                    if (vetsData[staffId]) {
                        if (!vetsData[staffId].expertiseAreas) {
                            vetsData[staffId].expertiseAreas = [];
                        }
                        vetsData[staffId].expertiseAreas.push(expertise.animalTypeName);
                    }
                });

                // Clear the table body
                $("#rotaTableBody").empty();

                // Iterate over each vet's data and append to the table
                Object.keys(vetsData).forEach(function (staffId) {
                    var vetData = vetsData[staffId];

                    if (vetData.name.toLowerCase().includes(searchQuery)) {
                        var row = $("<tr>");
                        row.append($("<td>").text(staffId));
                        row.append($("<td>").text(vetData.name));

                        // Append expertises
                        row.append($("<td>").text(vetData.expertiseAreas.join(", ")));

                        // Append timeslot
                        ul = $("<ul>");
                        td = $("<td>");
                        vetData.timeslots.forEach(function (slot) {
                            ul.append($("<li>").text(slot));
                        });
                        td.append(ul);
                        row.append(td);

                        // Append the row to the table body
                        $("#rotaTableBody").append(row);
                        rowAdded = true;
                    }
                });

                if (!rowAdded) {
                    $("#rotaTableBody").append("<tr><td colspan='5'>No active schedule found</td></tr>");
                }
            },
            error: function (_, __, error) {
                alert("Unknown error: " + error);
            }
        });
    }

    // Reset search
    function resetRota() {
        $('#searchInput').val("");
        viewRota();
    }

    $(document).ready(function () {
        viewRota();
    })

</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Working Rota</title>
    </head>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Working Rota Dashboard</h1>
        <div>
            <!-- Description -->
            <p>This is the working rota dashboard. You can view and add new working rota here.</p>
            <button onclick=viewRota()>View Existing Rota</button>
            <button onclick=addRota()>Generate New Timetable</button>
        </div>
        <hr/>
        <div id="searchBox">
            <input type="text" id="searchInput" placeholder="Search by staff name...">
            <button value="searchSubmit" type="button" onclick=viewRota()>Search</button>
            <button value="searchReset" type="reset" onclick=resetRota()()>Reset</button>
            <br/><br/>
        </div>


        <h3 id="tableLabel"></h3>
        <table border="1">
            <thead>
                <tr id="rotaTableHeader"></tr>
            </thead>
            <tbody id="rotaTableBody"></tbody>
        </table>
        <br/>
    </body>
</html>
</body>
</html>
