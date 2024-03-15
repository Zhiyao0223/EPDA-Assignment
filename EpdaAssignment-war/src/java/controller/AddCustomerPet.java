package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AnimalType;
import model.AnimalTypeFacade;
import model.Log;
import model.LogAction;
import model.LogFacade;
import model.Pet;
import model.PetFacade;
import model.Users;
import model.UsersFacade;
import service.Util;
import service.Validation;

/**
 *
 * @author USER
 */
@WebServlet(name = "AddCustomerPet", urlPatterns = {"/AddCustomerPet"})
public class AddCustomerPet extends HttpServlet {

    @EJB
    private LogFacade logFacade;

    @EJB
    private PetFacade petFacade;

    @EJB
    private AnimalTypeFacade animalTypeFacade;

    @EJB
    private UsersFacade usersFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Boolean isAddCustomer = request.getParameter("formType").equals("customer");

        try (PrintWriter out = response.getWriter()) {
            try {
                Pet newPet;
                Users newUser;

                // Check if add pet or customer
                if (isAddCustomer) {
                    // Validate data
                    if (Validation.isEmpty(request.getParameter("name")) || Validation.isEmpty(request.getParameter("email"))
                            || Validation.isEmpty(request.getParameter("dob")) || Validation.isEmpty(request.getParameter("phone"))) {
                        throw new Exception("-2");
                    } else if (!Validation.isValidDateofBirth(request.getParameter("dob"))) {
                        throw new Exception("-1");
                    } else if (!Validation.isValidPhoneNumber(request.getParameter("phone"))) {
                        throw new Exception("-3");
                    } else if (usersFacade.findByAttribute("email", request.getParameter("email")) != null) {
                        throw new Exception("-4");
                    }

                    newUser = new Users(
                            request.getParameter("name"),
                            request.getParameter("email"),
                            Util.dateStringToTimestamp(request.getParameter("dob")),
                            request.getParameter("gender"),
                            request.getParameter("phone"));
                    usersFacade.create(newUser);
                } else {
                    // Validate data
                    if (Validation.isEmpty(request.getParameter("name"))) {
                        throw new Exception("-2");
                    }

                    // Get owner
                    Users inputOwner = new Users();
                    inputOwner.setId(Long.parseLong(request.getParameter("userId")));

                    // Get animaltype
                    AnimalType inputAnimalType = new AnimalType();
                    inputAnimalType.setId(Long.parseLong(request.getParameter("animalType")));

                    newPet = new Pet(
                            request.getParameter("name"),
                            request.getParameter("gender"),
                            inputOwner,
                            inputAnimalType);
                    petFacade.create(newPet);
                }
                Users currentUser = (Users) request.getSession().getAttribute("user");
                String description = (isAddCustomer) ? "Generate new customer" : "Generate new pet";
                logFacade.create(new Log(currentUser, description, LogAction.CREATE));

                // Back to homepage if success
                String webParam = (isAddCustomer) ? "addCustomerSuccess" : "addPetSuccess";
                response.sendRedirect("index.jsp?" + webParam + "=true");

            } catch (Exception e) {
                response.sendRedirect("addCustomerPet.jsp?err=" + e.getMessage());
            }
        }
    }

    protected void retrieveFormRequireData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<AnimalType> animalTypeList = animalTypeFacade.findAll();
        List<Users> custList = usersFacade.findCustomer();

        // Used to store both list and return to JSP
        JsonObject combinedJson = new JsonObject();

        // Convert AnimalType list to JSON array
        JsonArray animalTypeJsonArray = new JsonArray();
        for (AnimalType animalType : animalTypeList) {
            JsonObject animalTypeJson = new JsonObject();
            animalTypeJson.addProperty("id", animalType.getId());
            animalTypeJson.addProperty("name", animalType.getDescription());
            animalTypeJsonArray.add(animalTypeJson);
        }
        combinedJson.add("animalTypes", animalTypeJsonArray);

        // Convert Users list to JSON array
        JsonArray custJsonArray = new JsonArray();
        for (Users user : custList) {
            JsonObject userJson = new JsonObject();
            userJson.addProperty("id", user.getId());
            userJson.addProperty("name", user.getName());
            custJsonArray.add(userJson);
        }
        combinedJson.add("customers", custJsonArray);

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
