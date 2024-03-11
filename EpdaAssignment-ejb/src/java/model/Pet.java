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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
    private Long id;

    // Variables
    @OneToOne
    @JoinColumn(name = "type", referencedColumnName = "id")
    AnimalType type;

    @OneToOne
    @JoinColumn(name = "cust_id", referencedColumnName = "id")
    private Users custID;

    public Pet() {
    }

    public AnimalType getType() {
        return type;
    }

    public Users getCustID() {
        return custID;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public void setCustID(Users custID) {
        this.custID = custID;
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
        if (!(object instanceof Pet)) {
            return false;
        }
        Pet other = (Pet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pet[ id=" + id + " ]";
    }

    public Long getPetID() {
        return id;
    }

    public void setPetID(Long petID) {
        this.id = petID;
    }

}
