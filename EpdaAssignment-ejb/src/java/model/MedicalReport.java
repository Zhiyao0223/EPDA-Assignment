/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class MedicalReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportID;

    // Variables
    @OneToOne
    private Appointment appointment;
    private double totalFee;
    private String description;
    private int type; // 0 - Diagnosis, 1 - Prognosis
    private int status;
    private Timestamp createdDate;
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
        return reportID;
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
        this.reportID = reportID;
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

    public Long getId() {
        return reportID;
    }

    public void setId(Long id) {
        this.reportID = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportID != null ? reportID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicalReport)) {
            return false;
        }
        MedicalReport other = (MedicalReport) object;
        if ((this.reportID == null && other.reportID != null) || (this.reportID != null && !this.reportID.equals(other.reportID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.DiagnosisReport[ id=" + reportID + " ]";
    }

}
