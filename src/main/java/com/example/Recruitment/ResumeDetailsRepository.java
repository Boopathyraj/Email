package com.example.Recruitment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeDetailsRepository extends JpaRepository<Employees, String> {

	// Employees findbyemailId(String emailId);

	

}