/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Collections;
import java.util.Comparator;
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
        return allUsers.size() > 1 ? sortByStatus(allUsers) : allUsers;
    }

    // Sort by status
    public List<Users> sortByStatus(List<Users> userList) {
        // Define a custom comparator
        Comparator<Users> statusComparator = new Comparator<Users>() {
            @Override
            public int compare(Users u1, Users u2) {
                // Order of status (Pending approval, active, suspended, deleted)
                int[] statusOrder = {1, 0, 2, 3};

                // Get the index of each appointment's status in the statusOrder array
                int indexU1 = getStatusIndex(u1.getStatus(), statusOrder);
                int indexU2 = getStatusIndex(u2.getStatus(), statusOrder);

                // Compare the indexes to determine the order
                return Integer.compare(indexU1, indexU2);
            }

            // Helper method to get the index of a status in the statusOrder array
            private int getStatusIndex(int status, int[] statusOrder) {
                for (int i = 0; i < statusOrder.length; i++) {
                    if (statusOrder[i] == status) {
                        return i;
                    }
                }
                // If status is not found in the statusOrder array, return a large value to push it to the end
                return statusOrder.length;
            }
        };

        // Sort the userList using the custom comparator
        Collections.sort(userList, statusComparator);
        return userList;
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

    // Retrieve vet only
    public List<Users> findVet() {
        List<Users> vets = findAll();

        // Remove deleted user
        Iterator<Users> iterator = vets.iterator();
        while (iterator.hasNext()) {
            Users user = iterator.next();
            if (!"vet".equals(user.getRole().getDescription())) {
                iterator.remove();
            }
        }
        return vets;
    }
}
