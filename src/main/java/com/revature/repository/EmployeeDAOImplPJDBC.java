package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.model.Employee;
import com.revature.utilities.ConnectionUtil;
import com.revature.utilities.StreamCloser;

public class EmployeeDAOImplPJDBC implements EmployeeDAO {

	public Employee getEmployee(long id) {
		Employee employee = null;
		final String query = "SELECT * FROM employee WHERE id = ?;";
		
		try (Connection conn = ConnectionUtil.getConnection()){
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setLong(1, id);
				if (stmt.execute()) {
					try (ResultSet resultSet = stmt.getResultSet()) {
						if (resultSet.next()) {
							employee = createEmployeeFromResultSet(resultSet);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employee;
	}

	private Employee createEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
		return new Employee(
				resultSet.getLong("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getString("first_name"),
				resultSet.getString("last_name"),
				resultSet.getString("email"),
				resultSet.getString("address"),
				resultSet.getString("company_position")
				);
	}

	public Employee getEmployee(String username) {
		Employee employee = null;
		final String query = "SELECT * FROM employee WHERE username = ?;";
		
		try (Connection conn = ConnectionUtil.getConnection()){
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, username);
				if (stmt.execute()) {
					try (ResultSet resultSet = stmt.getResultSet()) {
						if (resultSet.next()) {
							employee = createEmployeeFromResultSet(resultSet);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employee;
	}

	public List<Employee> getEmployees() {
		// Statement Interface
		Statement statement = null;
		ResultSet resultSet = null;
		Connection conn = null;
		
		List<Employee> employees = new ArrayList<Employee>();
		
		try {
			conn = ConnectionUtil.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM employee;");
			
			while (resultSet.next()) {
				employees.add(createEmployeeFromResultSet(resultSet));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			StreamCloser.close(resultSet);
			StreamCloser.close(statement);
			StreamCloser.close(conn);
		}
		return employees;
	}

	public boolean updateEmployee(Employee e) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String query = "UPDATE employee SET username=?, password=?, first_name=?, last_name=?, email=?, address=?, company_position=? WHERE id=?;";
		
		try {
			conn = ConnectionUtil.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setString(1, e.getUsername());
			stmt.setString(2, e.getPassword());
			stmt.setString(3, e.getFirstName());
			stmt.setString(4, e.getLastName());
			stmt.setString(5, e.getEmail());
			stmt.setString(6, e.getAddress());
			stmt.setString(7, e.getCompanyPosition());
			stmt.setLong(8, e.getId());
			stmt.execute();		
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			StreamCloser.close(stmt);
			StreamCloser.close(conn);
		}
		
		return true;
	}
}