package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Role;
import model.RoleFacade;
import model.Users;
import model.UsersFacade;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    @EJB
    private RoleFacade roleFacade;

    @EJB
    private UsersFacade usersFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Initialize tmp variable
        Users tmpUser = new Users(request.getParameter("username"), request.getParameter("password"), true);

        try (PrintWriter out = response.getWriter()) {
            try {

                if (Validation.isEmpty(tmpUser.getName()) || Validation.isEmpty(tmpUser.getPassword()) || Validation.isEmpty(request.getParameter("role"))) {
                    throw new Exception("-2");
                } // Check if input empty
                else if (!tmpUser.getPassword().equals(request.getParameter("confirmPass"))) {
                    throw new Exception("-3");
                } // Check if confirm pass same with original

                // Set role
                tmpUser.setRole(roleFacade.findByAttribute("description", request.getParameter("role")));

                // Check if username register before
                if (usersFacade.findByAttribute("name", tmpUser.getName()) != null) {
                    throw new Exception("-1");
                }

                // Create record in users table
                usersFacade.create(tmpUser);

                // Go to login page
                response.sendRedirect("login.jsp?registrationSuccess=true");
            } catch (Exception e) {
                request.getRequestDispatcher("register.jsp").include(request, response);
                out.println(getErrorMessage(e.getMessage()));
            }
        }
    }

    /*
    Check for relevant error code and return err message
    @param errCode error code
    @return error message
     */
    private String getErrorMessage(String errCode) {
        System.out.println("Error Code: " + errCode);

        // Switch case to differenciate err msg
        switch (errCode) {
            case "-1":
                return "Username exist. Please proceed to login or change your username.";
            case "-2":
                return "Please enter the required field.";
            case "-3":
                return "Password not match.";
            default:
                return "Unknown error.";
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
        processRequest(request, response);
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
