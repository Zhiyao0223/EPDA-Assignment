/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author USER
 */
@MappedSuperclass
public class AbstractUser {

    // Variables
    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    private int status; // 0 - Active, 1 - Pending Approval, 2 - Suspended, 3 - Deleted

    // Constructor
    public AbstractUser() {
        initializeDefaultClassValue();
    }

    // Used in registration
    public AbstractUser(String name) {
        this.name = name;

        // Initialize default value
        initializeDefaultClassValue();
    }

    // Used in add customer (Receiptionist)
    public AbstractUser(String tmpName, String tmpGender) {
        this.name = tmpName;
        this.gender = tmpGender;
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.updatedDate = new Timestamp(System.currentTimeMillis());
        this.status = 0;
    }

    public AbstractUser(String name, String gender, Timestamp createdDate, Timestamp updatedDate, int status) {
        this.name = name;
        this.gender = gender;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return (gender == null || gender.trim().isEmpty()) ? "" : gender;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public int getStatus() {
        return status;
    }

    // Initialize default value
    public void initializeDefaultClassValue() {
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.updatedDate = new Timestamp(System.currentTimeMillis());
        this.gender = null;
    }

    public void onUpdate() {
        setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    }
}
