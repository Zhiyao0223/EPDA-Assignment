package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "Appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Variables
    @OneToOne
    @JoinColumn(name = "working_rota_id", referencedColumnName = "id")
    private WorkingRota schedule;

    @OneToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private Pet petID;

    @Column(name = "status")
    private int status = 0; // 0 - Complete, 1 - Pending Vet, 2 - Scheduled, 3 - Cancelled

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    public Appointment() {
    }

    // Used in manage appointment (Vet)
    public Appointment(Long tmpId, String petName, String ownerName, Timestamp timeslot, int appointmentStatus, Timestamp createdDate, Timestamp updatedDate) {
        // Initialize tmp class to store var
        Pet pet = new Pet();
        Users customer = new Users();
        WorkingRota schedule = new WorkingRota();
        this.id = tmpId;
        this.petID = pet;
        this.status = appointmentStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.schedule = schedule;
        this.schedule.setTimeslot(timeslot);

        // Set customer class
        customer.setName(ownerName);

        // Set pet class
        this.petID.setName(petName);
        this.petID.setCustID(customer);
    }

    public void setAppointmentID(Long appointmentID) {
        this.id = appointmentID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPetID(Pet petID) {
        this.petID = petID;
    }

    public WorkingRota getSchedule() {
        return schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSchedule(WorkingRota schedule) {
        this.schedule = schedule;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getAppointmentID() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Pet getPetID() {
        return petID;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public Appointment(Pet petID, Timestamp createdDate, Timestamp updatedDate) {
        this.petID = petID;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Appointment[ id=" + id + " ]";
    }

}
