package com.example.Recruitment;

import java.util.ArrayList;
import java.util.List;

public interface CandidateService {

	void addDetails(ArrayList<String> list);

	boolean validateEmail(String emailId);

	Candidate getId(String emailId);

	void addResumeDetails(Employees emp);
	
	void updateResumeDetails(Employees eobj);


}