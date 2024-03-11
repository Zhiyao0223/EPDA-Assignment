/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author USER
 */
@Entity
public class MedicalReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Variables
    @OneToOne
    @JoinColumn(name = "appointment", referencedColumnName = "id")
    private Appointment appointment;

    @Column(name = "total_fee")
    private double totalFee;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private int type; // 0 - Diagnosis, 1 - Prognosis

    @Column(name = "status")
    private int status;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    public MedicalReport() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getReportID() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public void setReportID(Long reportID) {
        this.id = reportID;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(int type) {
        this.type = type;
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
        if (!(object instanceof MedicalReport)) {
            return false;
        }
        MedicalReport other = (MedicalReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.DiagnosisReport[ id=" + id + " ]";
    }

}
