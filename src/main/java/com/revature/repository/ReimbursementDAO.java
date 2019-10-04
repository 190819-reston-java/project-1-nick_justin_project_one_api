package com.revature.repository;

import java.util.List;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;

public interface ReimbursementDAO {
	Reimbursement getReimbursement(long id);
	List<Reimbursement> getAllReimbursements();
	List<Reimbursement> getAllReimbursementsForEmployee(Employee e);
	boolean createReimbursement(Reimbursement r);
	boolean updateReimbursement(Reimbursement r);
	
}