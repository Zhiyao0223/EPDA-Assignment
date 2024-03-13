/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author USER
 */
@Stateless
public class WorkingRotaFacade extends AbstractFacade<WorkingRota> {

    @EJB
    private ExpertiseFacade expertiseFacade;

    @EJB
    private UsersFacade usersFacade;

    @PersistenceContext(unitName = "EpdaAssignment-ejbPU")
    private EntityManager em;

    // For timetable use
    private static final int NUM_DAYS = 7;
    private static final int NUM_EXPERTISE = 5;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WorkingRotaFacade() {
        super(WorkingRota.class);
    }

    public Timestamp getLatestRowTimestamp() {
        List<WorkingRota> tmpList = findAll();
        return tmpList.get(tmpList.size() - 1).getTimeslot();
    }

    public List<WorkingRota> getAvailableSlot() {
        List<WorkingRota> scheduleList = findAll();

        // Remove deleted user
        Iterator<WorkingRota> iterator = scheduleList.iterator();
        while (iterator.hasNext()) {
            WorkingRota singleSchedule = iterator.next();
            if (singleSchedule.getStatus() == 1) {
                iterator.remove();
            }
        }
        return scheduleList;
    }

    // Convert list of vet to vet class
    public Map<String, List<Vet>> generateTimetable() {
        List<Vet> vetList = new ArrayList<>();
        List<Users> vets = usersFacade.findVet();
        List<Expertise> expertises = expertiseFacade.findAll();

        Map<Long, Vet> vetMap = new HashMap<>();

        // Group expertise entries by vet ID
        for (Expertise expertise : expertises) {
            long vetId = expertise.getVetID().getId();

            Vet vet = vetMap.getOrDefault(vetId, new Vet(getVetName(vets, vetId), new ArrayList<>()));
            vet.addExpertise(expertise.getAnimalType().getDescription());
            vetMap.put(vetId, vet);
        }

        vetList = new ArrayList<>(vetMap.values());

        Map<String, List<Vet>> workingRota = generateWorkingRota(vetList);
//        for (Map.Entry<String, List<Vet>> entry : workingRota.entrySet()) {
//            String day = entry.getKey();
//            List<Vet> vetsForDay = entry.getValue();
//
//            System.out.println(day + ":");
//            for (Vet vet : vetsForDay) {
//                System.out.println("Vet: " + vet.name);
//                System.out.println("Expertise Areas: " + String.join(", ", vet.expertiseAreas));
//                System.out.println(); // Separate each vet's information
//            }
//        }
        return workingRota;
    }

    // Method to retrieve vet name from the list of vets
    private String getVetName(List<Users> vets, long vetId) {
        for (Users vet : vets) {
            if (vet.getId() == vetId) {
                return vet.getName();
            }
        }
        return ""; // Return empty string if vet name not found
    }

    // Actual algorithm to generate working rota
    public Map<String, List<Vet>> generateWorkingRota(List<Vet> vets) {
        Map<String, List<Vet>> workingRota = new HashMap<>();

        // Shuffle the list of vets to randomize assignment
        Collections.shuffle(vets);

        // Initialize working rota for each day of the week
        for (int i = 0; i < NUM_DAYS; i++) {
            workingRota.put("Day " + (i + 1), new ArrayList<>());
        }

        // Iterate through each day of the week
        for (int i = 0; i < NUM_DAYS; i++) {
            // Get the current day
            String day = "Day " + (i + 1);
            List<Vet> dayVets = workingRota.get(day);

            // Assign vets to the current day ensuring coverage of expertise areas
            while (dayVets.size() < 3 || countCoveredExpertise(dayVets) < NUM_EXPERTISE) {
                Vet vet = getRandomVet(vets);
                if (!dayVets.contains(vet)) {
                    dayVets.add(vet);
                }
            }
        }
        return workingRota;
    }

    // Count the number of unique expertise areas covered by the given list of vets
    private int countCoveredExpertise(List<Vet> vets) {
        Set<String> expertiseSet = new HashSet<>();
        for (Vet vet : vets) {
            expertiseSet.addAll(vet.getExpertiseAreas());
        }
        return expertiseSet.size();
    }

    // Get a random vet from the list
    private static Vet getRandomVet(List<Vet> vets) {
        Random rand = new Random();
        return vets.get(rand.nextInt(vets.size()));
    }
}
