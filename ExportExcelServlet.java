package com.model;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jdbc.SqlConection;

@WebServlet("/ExportExcelServlet")
public class ExportExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SqlConection sqlConnection;

	
	public ExportExcelServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		System.out.println("Init method called from ExportExcelServlet");
		super.init();
		try {
			sqlConnection = new SqlConection();
			sqlConnection.testConnection(); // Ensure this method works correctly
		} catch (Exception e) {
			throw new ServletException("Failed to initialize database connection.", e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
            List<Employee> employees = sqlConnection.getEmployee();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Employees");

            // Create header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("EmpID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Skill");
            header.createCell(3).setCellValue("Designation");
            header.createCell(4).setCellValue("Salary");

            // Create data rows
            int rowNum = 1;
            for (Employee emp : employees) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emp.getEmpID());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getSkill());
                row.createCell(3).setCellValue(emp.getDesignation());
                row.createCell(4).setCellValue(emp.getSalary());
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
	}

	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		String action = request.getParameter("action");
//		if ("exportToExcel".equals(action)) {
//			try {
//				exportToExcel(response);
//			} catch (ClassNotFoundException | SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ServletException("Database error: " + e.getMessage(), e);
//			}
//		}
//		//doGet(request, response);
//	}
//
//	public void exportToExcel(HttpServletResponse response) throws ClassNotFoundException, SQLException {
//		List<Employee> employees = sqlConnection.getEmployee();
//
//		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//		response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");
//		
//		Workbook workbook=new XSSFWorkbook();
//		Sheet sheet=workbook.createSheet("Employees");
//		
//		Row header=sheet.createRow(0);
//		header.createCell(0).setCellValue("EmpID");
//		header.createCell(1).setCellValue("Name");
//		header.createCell(2).setCellValue("Skill");
//		header.createCell(3).setCellValue("Designation");
//		header.createCell(4).setCellValue("Salary");
//		
//		int rowNum=1;
//		for(Employee emp:employees) {
//			Row row=sheet.createRow(rowNum++);
//			row.createCell(0).setCellValue(emp.getEmpID());
//			row.createCell(1).setCellValue(emp.getName());
//			row.createCell(2).setCellValue(emp.getSkill());
//			row.createCell(3).setCellValue(emp.getDesignation());
//			row.createCell(4).setCellValue(emp.getSalary());
//			
//			try {
//				OutputStream out=response.getOutputStream();
//				workbook.write(out);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println(e.getMessage());
//			}
//		}
//	}

}
