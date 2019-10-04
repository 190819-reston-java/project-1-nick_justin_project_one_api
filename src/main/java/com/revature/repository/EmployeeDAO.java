package com.revature.repository;

import java.util.List;

import com.revature.model.Employee;

public interface EmployeeDAO {
	Employee getEmployee(long id);
	Employee getEmployee(String username); //get reimbursements from a specific employee
	List<Employee> getEmployees(); //view all employees
	boolean updateEmployee(Employee e);
}