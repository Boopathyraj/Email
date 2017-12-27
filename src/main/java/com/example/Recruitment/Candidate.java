package com.example.Recruitment;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "registered_candidates")
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    // @EmbeddedId
    String emailId;
    // String candidateName, qulification, address, emailId;
    // int yearsOfExperience;
    
    //Sequence name=SEQ_ID

    
    // @SequenceGenerator(name="seq",sequenceName="Seq_Id")        
    // @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq") 
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uniqueId;

    public Candidate() {
    }

    // public Candidate(int i) {
    // }

    public Candidate(String string) {
        this.emailId = string;
    }

    public Long getuniqueId() {
        return uniqueId;
    }

    public void setuniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    // public String getCandidateName() {
    //     return candidateName;
    // }

    // public void setCandidateName(String candidateName) {
    //     this.candidateName = candidateName;
    // }

    // public String getQulification() {
    //     return qulification;
    // }

    // public void setQulification(String qulification) {
    //     this.qulification = qulification;
    // }

    // public String getAddress() {
    //     return address;
    // }

    // public void setAddress(String address) {
    //     this.address = address;
    // }

    // public int getYearsOfExperience() {
    //     return yearsOfExperience;
    // }

    // public void setYearsOfExperience(int yearsOfExperience) {
    //     this.yearsOfExperience = yearsOfExperience;
    // }

}
