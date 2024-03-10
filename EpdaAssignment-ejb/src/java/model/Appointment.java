/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author USER
 */
@Entity
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long appointmentID;

    // Variables
    private int status = 0; // 0 - Complete, 1 - Pending Vet, 2 - Scheduled, 3 - Cancelled

    @OneToOne
    private Users vetID;

    @OneToOne
    private Users custID;

    @OneToOne
    private Pet petID;

    private Timestamp appointmentDate;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public Appointment() {
    }

    public void setAppointmentID(Long appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setVetID(Users vetID) {
        this.vetID = vetID;
    }

    public void setCustID(Users custID) {
        this.custID = custID;
    }

    public void setPetID(Pet petID) {
        this.petID = petID;
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

    public Users getVetID() {
        return vetID;
    }

    public Users getCustID() {
        return custID;
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

    public Appointment(Users vetID, Users custID, Pet petID, Timestamp appointmentDate, Timestamp createdDate, Timestamp updatedDate) {
        this.vetID = vetID;
        this.custID = custID;
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
