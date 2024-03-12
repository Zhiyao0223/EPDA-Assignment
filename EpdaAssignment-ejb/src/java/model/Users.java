package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 *
 * @author USER
 *
 * This class is for employees and customer
 */
@Entity
@Table(name = "USERS")
public class Users extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    private Role role;

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
        setStatus(1);
        this.password = tmpPass;

        // Initialize default value
        if (register) {
            initializeDefaultClassValue();
        }
    }

    // Used during add customer (Receiptionist)
    public Users(String tmpName, String tmpEmail, Timestamp tmpDob, String tmpGender, String tmpPhoneNo) {
        super(tmpName, tmpGender);
        this.email = tmpEmail;
        this.phoneNo = tmpPhoneNo;
        this.dateOfBirth = tmpDob;
        this.role = new Role(Long.parseLong("4"));
        this.password = "";
    }

    public Role getRole() {
        return role;
    }

    public Long getUserID() {
        return id;
    }

    public void setUserID(Long userID) {
        this.id = userID;
    }

    public String getEmail() {
        return email != null ? email : "";
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

    public void setDateOfBirth(String dateOfBirth) {
        // Prevent error when parsing date
        try {
            this.dateOfBirth = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRole(Role roleID) {
        this.role = roleID;
    }

    public String getPhoneNo() {
        return phoneNo != null ? phoneNo : "";
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return (dateOfBirth == null) ? null : new SimpleDateFormat("yyyy-MM-dd").format(dateOfBirth.getTime());
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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
