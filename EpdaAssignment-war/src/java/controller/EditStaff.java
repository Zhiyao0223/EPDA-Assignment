package controller;

import static controller.EditProfile.getErrorMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Users;
import model.UsersFacade;
import service.TableName;
import service.Util;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "editStaff", urlPatterns = {"/editStaff"})
public class EditStaff extends HttpServlet {

    @EJB
    private UsersFacade usersFacade;

    protected void retrieveStaffInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        servletContext.setAttribute("editUser", usersFacade.find(Long.parseLong(request.getParameter("userId"))));
        request.getRequestDispatcher("editStaff.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Check for any changes
        Boolean hasChange = false;

        // Get existing data and refetch database
        Users currentUser = (Users) request.getSession().getAttribute("user");
        currentUser = usersFacade.find(currentUser.getId());

        Users editUser = usersFacade.find(Long.parseLong(request.getParameter("userid")));

        try (PrintWriter out = response.getWriter()) {
            try {
                // Check for any changes
                String[][] appendedData = {
                    {"email", ""},
                    {"dateOfBirth", ""},
                    {"gender", ""},
                    {"phoneNo", ""},
                    {"status", ""}
                };

                // Compare changes
                if (!request.getParameter("email").trim().equalsIgnoreCase(editUser.getEmail()) && !Validation.isEmpty(request.getParameter("email"))) {
                    appendedData[0][1] = request.getParameter("email");

                    editUser.setEmail(appendedData[0][1]);
                    System.out.println("email: " + appendedData[0][1]);
                    hasChange = true;
                }
                if (!request.getParameter("dob").equals(editUser.getDateOfBirth()) && !Validation.isEmpty(request.getParameter("dob"))) {
                    appendedData[1][1] = request.getParameter("dob");

                    editUser.setDateOfBirth(appendedData[1][1]);
                    System.out.println("DOB: " + appendedData[1][1]);
                    hasChange = true;
                }
                if (!request.getParameter("gender").equals(editUser.getGender())) {
                    appendedData[2][1] = request.getParameter("gender");

                    editUser.setGender(appendedData[2][1]);
                    System.out.println("gender: " + appendedData[2][1]);
                    hasChange = true;
                }
                if (!request.getParameter("phone").trim().equals(editUser.getPhoneNo()) && !Validation.isEmpty(request.getParameter("phone"))) {
                    appendedData[3][1] = request.getParameter("phone");

                    editUser.setPhoneNo(appendedData[3][1]);
                    System.out.println("phone: " + currentUser.getPhoneNo() + "," + appendedData[3][1]);
                    hasChange = true;
                }
                System.out.println("Status:" + request.getParameter("status"));
                if (!request.getParameter("status").trim().equals(String.valueOf(editUser.getStatus()))) {
                    appendedData[4][1] = request.getParameter("status");

                    editUser.setPhoneNo(appendedData[4][1]);
                    System.out.println("Status: " + editUser.getStatus() + "," + appendedData[4][1]);
                    hasChange = true;
                }

                // No changes return, prevent access db
                if (!hasChange) {
                    throw new Exception("-1");
                }

                // Loop through all changes and update one by one
                for (String[] changes : appendedData) {
                    // Proceed if no changes
                    if (Validation.isEmpty(changes[1])) {
                        continue;
                    }

                    System.out.println("Changes: " + changes[0] + " " + changes[1]);

                    // Update db
                    if (changes[0].equals("dateOfBirth")) {
                        usersFacade.updateByAttribute(TableName.Users.name(), editUser.getId(), changes[0], Util.dateStringToTimestamp(changes[1]));
                    } else if (changes[0].equals("status")) {
                        usersFacade.updateByAttribute(TableName.Users.name(), editUser.getId(), changes[0], Integer.parseInt(changes[1]));
                    } else {
                        usersFacade.updateByAttribute(TableName.Users.name(), editUser.getId(), changes[0], changes[1]);
                    }
                }

                // Update updated time for row also
                usersFacade.refreshUpdatedDate(TableName.Users.name(), editUser.getId());
                response.sendRedirect("manageStaff.jsp?editSuccess=true");
            } catch (Exception e) {
                request.getRequestDispatcher("editStaff.jsp").include(request, response);
                out.println(getErrorMessage(e.getMessage()));
            }
        }
    }

    public static String getErrorMessage(String errCode) {
        System.out.println("Error Code: " + errCode);

        // Switch case to differenciate err msg
        switch (errCode) {
            case "-1":
                return "No changes apply.";
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
        retrieveStaffInfo(request, response);
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
