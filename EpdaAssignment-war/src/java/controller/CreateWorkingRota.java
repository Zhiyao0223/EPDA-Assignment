package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Expertise;
import model.ExpertiseFacade;
import model.Users;
import model.UsersFacade;
import model.Vet;
import model.WorkingRota;
import model.WorkingRotaFacade;
import service.Util;

/**
 *
 * @author USER
 */
@WebServlet(name = "CreateWorkingRota", urlPatterns = {"/CreateWorkingRota"})
public class CreateWorkingRota extends HttpServlet {

    @EJB
    private ExpertiseFacade expertiseFacade;

    @EJB
    private UsersFacade usersFacade;

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    protected void getAllTimetable(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Used to store both list and return to JSP
        JsonObject combinedJson = new JsonObject();

        List<WorkingRota> workingRotaList = workingRotaFacade.getActiveRota();
        List<Expertise> expertiseList = expertiseFacade.findAll();

        // Convert Working Rota list to JSON array
        JsonArray scheduleJsonArray = new JsonArray();
        for (WorkingRota singleSchedule : workingRotaList) {
            JsonObject scheduleJson = new JsonObject();
            scheduleJson.addProperty("id", singleSchedule.getId());
            scheduleJson.addProperty("staffId", singleSchedule.getStaffId().getId());
            scheduleJson.addProperty("staffName", singleSchedule.getStaffId().getName());
            scheduleJson.addProperty("timeslots", Util.timestampToDateTimeString(singleSchedule.getTimeslot()));
            scheduleJsonArray.add(scheduleJson);
        }
        combinedJson.add("schedule", scheduleJsonArray);

        // Convert expertise list to JSON array
        JsonArray expertiseJsonArray = new JsonArray();
        for (Expertise expertise : expertiseList) {
            JsonObject expertiseJson = new JsonObject();
            expertiseJson.addProperty("staffId", expertise.getVetID().getId());
            expertiseJson.addProperty("staffName", expertise.getVetID().getName());
            expertiseJson.addProperty("animalTypeId", expertise.getAnimalType().getId());
            expertiseJson.addProperty("animalTypeName", expertise.getAnimalType().getDescription());
            expertiseJsonArray.add(expertiseJson);
        }
        combinedJson.add("expertise", expertiseJsonArray);

        // Set response content type to JSON
        response.setContentType("application/json");
        response.getWriter().write(combinedJson.toString());
    }

    protected void generateTimetable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the new Timestamp
        Timestamp tmpDate = Util.addDaysToDate(workingRotaFacade.getLatestRowTimestamp(), 1);

        Map<String, List<Vet>> workingRota = workingRotaFacade.generateTimetable();

        for (Map.Entry<String, List<Vet>> entry : workingRota.entrySet()) {
            String day = entry.getKey();
            List<Vet> vetsForDay = entry.getValue();

            for (Vet vet : vetsForDay) {
                // Generate two timeslot
                System.out.println("Date: " + tmpDate);
                vet.setTimeslot(randomGenerateAppointmentTime(tmpDate), 0);

                // Loop to continous generate timeslot in case
                Timestamp tmp;
                while (true) {
                    tmp = randomGenerateAppointmentTime(tmpDate);

                    if (!tmp.equals(vet.getTimeslots()[0])) {
                        System.out.println(tmp + "," + vet.getTimeslots()[0]);
                        break;
                    }
                }
                vet.setTimeslot(tmp, 1);

                // Save into db
                Users tmpVet = usersFacade.find(vet.getVetId());
                workingRotaFacade.create(new WorkingRota(tmpVet, vet.getTimeslots()[0]));
                workingRotaFacade.create(new WorkingRota(tmpVet, vet.getTimeslots()[1]));
            }
            // Add one day to tmpDate
            tmpDate = Util.addDaysToDate(tmpDate, 1);
        }
        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON to response
        response.getWriter().write(new Gson().toJson(workingRota));
    }

    protected Timestamp randomGenerateAppointmentTime(Timestamp t) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t.getTime());

        // Set the minimum hour and minute for the appointment (10 am)
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Calculate the difference in milliseconds between 10 am and 5 pm
        long minTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        long maxTime = calendar.getTimeInMillis();
        long range = maxTime - minTime;

        // Generate a random time within the range, rounded to the nearest hour
        Random random = new Random();
        long randomOffset = (long) (range * random.nextDouble());

        // Round to the nearest hour
        // 1800000 milliseconds = 30 minutes, 3600000 milliseconds = 1 hour
        long roundedOffset = (randomOffset + 1800000) / 3600000 * 3600000;

        // Set the random appointment time
        Timestamp randomAppointmentTime = new Timestamp(minTime + roundedOffset);
        return randomAppointmentTime;
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
        getAllTimetable(request, response);
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
        generateTimetable(request, response);
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
