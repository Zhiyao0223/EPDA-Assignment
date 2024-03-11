package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MedicalReport;
import model.MedicalReportFacade;
import service.TableName;

/**
 *
 * @author USER
 */
@WebServlet(name = "diagnosis", urlPatterns = {"/diagnosis"})
public class Diagnosis extends HttpServlet {

    @EJB
    private MedicalReportFacade medicalReportFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet diagnosis</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet diagnosis at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void retrieveColumnName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] columnNames = medicalReportFacade.getColumnNames(TableName.MedicalReport.name());
            List<MedicalReport> allReports = medicalReportFacade.findAll();

            // Get ServletContext
            ServletContext servletContext = request.getServletContext();

            // Set DataCache in application scope
            servletContext.setAttribute("cachedMedicalReportHeader", columnNames);
            servletContext.setAttribute("cachedAllMedicalReports", allReports);
            request.getRequestDispatcher("diagnosis.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Diagnosis.class.getName()).log(Level.SEVERE, null, ex);
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
        retrieveColumnName(request, response);
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
