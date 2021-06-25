package com.company.core.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Employee {
	
	public static void main(String[] args) {
		EmploeeDetails em1 = new EmploeeDetails();
		em1.setEmpname("nino");
		em1.setEmpid("1");
		EmploeeDetails em2 = new EmploeeDetails();
		em2.setEmpname("ravi");
		em2.setEmpid("3");
		EmploeeDetails  em3 = new EmploeeDetails();
		em3.setEmpname("babu");
		em3.setEmpid("2");
		List<EmploeeDetails> newlist = new ArrayList<>();
		newlist.add(em1);
		newlist.add(em2);
		newlist.add(em3);
		Collections.sort(newlist,new EmpComparator());
		System.out.println(newlist);
}
}