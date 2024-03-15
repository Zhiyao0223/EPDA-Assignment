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
import model.Log;
import model.LogAction;
import model.LogFacade;
import model.MedicalReport;
import model.MedicalReportFacade;
import model.Users;
import service.TableName;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "ViewMedicalReport", urlPatterns = {"/ViewMedicalReport"})
public class ViewMedicalReport extends HttpServlet {

    @EJB
    private LogFacade logFacade;

    @EJB
    private MedicalReportFacade medicalReportFacade;

    // Process add prognosis update
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Set constant variable
        final int maxPrognosisChar = 250;

        // Get POST parameters
        String inputPrognosis = request.getParameter("prognosis");
        Long reportId = Long.parseLong(request.getParameter("reportId"));

        // Get current logined user
        Users currentUser = (Users) request.getSession().getAttribute("user");

        try (PrintWriter out = response.getWriter()) {
            try {
                if (Validation.isEmpty(inputPrognosis)) {
                    throw new Exception("-1");
                } else if (Validation.isExceedMaxCharacter(inputPrognosis, maxPrognosisChar)) {
                    throw new Exception("-2");
                }

                // Execute update on db
                medicalReportFacade.addPrognosis(inputPrognosis, reportId, TableName.MedicalReport.name());
                logFacade.create(new Log(currentUser, "Add prognosis to medical report ID - " + request.getParameter("reportId"), LogAction.CREATE));

                response.sendRedirect("viewMedicalReport.jsp?addPrognosisSuccess=true");
            } catch (Exception e) {
                request.getRequestDispatcher("viewMedicalReport.jsp").forward(request, response);
                out.println(getErrorMessage(e.getMessage()));
            }
        }
    }

    protected String getErrorMessage(String errCode) {
        switch (errCode) {
            case "-1":
                return "Input cannot be empty.";
            case "-2":
                return "Cannot exceed 250 characters";
            default:
                return "Unknown error.";
        }
    }

    protected void retrieveMedicalReportData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Users currentUser = (Users) request.getSession().getAttribute("user");
        List<MedicalReport> reports = medicalReportFacade.getMedicalReport(currentUser.getId());

        // Only sort if have var
        if (reports.size() > 1) {
            reports = medicalReportFacade.sortByStatus(reports);
        }

        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON to response
        response.getWriter().write(new Gson().toJson(reports));
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
        retrieveMedicalReportData(request, response);
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
