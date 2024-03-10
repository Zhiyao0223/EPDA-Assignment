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
public class Pet
        extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long petID;

    // Variables
    AnimalType type;

    public Pet() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (petID != null ? petID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pet)) {
            return false;
        }
        Pet other = (Pet) object;
        if ((this.petID == null && other.petID != null) || (this.petID != null && !this.petID.equals(other.petID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pet[ id=" + petID + " ]";
    }

    public Long getPetID() {
        return petID;
    }

    public void setPetID(Long petID) {
        this.petID = petID;
    }

}
