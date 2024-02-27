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
 * @author Zhi Yao
 *
 * This class is used as the base class for user and pet
 */
@Entity
public class BaseUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    // Variables
    String name;
    char gender;

    LocalDateTime createdDate;
    LocalDateTime updatedDate;

    int status; // 0 - Active, 1 - Pending Approval, 2 - Suspended, 3 - Deleted

    // Constructor
    public BaseUser() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // System Generated Functions
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BaseUser)) {
            return false;
        }
        BaseUser other = (BaseUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.BaseUser[ id=" + id + " ]";
    }

    // Self Function
}
