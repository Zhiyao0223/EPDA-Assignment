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
import model.Appointment;
import model.AppointmentFacade;
import model.Users;
import service.TableName;

/**
 *
 * @author USER
 */
@WebServlet(name = "ManageAppointment", urlPatterns = {"/ManageAppointment"})
public class ManageAppointment extends HttpServlet {

    @EJB
    private AppointmentFacade appointmentFacade;

    protected void deleteAppointment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type to JSON
        response.setContentType("application/json");

        // Initialize var
        String[] err = {"0"};

        // Get POST params
        String appointmentId = request.getParameter("id");

        // Get current logined user and deleted user
        Users currentUser = (Users) request.getSession().getAttribute("user");
        Appointment cancelAppointment = appointmentFacade.find(Long.parseLong(appointmentId));

        try {
            if (cancelAppointment.getStatus() == 0) {
                throw new Exception("-1");
            }// Check if cancel completed  appointment
            else if (cancelAppointment.getStatus() == 3) {
                throw new Exception("-2");
            } // Check if cancel cancelled appointment

            // Change status to cancel in db
            appointmentFacade.updateByAttribute(TableName.Appointment.name(), cancelAppointment.getId(), "status", 3);
            appointmentFacade.refreshUpdatedDate(TableName.Appointment.name(), cancelAppointment.getId());
        } catch (Exception e) {
            // Write JSON to response
            err[1] = e.getMessage();
        }

        response.getWriter().write(new Gson().toJson(err));
    }

    protected void retrieveAllAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Appointment> appointmentList = appointmentFacade.findAll();
        appointmentList = appointmentFacade.sortByStatus(appointmentList);

        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON to response
        response.getWriter().write(new Gson().toJson(appointmentList));
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
        retrieveAllAppointment(request, response);
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
        deleteAppointment(request, response);
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
