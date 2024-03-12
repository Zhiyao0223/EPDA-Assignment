/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "EpdaAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    // This function return all users except deleted
    public List<Users> findAllLegitUsers() {
        List<Users> allUsers = findAll();

        // Remove deleted user
        Iterator<Users> iterator = allUsers.iterator();
        while (iterator.hasNext()) {
            Users user = iterator.next();
            if (user.getStatus() == 3) {
                iterator.remove();
            }
        }
        return allUsers;
    }

    // Retrieve customer only
    public List<Users> findCustomer() {
        List<Users> customers = findAll();

        // Remove deleted user
        Iterator<Users> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Users user = iterator.next();
            if (!"customer".equals(user.getRole().getDescription())) {
                iterator.remove();
            }
        }
        return customers;
    }
}
