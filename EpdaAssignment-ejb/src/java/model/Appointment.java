/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    Expertise vetID;
    User custID;
    Pet petID;

    LocalDateTime appointmentDate;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;

    /*
    0 - Completed
    1 - Pending
    2 - Cancel
     */
    int status;

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
