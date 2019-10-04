package com.revature.model;

public class EmployeeId {

	private long id;
	
	public EmployeeId() {
		super();
	}
	
	public EmployeeId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "EmployeeId [id=" + id + "]";
	}
}
