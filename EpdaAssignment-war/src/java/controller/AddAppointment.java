package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AnimalType;
import model.Appointment;
import model.AppointmentFacade;
import model.Expertise;
import model.ExpertiseFacade;
import model.Pet;
import model.PetFacade;
import model.Users;
import model.WorkingRota;
import model.WorkingRotaFacade;
import service.TableName;
import service.Util;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "AddAppointment", urlPatterns = {"/AddAppointment"})
public class AddAppointment extends HttpServlet {

    @EJB
    private ExpertiseFacade expertiseFacade;

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    @EJB
    private PetFacade petFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Get current suer
        Users currentUser = (Users) request.getSession().getAttribute("user");

        try (PrintWriter out = response.getWriter()) {
            try {
                boolean isSelectVet = !Validation.isEmpty(request.getParameter("vet"));

                // If select vet must select timeslot also
                if (isSelectVet && Validation.isEmpty(request.getParameter("timeslot"))) {
                    throw new Exception("-1");
                }

                WorkingRota tmpSchedule = new WorkingRota();
                Pet tmpPet = new Pet();
                Users tmpUser = new Users();

                Appointment newAppointment = new Appointment();
                newAppointment.setStatus(request.getParameter("appointmentStatus").equals("Scheduled") ? 2 : 1);
                newAppointment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                newAppointment.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

                tmpPet = petFacade.find(Long.parseLong(request.getParameter("petName")));
                newAppointment.setPetID(tmpPet);

                // Only add schedule if has select vet
                if (isSelectVet) {
                    tmpUser.setId(Long.parseLong(request.getParameter("vet")));
                    tmpSchedule = workingRotaFacade.find(Long.parseLong(request.getParameter("timeslot")));
                    newAppointment.setSchedule(tmpSchedule);
                }

                appointmentFacade.create(newAppointment);

                // Update working rota to booked status
                workingRotaFacade.updateByAttribute(TableName.WorkingRota.name(), Long.parseLong(request.getParameter("timeslot")), "status", 1);
                workingRotaFacade.refreshUpdatedDate(TableName.WorkingRota.name(), Long.parseLong(request.getParameter("timeslot")));

                response.sendRedirect("manageAppointment.jsp?addAppointmentSuccess=true");
            } catch (Exception e) {
                request.getRequestDispatcher("addAppointment.jsp").forward(request, response);
                out.println(getErrorMessage(e.getMessage()));
            }
        }
    }

    protected String getErrorMessage(String errorCode) {
        switch (errorCode) {
            case "-1":
                return "Please select a timeslot";
            default:
                return "Unknown Error.";
        }
    }

    protected void retrieveFormRequireData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Pet> petList = petFacade.findAll();
        List<WorkingRota> workingRotaList = workingRotaFacade.getAvaiableSlot();
        List<Expertise> expertiseList = expertiseFacade.findAll();

        // Used to store both list and return to JSP
        JsonObject combinedJson = new JsonObject();

        // Convert Pet list to JSON array
        JsonArray petJsonArray = new JsonArray();
        for (Pet tmpPet : petList) {
            JsonObject petJson = new JsonObject();
            petJson.addProperty("id", tmpPet.getPetID());
            petJson.addProperty("petName", tmpPet.getName());
            petJson.addProperty("ownerName", tmpPet.getCustID().getName());
            petJson.addProperty("petTypeId", tmpPet.getType().getId());
            petJson.addProperty("petType", tmpPet.getType().getDescription());
            petJsonArray.add(petJson);
        }
        combinedJson.add("pets", petJsonArray);

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
            expertiseJsonArray.add(expertiseJson);
        }
        combinedJson.add("expertise", expertiseJsonArray);

        // Set response type and write combined JSON to response
        response.setContentType("application/json");
        response.getWriter().write(combinedJson.toString());
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
        retrieveFormRequireData(request, response);
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
