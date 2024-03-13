<%--
    Document   : addAppointment
    Created on : Mar 13, 2024, 5:50:53 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    function onPetFieldChange() {
        var selectedPetId = $("#petName").val();

        // If a pet type is selected
        if (selectedPetId) {
            // Get selected pet type
            var selectedAnimalTypeId = '';
            dbResponses.pets.forEach(function (pet) {
                if (selectedPetId == pet.id) {
                    selectedAnimalTypeId = pet.petTypeId;
                }
            });

            // Pet not found
            if (selectedAnimalTypeId == '') {
                alert("Pet not found.");
                return;
            }

            // Initialize DOM element and append into DOM
            var vetSelect = $("#vet");
            var optionAppended = false;

            // Clear the existing options in the vet select element
            $("#vet").empty();
            vetSelect.append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));
            $.each(dbResponses.expertise, function (index, type) {
                // Only append if suit expertises
                if (type.animalTypeId == selectedAnimalTypeId) {
                    vetSelect.append($("<option>").val(type.staffId).text(type.staffName));
                    optionAppended = true;
                }
            });

            // Check if has option
            if (!optionAppended) {
                $("#vet").empty();
                vetSelect.append($("<option>").val("").text("No vet available").prop("selected", true).prop("disabled", true));
            }
        }
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
        // Update status based on timeslot if selected
        $("#timeslot").val() ? $("#appointmentStatus").val("Scheduled") : $("#appointmentStatus").val("Pending vet");
    }

    // Initialize global var
    var dbResponses = {};

    $(document).ready(function () {
        // Fetch all data from servlet once load page
        $.ajax({
            url: "AddAppointment",
            method: "GET",
            dataType: "json",
            success: function (response) {
                console.log(response);
                dbResponses = response;

                // Initialize DOM element and append into DOM
                var petSelect = $("#petName");
                petSelect.append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));
                $.each(response.pets, function (index, type) {
                    petSelect.append($("<option>").val(type.id).text(type.petName + " ( " + type.ownerName + " )"));
                });

                // Append default value first
                $("#vet").append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));
                $("#timeslot").append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));

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
        <title>Add Appointment</title>
    </head>
    <body>
        <a href="manageAppointment.jsp">< Back</a>
        <h1>Add Appointment</h1>

        <form action="AddAppointment" method="POST" id="appointmentForm">
            <p>Appointment: </p>
            <label for="petLabel">Pet: </label>
            <select id="petName" name="petName" onchange=onPetFieldChange() required></select>
            <br/><br/>

            <label for="vetLabel">Vet: </label>
            <select id="vet" name="vet" onchange=onVetChange()></select>
            <br/><br/>

            <label for="timeslotLabel">Time Slot: </label>
            <select id="timeslot" name="timeslot" onchange=onTimeSlotChange()></select>
            <br/><br/>

            <label for="appointmentStatusLabel">Status: </label>
            <input type='text' id="appointmentStatus" name="appointmentStatus" value='Pending vet' readonly>
            <br/><br/>

            <input type="submit" value="Create">
        </form>
    </body>
</html>
