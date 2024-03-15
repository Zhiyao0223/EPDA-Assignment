package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import static controller.EditProfile.getErrorMessage;
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
import model.Appointment;
import model.AppointmentFacade;
import model.Expertise;
import model.ExpertiseFacade;
import model.Log;
import model.LogAction;
import model.LogFacade;
import model.MedicalReport;
import model.MedicalReportFacade;
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
@WebServlet(name = "EditAppointment", urlPatterns = {"/EditAppointment"})
public class EditAppointment extends HttpServlet {

    @EJB
    private LogFacade logFacade;

    @EJB
    private MedicalReportFacade medicalReportFacade;

    @EJB
    private AppointmentFacade appointmentFacade;

    @EJB
    private ExpertiseFacade expertiseFacade;

    @EJB
    private PetFacade petFacade;

    @EJB
    private WorkingRotaFacade workingRotaFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Check for any changes
        Boolean hasChange = false;

        // Get existing data and refetch database
        Users currentUser = (Users) request.getSession().getAttribute("user");
        System.out.println(request.getParameter("editId"));
        Appointment dbAppointment = appointmentFacade.find(Long.parseLong(request.getParameter("editId")));

        try (PrintWriter out = response.getWriter()) {
            try {
                // Check for any changes
                String[][] appendedData = {
                    {"schedule", ""},
                    {"status", ""}
                };

                // Compare changes
                if (!request.getParameter("timeslot").equals(String.valueOf(dbAppointment.getSchedule().getId()))
                        && !Validation.isEmpty(request.getParameter("timeslot"))) {
                    appendedData[0][1] = request.getParameter("timeslot");

                    System.out.println("rota ID: " + appendedData[0][1]);
                    hasChange = true;
                }
                if (!request.getParameter("appointmentStatus").equals(String.valueOf(dbAppointment.getStatus()))) {
                    appendedData[1][1] = request.getParameter("appointmentStatus");

                    System.out.println("appointmentStatus: " + appendedData[1][1]);
                    hasChange = true;
                }

                // No changes return, prevent access db
                if (!hasChange) {
                    throw new Exception("-2");
                }

                // Check if need update diagnosis
                if (appendedData[1][1].equals("0")) {
                    MedicalReport tmpReport = new MedicalReport();

                    tmpReport.setDiagnosisDetail(request.getParameter("diagnosis"));
                    tmpReport.setTotalFee(Double.parseDouble(request.getParameter("totalFee")));
                    tmpReport.setPrognosisDetail(null);
                    tmpReport.setStatus(1);
                    tmpReport.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    tmpReport.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                    tmpReport.setAppointment(dbAppointment);

                    medicalReportFacade.create(tmpReport);
                    logFacade.create(new Log(currentUser, "Create Medical Report", LogAction.CREATE));
                }

                // Loop through all changes and update one by one
                for (String[] changes : appendedData) {
                    // Proceed if no changes
                    if (Validation.isEmpty(changes[1])) {
                        continue;
                    }

                    System.out.println("Changes: " + changes[0] + " " + changes[1]);

                    // Update db, start by reseting working rota status to available for previous
                    appointmentFacade.updateByAttribute(TableName.WorkingRota.name(), dbAppointment.getSchedule().getId(), "status", 0);

                    if (changes[0] == "schedule") {
                        // Get specific working rota class
                        WorkingRota tmp = workingRotaFacade.find(Long.parseLong(changes[1]));
                        appointmentFacade.updateByAttribute(TableName.Appointment.name(), dbAppointment.getId(), "schedule", tmp);
                    } else {
                        appointmentFacade.updateByAttribute(TableName.Appointment.name(), dbAppointment.getId(), "status", Integer.parseInt(changes[1]));
                    }
                }
                logFacade.create(new Log(currentUser, "Modify appointment ID - " + dbAppointment.getId(), LogAction.UPDATE));

                response.sendRedirect("manageAppointment.jsp?editSuccess=true");
            } catch (Exception e) {
                response.sendRedirect("editAppointment.jsp?editId=" + dbAppointment.getId() + "&errCode=" + e.getMessage());
            }
        }
    }

    protected void retrieveFormRequireData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Appointment editAppointment = appointmentFacade.find(Long.parseLong(request.getParameter("id")));
        List<Expertise> expertiseList = expertiseFacade.findAll();

        List<WorkingRota> workingRotaList = workingRotaFacade.getAvailableSlot();
        workingRotaList.add(editAppointment.getSchedule());

        // Used to store both list and return to JSP
        JsonObject combinedJson = new JsonObject();

        // Convert appointment to JSON array
        JsonObject appointmentJson = new JsonObject();
        appointmentJson.addProperty("appointmentStatus", editAppointment.getStatus());
        appointmentJson.addProperty("scheduleId", editAppointment.getSchedule().getId());
        appointmentJson.addProperty("vetId", editAppointment.getSchedule().getStaffId().getId());
        appointmentJson.addProperty("petId", editAppointment.getPetID().getPetID());
        appointmentJson.addProperty("petName", editAppointment.getPetID().getName());
        appointmentJson.addProperty("ownerName", editAppointment.getPetID().getCustID().getName());
        appointmentJson.addProperty("petTypeId", editAppointment.getPetID().getType().getId());
        appointmentJson.addProperty("petType", editAppointment.getPetID().getType().getDescription());

        combinedJson.add("appointment", appointmentJson);

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
