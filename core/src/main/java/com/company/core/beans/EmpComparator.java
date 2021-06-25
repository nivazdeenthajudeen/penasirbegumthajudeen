package com.company.core.beans;

import java.util.Comparator;

public class EmpComparator implements Comparator<EmploeeDetails>{

	@Override
	public int compare(EmploeeDetails o1, EmploeeDetails o2) {
		
		return o1.getEmpname().compareTo(o2.getEmpname());
	}

}
