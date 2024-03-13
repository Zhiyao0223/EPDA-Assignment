<%--
    Document   : editAppointment
    Created on : Mar 13, 2024, 9:44:01 PM
    Author     : USER
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    // Get url params
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has("errCode")) {
        var errorCode = urlParams.get("errCode");
        var errorMessage = "";
        switch (errorCode) {
            case "-1":
                errorMessage = "Please select a timeslot";
                break;
            case "-2":
                errorMessage = "No changes";
                break;
            default:
                errorMessage = "Unknown Error.";
        }
        alert(errorMessage);
    }


    function onVetChange() {
        var selectedVetId = $("#vet").val();

        if (selectedVetId) {
            console.log(dbResponses.schedule);

            // Initialize DOM element and append into DOM
            var timeslotSelect = $("#timeslot");
            var optionAppended = false;

            // Clear the existing options in the timeslot select element
            $("#timeslot").empty();
            timeslotSelect.append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));
            $.each(dbResponses.schedule, function (index, singleSchedule) {
                if (singleSchedule.staffId == selectedVetId) {
                    timeslotSelect.append($("<option>").val(singleSchedule.id).text(singleSchedule.timeslots));
                    optionAppended = true;
                }
            });

            // Check if has option
            if (!optionAppended) {
                $("#timeslot").empty();
                timeslotSelect.append($("<option>").val("").text("No timeslot available").prop("selected", true).prop("disabled", true));
            }
        }
    }

    function onTimeSlotChange() {
        // Pending vet
        if (response.appointment.appointmentStatus == 1 && $("#timeslot").val()) {
            $("#appointmentStatus").empty();
            statusSelect.append($("<option>").val(2).text("Scheduled").prop("selected", true));
            statusSelect.append($("<option>").val(0).text("Completed"));
        }
    }

    function onStatusChange() {
        var diagnosisTextarea = document.getElementById("diagnosis");
        var totalFeeInput = document.getElementById("totalFee");

        // Show diagnosis field if complete appointment or vice versa
        if ($("#appointmentStatus").val() == 0) {
            $("#diagnosisField").show();
            diagnosisTextarea.setAttribute("required", "true");
            totalFeeInput.setAttribute("required", "true");
        } else {
            $("#diagnosisField").hide();
            diagnosisTextarea.removeAttribute("required");
            totalFeeInput.removeAttribute("required");
        }
    }

    // Initialize global var
    var dbResponses = {};

    $(document).ready(function () {
        // Set value into editId textfield
        $("#editId").val(urlParams.get("editId"));

        // Fetch all data from servlet once load page
        $.ajax({
            url: "EditAppointment",
            method: "GET",
            data: {id: urlParams.get('editId')},
            dataType: "json",
            success: function (response) {
                console.log(response);
                dbResponses = response;

                // Initialize DOM element and append into DOM
                $("#petName").append($("<option>")
                        .val(response.appointment.petId)
                        .text(response.appointment.petName + " ( " + response.appointment.ownerName + " )")
                        .prop("selected", true)
                        .prop("disabled", true));

                // Clear the existing options in the vet select element
                if (response.appointment.vetId) {
                    var vetSelect = $("#vet");
                    vetSelect.empty();
                    $.each(dbResponses.expertise, function (index, type) {
                        // Only append if suit expertises
                        if (type.animalTypeId == response.appointment.petTypeId) {
                            // Only select option if same with record
                            if (type.staffId == response.appointment.vetId) {
                                vetSelect.append($("<option>").val(type.staffId).text(type.staffName).prop("selected", true));
                            } else {
                                vetSelect.append($("<option>").val(type.staffId).text(type.staffName));
                            }
                            optionAppended = true;
                        }
                    });
                }

                // Clear the existing options in the timeslot select element
                if (response.appointment.scheduleId) {
                    var timeslotSelect = $("#timeslot");
                    timeslotSelect.empty();
                    $.each(dbResponses.schedule, function (index, singleSchedule) {
                        if (singleSchedule.staffId == response.appointment.vetId) {
                            console.log(singleSchedule.id);

                            if (response.appointment.scheduleId == singleSchedule.id) {
                                timeslotSelect.append($("<option>").val(singleSchedule.id).text(singleSchedule.timeslots).prop("selected", true));
                            } else {
                                timeslotSelect.append($("<option>").val(singleSchedule.id).text(singleSchedule.timeslots));
                            }
                            optionAppended = true;
                        }
                    });
                }


                // Add status
                var statusSelect = $("#appointmentStatus");
                var statusText = response.appointment.appointmentStatus == 1 ? "Pending vet" : "Scheduled";
                statusSelect.append($("<option>").val(response.appointment.appointmentStatus).text(statusText).prop("selected", true));
                statusSelect.append($("<option>").val(0).text("Completed"));
            },
            error: function (_, __, error) {
                console.error("Error fetching data:", error);
            }
        });
    });
</script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Appointment</title>
    </head>
    <body>
        <a href="manageAppointment.jsp">< Back</a>
        <h1>Edit Appointment</h1>

        <form action="EditAppointment" method="post" id="appointmentForm">
            <p>Appointment: </p>
            <label for="petLabel">Pet: </label>
            <select id="petName" name="petName" readonly></select>
            <br/><br/>

            <label for="vetLabel">Vet: </label>
            <select id="vet" name="vet" onchange=onVetChange()></select>
            <br/><br/>

            <label for="timeslotLabel">Time Slot: </label>
            <select id="timeslot" name="timeslot" onchange=onTimeSlotChange()></select>
            <br/><br/>

            <label for="statusLabel">Status: </label>
            <select id="appointmentStatus" name="appointmentStatus" onchange=onStatusChange()></select>
            <br/><br/>

            <!--Only show if status changed to complete-->
            <div id="diagnosisField" style="display: none;">
                <label for="diagnosisLabel">Diagnosis: </label><br/>
                <textarea rows="4" cols="50" maxlength="250" placeholder="Enter pet's diagnosis here..." name="diagnosis" id="diagnosis"></textarea>
                <br/><br/>

                <label for="totalFeeLabel">Total Fee: </label><br/>
                <input type="number" id="totalFee" name="totalFee" min="1" >
                <br/><br/>
            </div>

            <input type="text" id="editId" name="editId" hidden>
            <input type="submit" value="Save">
        </form>
    </body>
</html>
