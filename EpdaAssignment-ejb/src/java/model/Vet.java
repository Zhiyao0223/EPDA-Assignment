package model;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author USER
 */
public class Vet {

    private String name;
    private List<String> expertiseAreas;
    private Timestamp timeslot;

    public Vet(String name, List<String> expertiseAreas) {
        this.name = name;
        this.expertiseAreas = expertiseAreas;
    }

    public void addExpertise(String animalType) {
        this.expertiseAreas.add(animalType);
    }

    public Timestamp getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timestamp timeslot) {
        this.timeslot = timeslot;
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
