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

    @Column(name = "diagnosis_detail")
    private String diagnosisDetail;

    @Column(name = "prognosis_detail")
    private String prognosisDetail;

    @Column(name = "status")
    private int status; // 0 - Complete, 1 - Pending prognosis

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    public MedicalReport() {
    }

    // Used in view medical report page
    public MedicalReport(Long tmpId, String diagnosisDetail, String prognosisDetail, double totalFee, int reportStatus, Timestamp createdDate, Timestamp updatedDate, String petName, String custName, String animalType, Timestamp tmpTimeslot) {
        this.id = tmpId;
        this.diagnosisDetail = diagnosisDetail;
        this.prognosisDetail = prognosisDetail;
        this.totalFee = totalFee;
        this.status = reportStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.appointment = new Appointment();

        // Set value for working rota
        WorkingRota tmpWorkingRota = new WorkingRota();
        tmpWorkingRota.setTimeslot(tmpTimeslot);
        this.appointment.setSchedule(tmpWorkingRota);

        // Set value for users class
        Users tmpUser = new Users();
        tmpUser.setName(custName);

        // Set value for animalType class
        AnimalType tmpType = new AnimalType();
        tmpType.setDescription(animalType);

        // Set value for pet class
        Pet tmpPet = new Pet();
        tmpPet.setType(tmpType);
        tmpPet.setCustID(tmpUser);
        tmpPet.setName(petName);
        this.appointment.setPetID(tmpPet);
    }

    public int getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiagnosisDetail() {
        return diagnosisDetail;
    }

    public void setDiagnosisDetail(String diagnosisDetail) {
        this.diagnosisDetail = diagnosisDetail;
    }

    public String getPrognosisDetail() {
        return prognosisDetail;
    }

    public void setPrognosisDetail(String prognosisDetail) {
        this.prognosisDetail = prognosisDetail;
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

    public void setReportID(Long reportID) {
        this.id = reportID;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
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
