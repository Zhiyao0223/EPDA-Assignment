<%--
    Document   : createWorkingRota
    Created on : Mar 14, 2024, 2:04:52 AM
    Author     : USER
--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script>
    // Function to refetch database and filter by search
    function refreshContent() {
        $.ajax({
            url: "CreateWorkingRota",
            method: "GET",
            success: function (response) {
                console.log(response);

                // Iterate over each day in the JSON object
                $.each(response, function (day, vets) {
                    var row = $("<tr>");
                    row.append($("<td>").text(day));

                    // Iterate over each vet for the current day
                    $.each(vets, function (_, vet) {
                        var cell = $("<td>");
                        cell.append($("<div>").text(vet.name));
                        cell.append($("<div>").text("Expertise: " + vet.expertiseAreas.join(", ")));
                        cell.append($("<div>").text("Timeslot: " + vet.timeslot));
                        row.append(cell);
                    });

                    // Append the row to the table body
                    $("#rotaTableBody").append(row);
                });
            },
            error: function (_, __, error) {
                alert("Unknown error: " + error);
            }
        });
    }


    $(document).ready(function () {
        refreshContent();
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
            <button onclick="location.href = 'CreateWorkingRota'">Generate New Timetable</button>
        </div>
        <hr/>

        <table border="1">
            <thead>
                <tr id="rotaTableHeader">
                    <th>Day</th>
                    <th>Vet 1</th>
                    <th>Vet 2</th>
                    <th>Vet 3</th>
                </tr>
            </thead>
            <tbody id="rotaTableBody"></tbody>
        </table>
    </body>
</html>
</body>
</html>
