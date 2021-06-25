package com.company.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
@Component(service= Servlet.class,immediate = true,property = {
		"sling.servlet.methods=GET",
		"sling.servlet.paths=/api/nodepage"
})
public class NodePageCreation extends SlingAllMethodsServlet {

	
	private static final long serialVersionUID = -8885831950928835898L;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
            
	}

}
