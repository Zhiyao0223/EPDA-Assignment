/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private Long appointmentID;

    // Variables
    private int status = 0; // 0 - Complete, 1 - Pending Vet, 2 - Scheduled, 3 - Cancelled

    @OneToOne
    @JoinColumn(name = "working_rota_id", referencedColumnName = "id")
    private WorkingRota schedule;

    @OneToOne
    @Column(name = "id")
    private Pet petID;

    @Column(name = "appointment_date")
    private Timestamp appointmentDate;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    public Appointment() {
    }

    public void setAppointmentID(Long appointmentID) {
        this.appointmentID = appointmentID;
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

    public void setSchedule(WorkingRota schedule) {
        this.schedule = schedule;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getAppointmentID() {
        return appointmentID;
    }

    public int getStatus() {
        return status;
    }

    public Pet getPetID() {
        return petID;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public Appointment(Pet petID, Timestamp appointmentDate, Timestamp createdDate, Timestamp updatedDate) {
        this.petID = petID;
        this.appointmentDate = appointmentDate;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Long getId() {
        return appointmentID;
    }

    public void setId(Long id) {
        this.appointmentID = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentID != null ? appointmentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) object;
        if ((this.appointmentID == null && other.appointmentID != null) || (this.appointmentID != null && !this.appointmentID.equals(other.appointmentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Appointment[ id=" + appointmentID + " ]";
    }

}
