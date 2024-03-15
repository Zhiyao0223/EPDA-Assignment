package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
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
import service.Util;

/**
 *
 * @author USER
 */
@WebServlet(name = "editProfile", urlPatterns = {"/editProfile"})
public class EditProfile extends HttpServlet {

    @EJB
    private UsersFacade usersFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Check for any changes
        Boolean hasChange = false;

        // Get existing data and refetch database
        Users currentUser = (Users) request.getSession().getAttribute("user");
        currentUser = usersFacade.find(currentUser.getId());

        try (PrintWriter out = response.getWriter()) {
            try {
                // Check for any changes
                String[][] appendedData = {
                    {"email", ""},
                    {"dateOfBirth", ""},
                    {"gender", ""},
                    {"phoneNo", ""}
                };

                // Validate data
                if (!Validation.isValidDateofBirth(request.getParameter("dob"))) {
                    throw new Exception("-2");
                } else if (!Validation.isValidPhoneNumber(request.getParameter("phone"))) {
                    throw new Exception("-3");
                }

                // Compare changes
                if (!request.getParameter("email").trim().equalsIgnoreCase(currentUser.getEmail()) && !Validation.isEmpty(request.getParameter("email"))) {
                    appendedData[0][1] = request.getParameter("email");
                    currentUser.setEmail(appendedData[0][1]);

                    System.out.println("email: " + appendedData[0][1]);
                    hasChange = true;
                }
                if (!request.getParameter("dob").equals(currentUser.getDateOfBirth()) && !Validation.isEmpty(request.getParameter("dob"))) {
                    appendedData[1][1] = request.getParameter("dob");
                    currentUser.setDateOfBirth(appendedData[1][1]);

                    System.out.println("DOB: " + appendedData[1][1]);
                    hasChange = true;
                }
                if (!request.getParameter("gender").equals(currentUser.getGender()) && !Validation.isEmpty(request.getParameter("gender"))) {
                    appendedData[2][1] = request.getParameter("gender");
                    currentUser.setGender(appendedData[2][1]);

                    System.out.println("gender: " + appendedData[2][1]);
                    hasChange = true;
                }
                if (!request.getParameter("phone").trim().equals(currentUser.getPhoneNo()) && !Validation.isEmpty(request.getParameter("phone"))) {
                    appendedData[3][1] = request.getParameter("phone");
                    currentUser.setPhoneNo(appendedData[3][1]);

                    System.out.println("phone: " + currentUser.getPhoneNo() + "," + appendedData[3][1]);
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
                    // If datetime need convert before update
                    if (changes[0].equals("dateOfBirth")) {
                        usersFacade.updateByAttribute(TableName.Users.name(), currentUser.getId(), changes[0], Util.dateStringToTimestamp(changes[1]));
                    } else {
                        usersFacade.updateByAttribute(TableName.Users.name(), currentUser.getId(), changes[0], changes[1]);
                    }
                }

                // Replace session with new data
                request.getSession().setAttribute("user", currentUser);

                response.sendRedirect("index.jsp?updateSuccess=true");

            } catch (Exception e) {
                request.getRequestDispatcher("editProfile.jsp").include(request, response);
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
            case "-2":
                return "Invalid date of birth. Must be older than 12 years old";
            case "-3":
                return "Invalid phone number";
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
