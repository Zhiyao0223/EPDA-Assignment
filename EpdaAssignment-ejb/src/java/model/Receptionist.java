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
public class Receptionist extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long receiptionistID;

    public Long getId() {
        return receiptionistID;
    }

    public void setId(Long id) {
        this.receiptionistID = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receiptionistID != null ? receiptionistID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Receptionist)) {
            return false;
        }
        Receptionist other = (Receptionist) object;
        if ((this.receiptionistID == null && other.receiptionistID != null) || (this.receiptionistID != null && !this.receiptionistID.equals(other.receiptionistID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Receptionist[ id=" + receiptionistID + " ]";
    }

}
