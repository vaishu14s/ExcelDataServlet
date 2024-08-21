package com.model;

public class Employee {
	private int empID;

	private String name;
	private String skill;
	private String designation;
	private double salary;

//	public Employee(int empID, String name, String skill, String designation, double salary) {
//		super();
//		this.empID = empID;
//		this.name = name;
//		this.skill = skill;
//		this.designation = designation;
//		this.salary = salary;
//	}
//
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [empID=" + empID + ", name=" + name + ", skill=" + skill + ", designation=" + designation
				+ ", salary=" + salary + "]";
	}
}
