package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AnimalTypeFacade;
import model.Appointment;
import model.AppointmentFacade;
import model.MedicalReportFacade;
import model.PetFacade;
import model.Users;
import model.UsersFacade;

/**
 *
 * @author USER
 */
@WebServlet(name = "ViewAppointment", urlPatterns = {"/ViewAppointment"})
public class ViewAppointment extends HttpServlet {

    @EJB
    private MedicalReportFacade medicalReportFacade;

    @EJB
    private AnimalTypeFacade animalTypeFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private PetFacade petFacade;

    @EJB
    private UsersFacade usersFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

        }
    }

    protected void retrievedStaffData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Users currentUser = (Users) request.getSession().getAttribute("user");
        List<Appointment> appointments = appointmentFacade.getPersonalAppointmentList(currentUser.getId());

        // Only sort if have var
        if (appointments.size() > 1) {
            appointments = appointmentFacade.sortByLatestTimeslot(appointments);
        }

        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON to response
        response.getWriter().write(new Gson().toJson(appointments));
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
        retrievedStaffData(request, response);
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
