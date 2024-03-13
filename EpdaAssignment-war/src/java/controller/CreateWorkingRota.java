package controller;

import com.google.gson.Gson;
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
import model.Vet;
import model.WorkingRotaFacade;
import service.Util;

/**
 *
 * @author USER
 */
@WebServlet(name = "CreateWorkingRota", urlPatterns = {"/CreateWorkingRota"})
public class CreateWorkingRota extends HttpServlet {

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

        }
    }

    protected void generateTimetable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the new Timestamp
        Timestamp tmpDate = Util.addDaysToDate(workingRotaFacade.getLatestRowTimestamp(), 1);

        Map<String, List<Vet>> workingRota = workingRotaFacade.generateTimetable();

        for (Map.Entry<String, List<Vet>> entry : workingRota.entrySet()) {
            String day = entry.getKey();
            List<Vet> vetsForDay = entry.getValue();
//
//            System.out.println(day + ":");
            for (Vet vet : vetsForDay) {
                vet.setTimeslot(randomGenerateAppointmentTime(tmpDate));
//                System.out.println("Vet: " + vet.getName());
//                System.out.println("Expertise Areas: " + String.join(", ", vet.getExpertiseAreas()));
//                System.out.println();
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
        generateTimetable(request, response);
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
