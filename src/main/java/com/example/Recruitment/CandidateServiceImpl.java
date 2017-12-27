package com.example.Recruitment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateServiceImpl implements CandidateService {

	@Autowired
	private CandidateRepository repo;

	@Autowired
    private ResumeDetailsRepository resumeRepo;

	@Override
	public void addDetails(ArrayList<String> list) {

		ArrayList<Candidate> productsInBatch = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Candidate p = new Candidate(list.get(i));
			productsInBatch.add(p);
		}
		Set<Candidate> hs = new HashSet<Candidate>(productsInBatch);
		hs.addAll(productsInBatch);
		productsInBatch.clear();
		productsInBatch.addAll(hs);
		//    System.out.println("sdfds" +productsInBatch.size());    

		repo.save(productsInBatch);

	}

	@Override
	public Candidate getId(String emailId) {
		Candidate no = repo.findByemailId(emailId);
		System.out.println("get :" + no.getuniqueId());

		return no;
	}

	@Override
	public boolean validateEmail(String emailId) {
		boolean isValid = false;
		List<Candidate> list = repo.findAll();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getEmailId().contentEquals(emailId)) {
				isValid = true;
			}
		}
		return isValid;

	}


	@Override
	public void addResumeDetails(Employees emp) {
		System.out.println(emp.agencyName);
		this.resumeRepo.save(emp);
	}



	@Override
	public void updateResumeDetails(Employees eobj) {
		//  Employees list = this.resumeRepo.findbyemailId(eobj.getEmailId());
		// System.out.println(list.agencyName);
	}

	

}