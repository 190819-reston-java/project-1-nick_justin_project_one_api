package com.revature.service;

import com.revature.repository.ReimbursementDAO;

import java.util.List;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;

public class ReimbursementService {
	
	private ReimbursementDAO reimbursementDAO;
	
	public ReimbursementService(ReimbursementDAO reimbursementDAO) {
		this.reimbursementDAO = reimbursementDAO;
	}
	
	public boolean submitRequest(Employee e, Reimbursement r) {
		reimbursementDAO.createReimbursement(r);
		return true;
	}

	public boolean createReimbursement(Employee emp, Reimbursement r) {
		reimbursementDAO.createReimbursement(r);
		return false;
	}
	
	public boolean updateReimbursement (Reimbursement r) {
		return reimbursementDAO.updateReimbursement(r);
	}
	
	public Reimbursement getReimbursement(long id) {
		return reimbursementDAO.getReimbursement(id);
	}

	public List<Reimbursement> getReimbursements() {
		return reimbursementDAO.getAllReimbursements();
	}
	
	public List<Reimbursement> getReimbursementsForEmployee(Employee emp){
		return reimbursementDAO.getAllReimbursementsForEmployee(emp);
	}
}
