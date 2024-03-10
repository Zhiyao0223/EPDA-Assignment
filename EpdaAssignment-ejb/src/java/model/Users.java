package model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author USER
 *
 * This class is for employees and customer
 */
@Entity
@Table(name = "Users")
public class Users extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;

    // Variables
    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;

    @Column(name = "email")
    private String email;

    @OneToOne
    Role role;

    // Constructor
    public Users() {
        super();
    }

    // Used during login
    public Users(String tmpName, String tmpPass) {
        setName(tmpName);
        this.password = tmpPass;
    }

    // Used during registration
    public Users(String tmpName, String tmpPass, Boolean register) {
        setName(tmpName);
        this.password = tmpPass;

        // Initialize default value
        if (register) {
            initializeDefaultClassValue();
        }
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRole(Role roleID) {
        this.role = roleID;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public Role getRoleID() {
        return role;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.User[ id=" + getId() + " ]";
    }

    public Long getId() {
        return userID;
    }

    public void setId(Long id) {
        this.userID = id;
    }

}
