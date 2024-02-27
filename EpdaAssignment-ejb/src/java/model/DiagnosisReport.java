/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author USER
 */
@Entity
public class DiagnosisReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportID;

    // Variables
    double totalFee;

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
        if (!(object instanceof DiagnosisReport)) {
            return false;
        }
        DiagnosisReport other = (DiagnosisReport) object;
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
