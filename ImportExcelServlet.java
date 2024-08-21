package com.upload;

import com.model.Employee;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//@WebServlet("/ImportExcelServlet")
@MultipartConfig
public class ImportExcelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ImportExcelServlet() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        request.getRequestDispatcher("/Upload.jsp").forward(request, response);

		//super.doGet(req, resp);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        List<Employee> employees = new ArrayList<>();

        if (filePart != null) {
            try (InputStream inputStream = filePart.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) { // Skip header row
                        continue;
                    }
                    int empID = (int) row.getCell(0).getNumericCellValue();
                    String name = row.getCell(1).toString();
                    String skill = row.getCell(2).toString();
                    String designation = row.getCell(3).toString();
                    double salary = row.getCell(4).getNumericCellValue();

                    Employee employee = new Employee();
                    employee.setEmpID(empID);
                    employee.setName(name);
                    employee.setSkill(skill);
                    employee.setDesignation(designation);
                    employee.setSalary(salary);

                    employees.add(employee);
                    updateDatabase(empID, name, skill, designation, salary);
                }
            }
        }

        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/Upload.jsp").forward(request, response);
        System.out.println("File part: " + filePart);
        System.out.println("Number of employees: " + employees.size());

    }

    private void updateDatabase(int empID, String name, String skill, String designation, double salary) {
        String JDBC_URL = "jdbc:postgresql://localhost:5432/mydatabase";
        String JDBC_USER = "postgres";
        String JDBC_PASSWORD = "postgres";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "UPDATE resource SET name = ?, skill = ?, designation = ?, salary = ? WHERE empid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, skill);
                statement.setString(3, designation);
                statement.setDouble(4, salary);
                statement.setInt(5, empID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
