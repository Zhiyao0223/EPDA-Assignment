package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Users;
import model.UsersFacade;
import service.TableName;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/ChangePassword"})
public class ChangePassword extends HttpServlet {

    @EJB
    private UsersFacade usersFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Get POST param
        String currentPass = request.getParameter("currentPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        // Get current user
        HttpSession s = request.getSession();
        Users currentUser = (Users) s.getAttribute("user");

        // Refetch db to get latest data
        currentUser = usersFacade.find(currentUser.getId());

        try (PrintWriter out = response.getWriter()) {
            try {
                if (Validation.isEmpty(currentPass) || Validation.isEmpty(newPass) || Validation.isEmpty(confirmPass)) {
                    throw new Exception("-2");
                } // Empty field
                else if (!currentPass.equals(currentUser.getPassword())) {
                    throw new Exception("-3");
                } // Incorrect Password
                else if (!newPass.equals(confirmPass)) {
                    throw new Exception("-1");
                } // Password mismatch

                // Update db
                usersFacade.updateByAttribute(TableName.Users.name(), currentUser.getId(), "password", newPass);

                // Prompt and redirect to index
                response.sendRedirect("editProfile.jsp?changePassSuccess=true");
            } catch (Exception e) {
                request.setAttribute("isChangePass", "true");
                request.getRequestDispatcher("editProfile.jsp?isChangePass=true").include(request, response);
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
                return "Password mismatch.";
            case "-2":
                return "Please enter the required field.";
            case "-3":
                return "Incorrect password.";
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
