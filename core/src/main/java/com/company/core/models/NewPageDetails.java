package com.company.core.models;

import java.util.List;

public class NewPageDetails {
	private List<NewPageDetails> pages ;
	
	private String parentpagepath;
	private String pagename;
	private String pagetemplate;
	private String pagetitle;
	public List<NewPageDetails> getPages() {
		return pages;
	}
	public void setPages(List<NewPageDetails> pages) {
		this.pages = pages;
	}
	public String getParentpagepath() {
		return parentpagepath;
	}
	public void setParentpagepath(String parentpagepath) {
		this.parentpagepath = parentpagepath;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public String getPagetemplate() {
		return pagetemplate;
	}
	public void setPagetemplate(String pagetemplate) {
		this.pagetemplate = pagetemplate;
	}
	public String getPagetitle() {
		return pagetitle;
	}
	public void setPagetitle(String pagetitle) {
		this.pagetitle = pagetitle;
	}


}
