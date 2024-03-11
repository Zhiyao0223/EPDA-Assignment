package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
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

    // Get all role related data
    protected void retrieveRoleData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve all roles from the facade
        List<Role> roles = roleFacade.findAll();

        // Capitalize all first case
        for (Role role : roles) {
            String tmp = role.getDescription();

            // Check if description is not empty
            if (tmp != null && !tmp.isEmpty()) {
                // Capitalize the first letter
                role.setDescription(tmp.substring(0, 1).toUpperCase() + tmp.substring(1));
            }
        }

        // Get ServletContext
        ServletContext servletContext = request.getServletContext();

        // Set DataCache in application scope
        servletContext.setAttribute("cachedRoles", roles);

        // Check if come from add staff
        if (request.getParameter("addStaff") != null) {
            servletContext.setAttribute("isAddStaff", "true");
        }

        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    // Process form request (POST)
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Initialize tmp variable
        Users tmpUser = new Users(request.getParameter("username"), request.getParameter("password"), true);
        Boolean isAddStaff = !Validation.isEmpty(request.getParameter("isAddStaff"));

        try (PrintWriter out = response.getWriter()) {
            try {

                if (Validation.isEmpty(tmpUser.getName()) || Validation.isEmpty(tmpUser.getPassword()) || Validation.isEmpty(request.getParameter("role"))) {
                    throw new Exception("-2");
                } // Check if input empty
                else if (!tmpUser.getPassword().equals(request.getParameter("confirmPass"))) {
                    throw new Exception("-3");
                } // Check if confirm pass same with original

                // Set role
                tmpUser.setRole(roleFacade.findByAttribute("description", request.getParameter("role").toLowerCase()));

                // Check if username register before
                if (usersFacade.findByAttribute("name", tmpUser.getName()) != null) {
                    throw new Exception("-1");
                }

                // Change user status if add by manager
                if (isAddStaff) {
                    tmpUser.setStatus(0);
                }

                // Create record in users table
                usersFacade.create(tmpUser);

                // Go to login page
                if (isAddStaff) {
                    request.getRequestDispatcher("register.jsp").include(request, response);
                    out.println(tmpUser.getName() + " is successful added as a " + tmpUser.getRole().getDescription());
                } else {
                    response.sendRedirect("login.jsp?registrationSuccess=true");
                }
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
        retrieveRoleData(request, response);
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
