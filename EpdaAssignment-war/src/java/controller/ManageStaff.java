package controller;

import com.google.gson.Gson;
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
import model.Log;
import model.LogAction;
import model.LogFacade;
import model.Users;
import model.UsersFacade;
import service.TableName;

/**
 *
 * @author USER
 */
@WebServlet(name = "ManageStaff", urlPatterns = {"/ManageStaff"})
public class ManageStaff extends HttpServlet {

    @EJB
    private LogFacade logFacade;

    @EJB
    private UsersFacade usersFacade;

    protected void deleteStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type to JSON
        response.setContentType("application/json");

        // Initialize var
        String[] err = {"0"};

        // Get POST params
        String staffId = request.getParameter("id");

        // Get current logined user and deleted user
        Users currentUser = usersFacade.find(((Users) request.getSession().getAttribute("user")).getId());
        Users deletedUser = usersFacade.find(Long.parseLong(staffId));

        try {
            if (currentUser.getId().equals(Long.parseLong(staffId))) {
                throw new Exception("-1");
            } // Check if delete self
            else if (currentUser.getRole().getId().equals(deletedUser.getRole().getId())) {
                throw new Exception("-2");
            } // Check if delete someone that has same role

            // Change status to delete in db
            usersFacade.updateByAttribute(TableName.Users.name(), deletedUser.getId(), "status", 3);
            usersFacade.refreshUpdatedDate(TableName.Users.name(), deletedUser.getId());

            logFacade.create(new Log(currentUser, "Soft delete user ID - " + deletedUser.getId(), LogAction.DELETE));
        } catch (Exception e) {
            // Write JSON to response
            err[1] = e.getMessage();
        }

        response.getWriter().write(new Gson().toJson(err));
    }

    protected void retrievedStaffData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Users> staff = usersFacade.findAllLegitUsers();

        // Set response content type to JSON
        response.setContentType("application/json");

        // Write JSON to response
        response.getWriter().write(new Gson().toJson(staff));
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
        retrievedStaffData(request, response);
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
        deleteStaff(request, response);
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
