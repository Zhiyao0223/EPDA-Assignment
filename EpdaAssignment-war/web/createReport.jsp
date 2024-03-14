<%--
    Document   : createReport
    Created on : Mar 14, 2024, 3:49:43 PM
    Author     : USER
--%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script>
    // Declare global var for chart
    var dataReport1 = {};
    var dataReport2 = {};
    var dataReport3 = {};
    var dataReport4 = {};
    var dataReport5 = {};

    function downloadChart(canvasId) {
        var canvas = document.getElementById(canvasId);

        // Create an image from the canvas
        var imageData = canvas.toDataURL('image/png');

        // Create a temporary link element
        var link = document.createElement('a');
        link.href = imageData;
        link.download = 'chart.png';

        // Trigger a click event on the link to start the download
        link.click();
    }

    function createVerticalBarChart(chartId, chartData) {
        console.log(chartId);
        console.log(chartData);

        const ctx = document.getElementById(chartId);
        return new Chart(ctx, {
            type: "bar",
            data: chartData,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    function createHorizontalBarChart(chartId, chartData) {
        const ctx = document.getElementById(chartId);
        return new Chart(ctx, {
            type: "bar",
            data: chartData,
            options: {
                indexAxis: 'y',
            }
        });
    }

    function createPieChart(chartId, chartData) {
        const ctx = document.getElementById(chartId);
        return new Chart(ctx, {
            type: "doughnut",
            data: chartData,
        });
    }

    function createLineChart(chartId, chartData) {
        const ctx = document.getElementById(chartId);
        return new Chart(ctx, {
            type: "line",
            data: chartData,
        });
    }

    // Basic function
    function getChartData(labels, axisLabel, data) {
        return {
            labels: labels,
            datasets: [{
                    label: axisLabel,
                    data: data,
                    borderWidth: 1
                }]
        };
    }

    function getLineChartData(labels, axisLabel, data) {
        return {
            labels: labels,
            datasets: [{
                    label: axisLabel,
                    data: data,
                    fill: false,
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                }]
        };
    }

    function initializeReport1(jsonResponse) {
        // Initialize arrays to store data labels and data
        let dataLabels = [];
        let data = [];

        // Iterate over the JSON object
        for (const [role, genderCounts] of Object.entries(jsonResponse)) {
            let count = 0;
            // Iterate over gender counts for this role
            for (const gender in genderCounts) {
                if (gender !== "") {
                    count += genderCounts[gender];
                }
            }

            // Add the role as a data label and the total count as data
            dataLabels.push(role);
            data.push(count);

            // Set into global var
            dataReport1 = getChartData(dataLabels, "No. of users", data);
        }
    }

    function initializeReport2And3(jsonResponse, label) {
        const monthNames = [
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ];

        let dataLabels = [];
        let data = [];

        // Iterate over the JSON object
        for (const [monthNum, value] of Object.entries(jsonResponse)) {
            // Subtract 1 to match array index
            const monthName = monthNames[parseInt(monthNum) - 1];
            // Add the month name as a data label and the corresponding value as data
            dataLabels.push(monthName);
            data.push(value);
        }
        if (label == "Total revenues") {
            dataReport2 = getLineChartData(dataLabels, label, data);

        } else {
            dataReport3 = getLineChartData(dataLabels, label, data);
        }
    }

    function initializeReport4(jsonResponse) {
        const dataLabel = Object.keys(jsonResponse);
        const data = Object.values(jsonResponse);
        dataReport4 = {
            labels: dataLabel,
            datasets: [{
                    label: "Age group among user",
                    data: data,
                    backgroundColor: [
                        'rgb(255, 99, 132)',
                        'rgb(54, 162, 235)',
                        'rgb(255, 205, 86)'
                    ],
                    hoverOffset: 4
                }]
        };
    }

    function initializeReport5(jsonResponse) {
        const dataLabel = Object.keys(jsonResponse);
        const data = Object.values(jsonResponse);
        dataReport5 = getChartData(dataLabel, "Total Expertises", data);
    }

    function loadContent() {
        $.ajax({
            url: "CreateReport",
            method: "GET",
            success: function (responses) {
                console.log(responses);

                initializeReport1(responses.report1);
                initializeReport2And3(responses.report2, "Total revenues");
                initializeReport2And3(responses.report3, "Total appointments");
                initializeReport4(responses.report4);
                initializeReport5(responses.report5);

                createVerticalBarChart('report1', dataReport1);
                createVerticalBarChart('report2', dataReport2);
                createVerticalBarChart('report3', dataReport3);
                createPieChart('report4', dataReport4);
                createHorizontalBarChart('report5', dataReport5);
            },
            error: function (xhr, status, error) {
                alert("Unknown error");
            }
        });
    }

    $(document).ready(function () {
        loadContent();
    })
</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report Dashboard</title>
    </head>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Report Dashboard</h1>
        <div>
            <!-- Description -->
            <p>This is the report dashboard. You can view and download reports here</p>
        </div>
        <hr/>


        <h3>1. Statistics of Genders among Users</h3>
        <button id="downloadButton1" onclick=downloadChart("report1")>Download Image</button>
        <div style="width:80%; margin: 0 auto;">
            <canvas id="report1" height="100"></canvas>
        </div>
        <br/><br/>

        <h3>2. Statistics of Revenue between Months</h3>
        <button id="downloadButton2" onclick=downloadChart("report2")>Download Image</button>
        <div style="width:80%; margin: 0 auto;">
            <canvas id="report2" height="100"></canvas>
        </div>
        <br/><br/>

        <h3>3. Statistics of Booked Appointment among Months</h3>
        <button id="downloadButton3" onclick=downloadChart("report3")>Download Image</button>
        <div style="width:80%; margin: 0 auto;">
            <canvas id="report3" height="100"></canvas>
        </div>
        <br/><br/>

        <h3>4. Statistics of Age Group among Users</h3>
        <button id="downloadButton4" onclick=downloadChart("report4")>Download Image</button>
        <div style="width:30%; margin: 0 auto;">
            <canvas id="report4" height="10"></canvas>
        </div>
        <br/><br/>

        <h3>5. Statistics of Expertise among Vets</h3>
        <button id="downloadButton5" onclick=downloadChart("report5")>Download Image</button>
        <div style="width:80%; margin: 0 auto;">
            <canvas id="report5" height="100"></canvas>
        </div>
        <br/><br/>

        <script>



        </script>
    </body>
</html>
