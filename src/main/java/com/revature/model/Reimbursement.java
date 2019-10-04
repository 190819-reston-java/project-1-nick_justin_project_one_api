package com.revature.model;

import java.io.File;
import java.sql.Date;

public class Reimbursement {
	private long id;
	private long employeeId;
	private double amount;
	private Date sqlDate;
	private String status;
	private String approvingManager;
	
	public Reimbursement() {
		super();
	}
	
	public Reimbursement(long id, long employeeId, double amount, Date sqlDate, String status, String approvingManager) {
		super();
		this.id = id;
		this.employeeId = employeeId;
		this.amount = amount;
		this.sqlDate = sqlDate;
		this.status = status;
		this.approvingManager = approvingManager;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getSqlDate() {
		return sqlDate;
	}

	public void setSqlDate(Date sqlDate) {
		this.sqlDate = sqlDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovingManager() {
		return approvingManager;
	}

	public void setApprovingManager(String approvingManager) {
		this.approvingManager = approvingManager;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", employeeId=" + employeeId + ", amount=" + amount + ", sqlDate=" + sqlDate
				+ ", status=" + status + ", approvingManager=" + approvingManager + "]";
	}
	
}