package com.example.Recruitment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employeeDetails")
public class Employees implements Serializable {

    @Id
    String emailId;
    
    String agencyName,status,name,qualification,postForApply,technologiesKnown;
    Long contactNo,yearOfExperience;

    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getAgencyName() {
        return agencyName;
    }
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getQualification() {
        return qualification;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    public String getPostForApply() {
        return postForApply;
    }
    public void setPostForApply(String postForApply) {
        this.postForApply = postForApply;
    }
    public String getTechnologiesKnown() {
        return technologiesKnown;
    }
    public void setTechnologiesKnown(String technologiesKnown) {
        this.technologiesKnown = technologiesKnown;
    }
    public Long getContactNo() {
        return contactNo;
    }
    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }
    public Long getYearOfExperience() {
        return yearOfExperience;
    }
    public void setYearOfExperience(Long yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }
    
}