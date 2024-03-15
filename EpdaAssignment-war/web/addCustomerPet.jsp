<%--
    Document   : addCustomerAndPetProfile
    Created on : Mar 12, 2024, 5:23:10 PM
    Author     : USER
--%>

<%@page import="model.Users"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="checkSession.jsp"%>

<%
    // Get the gender value from the session scope
    Users currentUser = (Users) session.getAttribute("user");

    // Define gender options
    String[][] genderOptions = {
        {"", "Please Select"},
        {"m", "Male"},
        {"f", "Female"}
    };
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Customer & Pet Profile</title>
    </head>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has("err")) {
            checkException(urlParams.get("err"));
        }

        function checkException(errCode) {
            switch (errCode) {
                case "-1":
                    alert("Invalid date of birth");
                    break;
                case "-2":
                    alert("Please fill all required field");
                    break;
                case "-3":
                    alert("Invalid phone number format.");
                    break;
                case "-4":
                    alert("Email is being registered before");
                    break;
                default:
                    alert("Unknown Error.");
            }
        }

        // Toggle between customer and pet profile forms
        function toggleForm(formType) {
            var customerForm = document.getElementById("customer-form");
            var petForm = document.getElementById("pet-form");
            if (formType === "customer") {
                customerForm.style.display = "block";
                petForm.style.display = "none";
            } else if (formType === "pet") {
                customerForm.style.display = "none";
                petForm.style.display = "block";
            }
        }

        $(document).ready(function () {
            // Fetch all data from servlet
            $.ajax({
                url: "AddCustomerPet",
                method: "GET",
                dataType: "json",
                success: function (response) {
                    console.log(response);
                    // Initialize DOM element and append into DOM
                    var animalTypeSelect = $("#animalType");
                    animalTypeSelect.append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));
                    $.each(response.animalTypes, function (index, type) {
                        animalTypeSelect.append($("<option>").val(type.id).text(type.name));
                    });
                    var custList = $("#userId");
                    custList.append($("<option>").val("").text("Please Select").prop("selected", true).prop("disabled", true));
                    $.each(response.customers, function (index, type) {
                        custList.append($("<option>").val(type.id).text(type.name));
                    });
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching data:", error);
                }
            });
        });
    </script>
    <body>
        <a href="index.jsp">< Back</a>
        <h1>Add Customer & Pet Profile</h1>

        <button onclick="toggleForm('customer')">Customer Profile</button>
        <button onclick="toggleForm('pet')">Pet Profile</button>
        <br/><br/>

        <!-- Customer Profile Form -->
        <form action="AddCustomerPet" method="POST" id="customer-form">
            <p>Customer: </p>
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" required>
            <br/><br/>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
            <br/><br/>

            <label for="dob">Date of Birth:</label>
            <input type="date" id="dob" name="dob" required>
            <br/><br/>

            <label for="gender">Gender:</label>
            <select id="gender" name="gender" required>
                <% for (String[] option : genderOptions) {%>
                <option value="<%= option[0]%>"
                        <%= option[0].equals("") ? "selected disabled" : ""%>><%= option[1]%>
                </option>
                <% }%>
            </select>
            <br/><br/>

            <label for="phone" >Phone Number:</label>
            <input type="tel" id="phone" name="phone" required>
            <br/><br/>

            <input type="text" name="formType" value="customer" hidden>
            <input type="submit" value="Create">
        </form>

        <!-- Pet Profile Form -->
        <form action="AddCustomerPet" method="POST" id="pet-form" style="display:none;">
            <p>Pet: </p>
            <label for="petName">Pet Name:</label>
            <input type="text" id="petName" name="petName" required>
            <br/><br/>

            <label for="petGender">Gender:</label>
            <select id="petGender" name="petGender" required>
                <% for (String[] option : genderOptions) {%>
                <option value="<%= option[0]%>"
                        <%= option[0].equals("") ? "selected disabled" : ""%>><%= option[1]%>
                </option>
                <% }%>
            </select>
            <br/><br/>

            <label for="userId">Customer: </label>
            <select id="userId" name="userId" required></select>
            <br/><br/>

            <label for="animalType">Type: </label>
            <select id="animalType" name="animalType" required></select>
            <br/><br/>

            <input type="text" name="formType" value="pet" hidden>
            <input type="submit" value="Create">
        </form>
    </div>
</body>
</html>
