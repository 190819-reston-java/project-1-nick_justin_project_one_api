package com.revature.model;

public class UpdateReimbursement {

	private long id;
	private String status;
	
	public UpdateReimbursement() {
		super();
	}
	
	public UpdateReimbursement(long id, String status) {
		this.id = id;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UpdateReimbursement [id=" + id + ", status=" + status + "]";
	}
	
}
