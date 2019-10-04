package com.revature.mock;

import com.revature.model.Employee;
import com.revature.repository.EmployeeDAO;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeDaoMock implements EmployeeDAO{
	
	private static long id;
	private static String username;
	private static Employee e;
	
	@BeforeClass
	public static void setUpEmployeeDaoMock() {
		id = 1;
		username = "Chan123";
		e = new Employee(id, username, "123", "Chan", "Nguyen", "chan123@email.com", "1234 California St.", "employee");
	}

	@Override
	public Employee getEmployee(long id) {
		if (id == 1L) {
			return new Employee(id, "Chan123", "123", "Chan", "Nguyen", "chan123@email.com", "1234 California St.", "employee");
		} else {
			return null;
		}
	}
	
	@Test
	public void testGetEmployeeWithId() {
		this.getEmployee(id);
	}

	@Override
	public Employee getEmployee(String username) {
		if (username.equals("Chan123")) {
			return new Employee(1, username, "123", "Chan", "Nguyen", "chan123@email.com", "1234 California St.", "employee");
		} else {
			return null;
		}
	}
	
	@Test
	public void testGetEmployeeWithUsername() {
		this.getEmployee(username);
	}

	@Override
	public List<Employee> getEmployees() {
		List<Employee> mockList = new ArrayList<Employee>();
		mockList.add(e);
		mockList.add(new Employee(2, "CJ123", "123", "ChrisJoy", "Manansala", "CJ123@email.com", "1234 Nurse St.", "manager"));
		return mockList;
	}
	
	@Test
	public void testGetEmployees() {
		this.getEmployees();
	}

	@Override
	public boolean updateEmployee(Employee e) {
		Employee updatedE = new Employee(
				e.getId(),
				e.getUsername(),
				e.getPassword(),
				e.getFirstName(),
				e.getLastName(),
				e.getEmail(),
				"1234 Mulholland Drive",
				"manager");
		return true;
	}
	
	@Test
	public void testUpdateEmployee() {
		this.updateEmployee(e);
	}
	
	@AfterClass
	public static void tearDownEmployeeDaoMock() {
		username = null;
		e = null;
	}
}
