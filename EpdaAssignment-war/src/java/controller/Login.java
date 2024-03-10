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
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

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

        // Check for POST variable
        Users tmpUser = new Users(request.getParameter("username"), request.getParameter("password"));

        try (PrintWriter out = response.getWriter()) {
            try {
                // Check if empty input
                if (Validation.isEmpty(tmpUser.getName()) || Validation.isEmpty(tmpUser.getPassword())) {
                    throw new Exception("-2");
                }

                // Get object from database, null if invalid
                Users dbUser = usersFacade.find(tmpUser.getName());

                // No username found or invalid password
                if (dbUser == null || !dbUser.getPassword().equals(tmpUser.getPassword())) {
                    throw new Exception("-1");
                }

                // If found set session
                HttpSession s = request.getSession();
                s.setAttribute("loginUser", tmpUser);

                request.getRequestDispatcher("index.jsp").include(request, response);
                out.println("<br><br><br>Hi " + tmpUser.getName() + ", welcome to APU!");
            } catch (Exception e) {
                request.getRequestDispatcher("login.jsp").include(request, response);
                out.println(e.getMessage() == "-1" ? "Invalid Username / Password" : "Please Enter Username and Password");
            }
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
