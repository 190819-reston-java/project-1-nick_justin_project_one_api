package com.revature.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.utilities.ConnectionUtil;
import com.revature.utilities.ImageUtil;

public class ReimbursementDAOImplPJDBC implements ReimbursementDAO {

	@Override
	public Reimbursement getReimbursement(long id) {
		Reimbursement reimbursement = null;
		final String query = "SELECT * FROM reimbursement WHERE id = ?;";

		try (Connection conn = ConnectionUtil.getConnection()) {
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setLong(1, id);
				if (stmt.execute()) {
					try (ResultSet resultSet = stmt.getResultSet()) {
						if (resultSet.next()) {
							try {
							reimbursement = createReimbursementFromResultSet(resultSet);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reimbursement;
	}

	private Reimbursement createReimbursementFromResultSet(ResultSet resultSet)  {

		try {	
			
			return new Reimbursement (
					resultSet.getLong("id"),
					resultSet.getLong("employee_id"),
					resultSet.getDouble("amount"),
					resultSet.getDate("date_recorded"),
					resultSet.getString("status"),
					resultSet.getString("approving_manager")
					);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public byte[] getImage(Reimbursement r) {
		byte[] byteImage = null;
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT receipt FROM reimbursement WHERE id = ?;");
			stmt.setLong(1, r.getId());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				byteImage = resultSet.getBytes(1);
			}
			
			resultSet.close();
			stmt.close();
			return byteImage;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public void addImage(byte[] img, Reimbursement r) {

		  Connection conn = null;
		
		  try {
		
		   conn = ConnectionUtil.getConnection();
		   PreparedStatement ps = conn.prepareStatement("UPDATE reimbursement SET receipt = ? WHERE id = ?");
		   ps.setBytes(1, img);
		   ps.setLong(2, r.getId());
		   ps.executeUpdate();
		   ps.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  } finally {
		   try {
		    conn.close();
		 
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	  }

	}

	@Override
	public List<Reimbursement> getAllReimbursements() {
		Statement statement = null;
		ResultSet resultSet = null;
		Connection conn = null;

		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		
		try {
			conn = ConnectionUtil.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM reimbursement;");
			while (resultSet.next()) {
				
				reimbursements.add(createReimbursementFromResultSet(resultSet));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reimbursements;
	}

	@Override
	public List<Reimbursement> getAllReimbursementsForEmployee(Employee emp) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection conn = null;
		List<Reimbursement> reimbursements = new ArrayList<Reimbursement>();
		
		try {
			conn = ConnectionUtil.getConnection();
			statement = conn.prepareStatement("SELECT * FROM reimbursement WHERE employee_id = ?;");
			statement.setLong(1, emp.getId());
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				
				reimbursements.add(createReimbursementFromResultSet(resultSet));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reimbursements;
	}

	@Override
	public boolean createReimbursement(Reimbursement r) {
		Connection conn = null;
		PreparedStatement stmt = null;		
		
		String query = "INSERT INTO reimbursement VALUES (DEFAULT,?,?,?,?,?);";
		
		try {
			
			conn = ConnectionUtil.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setLong(1, r.getEmployeeId());
			stmt.setDouble(2, r.getAmount());
			stmt.setDate(3, r.getSqlDate());
			stmt.setString(4, r.getStatus());
			stmt.setString(5, r.getApprovingManager());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	@Override
	public boolean updateReimbursement(Reimbursement r) {
		Connection conn = null;
		PreparedStatement stmt = null;		
		
		String query = "UPDATE reimbursement SET employee_id=?, amount=?,"
				+ " date_recorded=?, status=?, approving_manager=? WHERE id=?";
		
		try {
			conn = ConnectionUtil.getConnection();
			stmt = conn.prepareStatement(query);
			stmt.setLong(1, r.getEmployeeId());
			stmt.setDouble(2, r.getAmount());
			stmt.setDate(3, r.getSqlDate());
			stmt.setString(4, r.getStatus());
			stmt.setString(5, r.getApprovingManager());
			stmt.setLong(6, r.getId());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}

}