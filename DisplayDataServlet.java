package com.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdbc.SqlConection;

/**
 * Servlet implementation class DisplayDataServlet
 */
@WebServlet("/DisplayDataServlet")
public class DisplayDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SqlConection sqlConnection;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayDataServlet() {
        super();
        // TODO Auto-generated constructor stub
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
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String JDBC_URL = "jdbc:postgresql://localhost:5432/mydatabase";
        String JDBC_USER = "postgres";
        String JDBC_PASSWORD = "postgres";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM resource order by empid";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            out.println("<html><body>");
            out.println("<h2>Employee Data</h2>");
            out.println("<table border='1'><tr><th>EmpID</th><th>Name</th><th>Skill</th><th>Designation</th><th>Salary</th></tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getInt("empid") + "</td>");
                out.println("<td>" + resultSet.getString("name") + "</td>");
                out.println("<td>" + resultSet.getString("skill") + "</td>");
                out.println("<td>" + resultSet.getString("designation") + "</td>");
                out.println("<td>" + resultSet.getDouble("salary") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
