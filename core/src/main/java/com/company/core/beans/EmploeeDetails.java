package com.company.core.beans;



public class EmploeeDetails implements Comparable<EmploeeDetails> {

	private String empname;
	private String empid;

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	@Override
	public String toString() {
		return "EmploeeDetails [empname=" + empname + ", empid=" + empid + "]";
	}

	@Override
	public int compareTo(EmploeeDetails o) {
		return this.empname.compareTo(o.getEmpname());
	}

}
