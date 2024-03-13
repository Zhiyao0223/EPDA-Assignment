/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class AppointmentFacade extends AbstractFacade<Appointment> {

    @PersistenceContext(unitName = "EpdaAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppointmentFacade() {
        super(Appointment.class);
    }

    public List<Appointment> getPersonalAppointmentList(Long vetId) {
        List<Appointment> combinedDataList = new ArrayList<>();
        try {
            String customQuery = "SELECT a.id, p.name as pet_name, u.name as owner_name, wr.timeslot, a.status as appointment_status, a.createdDate, a.updatedDate "
                    + "FROM Appointment a "
                    + "JOIN a.petID p "
                    + "JOIN p.custID u "
                    + "JOIN a.schedule wr "
                    + "JOIN wr.staffId vet "
                    + "WHERE wr.staffId.id = :staffId";

            System.out.println("Query: " + customQuery);

            List<Object[]> results = em.createQuery(customQuery)
                    .setParameter("staffId", vetId)
                    .getResultList();

            System.out.println("Query result: " + results.size());

            if (!results.isEmpty()) {
                combinedDataList = convertJsonToAppointment(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return combinedDataList;
    }

    public List<Appointment> convertJsonToAppointment(List<Object[]> tmpList) {
        List<Appointment> appointmentList = new ArrayList<>();

        for (Object[] tmp : tmpList) {
            Long tmpId = (Long) tmp[0];
            String petName = (String) tmp[1];
            String ownerName = (String) tmp[2];
            Timestamp timeslot = (Timestamp) tmp[3];
            int appointmentStatus = (int) tmp[4];
            Timestamp createdDate = (Timestamp) tmp[5];
            Timestamp updatedDate = (Timestamp) tmp[6];

            appointmentList.add(new Appointment(tmpId, petName, ownerName, timeslot, appointmentStatus, createdDate, updatedDate));
        }

        return appointmentList;
    }

    public List<Appointment> sortByLatestTimeslot(List<Appointment> appointmentList) {
        // Sort appointments by timeslot in descending order (latest first)
        Collections.sort(appointmentList, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                // Compare timeslots in descending order
                return a2.getSchedule().getTimeslot().compareTo(a1.getSchedule().getTimeslot());
            }
        });
        return appointmentList;
    }

    public List<Appointment> sortByStatus(List<Appointment> appointmentList) {
        // Define a custom comparator
        Comparator<Appointment> statusComparator = new Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                // Order of status (Pending vet, scheduled, completed, cancelled)
                int[] statusOrder = {1, 2, 0, 3};

                // Get the index of each appointment's status in the statusOrder array
                int indexA1 = getStatusIndex(a1.getStatus(), statusOrder);
                int indexA2 = getStatusIndex(a2.getStatus(), statusOrder);

                // Compare the indexes to determine the order
                return Integer.compare(indexA1, indexA2);
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

        // Sort the appointmentList using the custom comparator
        Collections.sort(appointmentList, statusComparator);

        return appointmentList;
    }
}
