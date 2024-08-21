package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.model.Employee;

public class SqlConection {

	// Database connection parameters
	private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/mydatabase";
	private static final String JDBC_USER = "postgres";
	private static final String JDBC_PASSWORD = "postgres";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// Test the database connection
		SqlConection connection = new SqlConection();
		connection.testConnection();
	}

	// Method to test the database connection
	public boolean testConnection() {
		try {
			Connection con = getMySqlConnection();
			if (con != null) {
				System.out.println("Database connected successfully!");
				con.close();
				return true;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Failed to connect to the database.");
		return false;
	}

	public List<Employee> getEmployee() throws SQLException, ClassNotFoundException {
		Connection con = getMySqlConnection();
		Statement stmt = con.createStatement();
		String sql = "select * from resource order by empid";
		ResultSet rs = stmt.executeQuery(sql);
		List<Employee> employees = new ArrayList<>();

		while (rs.next()) {
			Employee employee = new Employee();
			// Retrieve by column name
			employee.setEmpID(rs.getInt("empid"));
			employee.setName(rs.getString("name"));
			employee.setSkill(rs.getString("skill"));
			employee.setDesignation(rs.getString("designation"));
			employee.setSalary(rs.getInt("salary"));
			employees.add(employee);

		}
		stmt.close();
		System.out.println(employees);
		rs.close();
		con.close();
		return employees;
	}

	public List<String> getSkills() throws SQLException, ClassNotFoundException {
		List<String> skills = new ArrayList<>();
		String sql = "select skill from resource"; // Adjust the query to match your database schema
		try (Connection conn = getMySqlConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				skills.add(rs.getString("skill"));
			}
		}
		return skills;
	}

	public List<String> getDesignations() throws SQLException, ClassNotFoundException {
		List<String> designations = new ArrayList<>();
		String sql = "SELECT designation FROM resource "; // Adjust the query to match your database schema
		try (Connection conn = getMySqlConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				designations.add(rs.getString("designation"));
			}
		}
		return designations;
	}

	public void updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
	    String sql = "UPDATE resource SET name = ?, skill = ?, designation = ?, salary = ? WHERE empid = ?";
	    try (Connection conn = getMySqlConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
	        statement.setString(1, employee.getName());
	        statement.setString(2, employee.getSkill());
	        statement.setString(3, employee.getDesignation());
	        statement.setDouble(4, employee.getSalary());
	        statement.setInt(5, employee.getEmpID());

	        int rowsUpdated = statement.executeUpdate();
	        if (rowsUpdated > 0) {
	            System.out.println("An existing employee was updated successfully!");
	        } else {
	            System.out.println("No employee found with ID: " + employee.getEmpID());
	        }

	        // Commit the transaction if auto-commit is disabled
	        if (!conn.getAutoCommit()) {
	            conn.commit();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }
	}


	private static Connection getMySqlConnection() throws SQLException, ClassNotFoundException {

		// Import driver like this and throw ClassNotFoundException;
		Class.forName("org.postgresql.Driver");

		// To connect we need to put database name, localhost, password;
		// Connection con =
		// DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydatabase",
		// "postgres",
		// "postgres");

		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	public Employee getEmployeeById(int empID) throws SQLException, ClassNotFoundException {
		Connection conn = getMySqlConnection();
		String sql = "select * from resource where empid = ? order by empid";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, empID);
		ResultSet rs = pstmt.executeQuery();

		Employee employee = null;
		// Extract data from result set
		if (rs.next()) {
			// Retrieve by column name
			employee = new Employee();
			employee.setEmpID(rs.getInt("empid"));
			employee.setName(rs.getString("name"));
			employee.setSkill(rs.getString("skill"));
			employee.setDesignation(rs.getString("designation"));
			employee.setSalary(rs.getInt("salary"));

		}
		rs.close();
		pstmt.close();
		conn.close();

		return employee;
	}
}
