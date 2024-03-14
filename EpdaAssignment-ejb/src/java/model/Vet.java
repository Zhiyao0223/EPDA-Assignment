package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class Vet {

    private Long vetId;
    private String name;
    private List<String> expertiseAreas;
    private Timestamp[] timeslots;

    public Vet(Long vetId, String name, List<String> expertiseAreas) {
        this.vetId = vetId;
        this.name = name;
        this.expertiseAreas = expertiseAreas;
        this.timeslots = new Timestamp[2];
    }

    public void addExpertise(String animalType) {
        this.expertiseAreas.add(animalType);
    }

    public Long getVetId() {
        return vetId;
    }

    public void setVetId(Long vetId) {
        this.vetId = vetId;
    }

    public Timestamp[] getTimeslots() {
        return timeslots;
    }

    public void setTimeslot(Timestamp timeslot, int index) {
        this.timeslots[index] = timeslot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getExpertiseAreas() {
        return expertiseAreas;
    }

    public void setExpertiseAreas(List<String> expertiseAreas) {
        this.expertiseAreas = expertiseAreas;
    }

}
