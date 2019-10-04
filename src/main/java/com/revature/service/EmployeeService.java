package com.revature.service;

import com.revature.repository.EmployeeDAO;

import java.util.List;

import com.revature.model.Employee;

public class EmployeeService {
	
	private EmployeeDAO employeeDAO;
	
	public EmployeeService(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	
	public List<Employee> getEmployees() {
		return employeeDAO.getEmployees();
	}
	
	public Employee getEmployee(String input) {
		try {
			// if input cannot be converted into long, it may not be an id
			return employeeDAO.getEmployee(Long.parseLong(input));
		} catch (NumberFormatException e) {
			// so, treat input as an username instead
			return employeeDAO.getEmployee(input);
		}
	}
	
	public boolean updateEmployee(Employee e) {
		return employeeDAO.updateEmployee(e);
	}
}
