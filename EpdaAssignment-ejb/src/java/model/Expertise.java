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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author USER
 */
@Entity
public class Expertise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long expertiseID;

    // Variables
    @ManyToOne
    @JoinColumn(name = "vet_id", referencedColumnName = "id")
    private Users vetID;

    @OneToOne
    @JoinColumn(name = "animal_type", referencedColumnName = "id")
    private AnimalType animalType;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "status")
    private int status; // 0 - Enable, 1 - Disable

    // Constructor
    public Expertise() {
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.updatedDate = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return expertiseID;
    }

    public void setId(Long id) {
        this.expertiseID = id;
    }

    public Long getExpertiseID() {
        return expertiseID;
    }

    public void setExpertiseID(Long expertiseID) {
        this.expertiseID = expertiseID;
    }

    public Users getVetID() {
        return vetID;
    }

    public void setVetID(Users vetID) {
        this.vetID = vetID;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expertiseID != null ? expertiseID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expertise)) {
            return false;
        }
        Expertise other = (Expertise) object;
        if ((this.expertiseID == null && other.expertiseID != null) || (this.expertiseID != null && !this.expertiseID.equals(other.expertiseID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Vet[ id=" + expertiseID + " ]";
    }

}
