package com.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.SqlConection;
import com.jdbc.*;

/**
 * Servlet implementation class EmployeeServlet
 */
//@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SqlConection sqlConnection;

	public EmployeeServlet() {
		super();
	}

	public void init() throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("Init method called from EmployeeServlet.");
		super.init();
		try {
			sqlConnection = new SqlConection();
			sqlConnection.testConnection(); // Ensure this method works correctly
		} catch (Exception e) {
			throw new ServletException("Failed to initialize database connection.", e);
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println("Action: " + action);
		System.out.println("EmpID: " + request.getParameter("id"));
		try {
			if (action == null || action.isEmpty()) {
				listEmployees(request, response);
			} else {
				switch (action) {
				case "view":
					viewEmployee(request, response);
					break;
				case "update":
					showUpdateForm(request, response);
					break;
				 case "exportToExcel":
	                    // Redirect to export servlet
	                    response.sendRedirect("ExportExcelServlet");
	                    break;
				 case "importToExcel":
	                    // Redirect to export servlet
	                    response.sendRedirect("ImportExcelServlet");
	                    break;
				default:
					listEmployees(request, response);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("Database error: " + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException("Class not found: " + e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("An unexpected error occurred: " + e.getMessage(), e);
		}
	}

	private void listEmployees(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException, ClassNotFoundException {
		List<Employee> employees = sqlConnection.getEmployee();
		request.setAttribute("employeeList", employees);
		RequestDispatcher dispatcher = request.getRequestDispatcher("employee.jsp");
		dispatcher.forward(request, response);
	}

	private void viewEmployee(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException, ClassNotFoundException {
		String empIDParam = request.getParameter("id");
		if (empIDParam == null || empIDParam.isEmpty()) {
			throw new ServletException("Employee ID is missing or invalid.");
		}

		int empID = Integer.parseInt(empIDParam);
		System.out.println("Fetching details for Employee ID: " + empID);
		Employee employee = sqlConnection.getEmployeeById(empID);
		if (employee == null) {
			throw new ServletException("Employee not found for ID: " + empID);
		}
		System.out.println("Employee Details: " + employee);
		request.setAttribute("employee", employee);
		RequestDispatcher dispatcher = request.getRequestDispatcher("EmployeeDetails.jsp");
		dispatcher.forward(request, response);
	}

	private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException, ClassNotFoundException {
		String empIDParam = request.getParameter("id");
		if (empIDParam == null || empIDParam.isEmpty()) {
			throw new ServletException("Employee ID is missing or invalid.");
		}

		int empID = Integer.parseInt(empIDParam);
		System.out.println("Fetching details for Employee ID: " + empID);
		Employee employee = sqlConnection.getEmployeeById(empID);
		if (employee == null) {
			throw new ServletException("Employee not found for ID: " + empID);
		}
		System.out.println("Employee Details: " + employee);
		request.setAttribute("employee", employee);
		RequestDispatcher dispatcher = request.getRequestDispatcher("updateEmployee.jsp");
		dispatcher.forward(request, response);
	}

	private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException, ClassNotFoundException {
		try {
			String empIDParam = request.getParameter("empID");
			String name = request.getParameter("name");
			String skill = request.getParameter("skill");
			String designation = request.getParameter("designation");
			String salaryStr = request.getParameter("salary");

			// Check if parameters are missing
			if (empIDParam == null || empIDParam.isEmpty() || name == null || name.isEmpty() || skill == null
					|| skill.isEmpty() || designation == null || designation.isEmpty() || salaryStr == null
					|| salaryStr.isEmpty())

			{

				System.out.println("EmpID Param: " + empIDParam);
				System.out.println("Name: " + name);
				System.out.println("Skill: " + skill);
				System.out.println("Designation: " + designation);
				System.out.println("Salary String: " + salaryStr);
				throw new ServletException("One or more fields are missing or invalid.");
			}

			int empID = Integer.parseInt(empIDParam);
			double salary = Double.parseDouble(salaryStr);

			System.out.println("Updating Employee - ID: " + empID + ", Name: " + name + ", Skill: " + skill
					+ ", Designation: " + designation + ", Salary: " + salary);

			Employee employee = new Employee();
			employee.setEmpID(empID);
			employee.setName(name);
			employee.setSkill(skill);
			employee.setDesignation(designation);
			employee.setSalary(salary);

			sqlConnection.updateEmployee(employee);
			response.sendRedirect("EmployeeServlet");
		} catch (NumberFormatException e) {
			throw new ServletException("Invalid number format.", e);
		} catch (Exception e) {
			throw new ServletException("An unexpected error occurred.", e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		 if ("update".equals(action)) {
	            try {
	                updateEmployee(request, response);
	            } catch (SQLException | ClassNotFoundException e) {
	                throw new ServletException(e);
	            }
	        } else {
	            doGet(request, response);
	        }
	}
}
