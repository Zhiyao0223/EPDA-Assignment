package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Appointment;
import model.AppointmentFacade;
import model.Expertise;
import model.ExpertiseFacade;
import model.MedicalReport;
import model.MedicalReportFacade;
import model.Users;
import model.UsersFacade;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "CreateReport", urlPatterns = {"/CreateReport"})
public class CreateReport extends HttpServlet {

    @EJB
    private ExpertiseFacade expertiseFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private MedicalReportFacade medicalReportFacade;

    @EJB
    private UsersFacade usersFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

        }
    }

    protected void getGraphData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Used to store both list and return to JSP
        JsonObject combinedJson = new JsonObject();

        // 1. Statistics of Genders among Users
        combinedJson.add("report1", getUsersByRoleAndGenderCount());

        // 2. Statistics of Revenue between Months
        combinedJson.add("report2", getRevenueBetweenMonths());

        // 3. Statistics of Booked Appointment among Months
        combinedJson.add("report3", getBookedAppointmentBetweenMonths());

        // 4. Statistics of Age Group among Users
        combinedJson.add("report4", getAgeGroupAmongUsers());

        // 5. Statistics of Expertise among Vets
        combinedJson.add("report5", getExpertiseAmongVet());

        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON to response
        response.getWriter().write(combinedJson.toString());
    }

    protected JsonObject getUsersByRoleAndGenderCount() {
        JsonObject combinedJson = new JsonObject();
        List<Users> userList = usersFacade.findAllLegitUsers();

        for (Users user : userList) {
            String role = user.getRole().getDescription();
            String gender = user.getGender();

            // If the role is not present in the JsonObject, add it
            if (!combinedJson.has(role)) {
                combinedJson.add(role, new JsonObject());
            }

            // Get the JsonObject of gender counts for the role
            JsonObject genderCountJson = combinedJson.getAsJsonObject(role);

            // Increment the count for the gender in the JsonObject
            if (genderCountJson.has(gender)) {
                genderCountJson.addProperty(gender, genderCountJson.get(gender).getAsInt() + 1);
            } else {
                genderCountJson.addProperty(gender, 1);
            }
        }
        return combinedJson;
    }

    protected JsonObject getExpertiseAmongVet() {
        JsonObject combinedJson = new JsonObject();
        List<Expertise> expertiseList = expertiseFacade.findAll();

        for (Expertise expertise : expertiseList) {
            String expertiseName = expertise.getAnimalType().getDescription();

            // Increment the count for the expertise name in the JsonObject
            if (combinedJson.has(expertiseName)) {
                combinedJson.addProperty(expertiseName, combinedJson.get(expertiseName).getAsInt() + 1);
            } else {
                combinedJson.addProperty(expertiseName, 1);
            }
        }
        return combinedJson;
    }

    protected JsonObject getRevenueBetweenMonths() {
        JsonObject combinedJson = new JsonObject();
        List<MedicalReport> reportList = medicalReportFacade.findAll();

        // Initialize a map to store revenue for each month with an initial value of 0
        Map<Integer, Double> monthRevenueMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            monthRevenueMap.put(month, 0.0);
        }

        // Iterate over each appointment
        for (MedicalReport report : reportList) {
            // Extract the month from the created_date timestamp
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(report.getCreatedDate());
            int month = calendar.get(Calendar.MONTH) + 1;

            // Increment the revenue for the corresponding month
            double currentRevenue = monthRevenueMap.get(month);
            double totalFee = report.getTotalFee();
            monthRevenueMap.put(month, currentRevenue + totalFee);
        }

        // Convert the map to JsonObject
        for (Map.Entry<Integer, Double> entry : monthRevenueMap.entrySet()) {
            combinedJson.addProperty(String.valueOf(entry.getKey()), entry.getValue());
        }
        return combinedJson;
    }

    protected JsonObject getBookedAppointmentBetweenMonths() {
        JsonObject combinedJson = new JsonObject();
        List<Appointment> appointmentList = appointmentFacade.findAll();

        // Initialize a map to store revenue for each month with an initial value of 0
        Map<Integer, Integer> monthAppointmentMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            monthAppointmentMap.put(month, 0);
        }

        // Iterate over each appointment
        for (Appointment appointment : appointmentList) {
            // Extract the month from the created_date timestamp
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(appointment.getCreatedDate());
            int month = calendar.get(Calendar.MONTH) + 1;

            // Increment the appointment count for the corresponding month
            int currentCount = monthAppointmentMap.get(month);
            monthAppointmentMap.put(month, currentCount + 1);
        }

        // Convert the map to JsonObject
        for (Map.Entry<Integer, Integer> entry : monthAppointmentMap.entrySet()) {
            combinedJson.addProperty(String.valueOf(entry.getKey()), entry.getValue());
        }
        return combinedJson;
    }

    protected JsonObject getAgeGroupAmongUsers() {
        JsonObject combinedJson = new JsonObject();
        List<Users> userList = usersFacade.findAll();

        // Initialize a map to store user count for each age group with an initial value of 0
        Map<String, Integer> userAgeGroupMap = new HashMap<>();
        userAgeGroupMap.put("Under 18", 0);
        userAgeGroupMap.put("18-25", 0);
        userAgeGroupMap.put("26-35", 0);
        userAgeGroupMap.put("36-45", 0);
        userAgeGroupMap.put("46-55", 0);
        userAgeGroupMap.put("56-65", 0);
        userAgeGroupMap.put("Over 65", 0);
        userAgeGroupMap.put("Unknown", 0);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Iterate over each user
        for (Users user : userList) {
            // Continue if no date of birth
            if (Validation.isEmpty(user.getDateOfBirth())) {
                userAgeGroupMap.put("Unknown", userAgeGroupMap.get("Unknown") + 1);
                continue;
            }

            // Parse the date of birth string into a LocalDate object
            LocalDate dob = LocalDate.parse(user.getDateOfBirth());

            // Calculate the age of the user
            int age = Period.between(dob, currentDate).getYears();

            // Determine the age group and increment the count
            String ageGroup = getAgeGroup(age);
            userAgeGroupMap.put(ageGroup, userAgeGroupMap.get(ageGroup) + 1);
        }

        // Convert the map to JsonObject
        for (Map.Entry<String, Integer> entry : userAgeGroupMap.entrySet()) {
            combinedJson.addProperty(entry.getKey(), entry.getValue());
        }

        return combinedJson;
    }

    // Helper method to determine the age group based on age
    private String getAgeGroup(int age) {
        if (age < 18) {
            return "Under 18";
        } else if (age >= 18 && age <= 25) {
            return "18-25";
        } else if (age >= 26 && age <= 35) {
            return "26-35";
        } else if (age >= 36 && age <= 45) {
            return "36-45";
        } else if (age >= 46 && age <= 55) {
            return "46-55";
        } else if (age >= 56 && age <= 65) {
            return "56-65";
        } else {
            return "Over 65";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        getGraphData(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
