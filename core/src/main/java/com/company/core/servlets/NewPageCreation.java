package com.company.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.core.models.NewPageDetails;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.google.gson.Gson;

@Component(service = Servlet.class, immediate = true, property = { "sling.servlet.methods=GET",
		"sling.servlet.paths=/api/pagecreation" })
public class NewPageCreation extends SlingSafeMethodsServlet {

	@Reference
	ResourceResolverFactory resourceresolverfactory;
	
	@Reference
	Replicator replicator;

	private static final long serialVersionUID = -2156941236407556682L;

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {

			Map<String, Object> pagemap = new HashMap<>();
			pagemap.put(ResourceResolverFactory.SUBSERVICE, "penasirsub");
			ResourceResolver resourceResolver;
			resourceResolver = resourceresolverfactory.getServiceResourceResolver(pagemap);
			Session session = resourceResolver.adaptTo(Session.class);
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			String jsonstr = "{\"pages\":[{\"parentpagepath\":\"/content/lg-fridge\",\"pagename\":\"lgfridgepage\",\"pagetemplate\":\"apps/lg/templates/homepage\",\"pagetitle\":\"LG page\"}]}";
			Gson gson = new Gson();
			NewPageDetails newPageDetails = gson.fromJson(jsonstr, NewPageDetails.class);
			List<NewPageDetails> pages = newPageDetails.getPages();
			if (null != pages && pages.size() > 0) {
				for (NewPageDetails newpage : pages) {
					String parentpagepath = newpage.getParentpagepath();
					String pagename = newpage.getPagename();
					String pagetemplate = newpage.getPagetemplate();
					String pagetitle = newpage.getPagetitle();
					Page newlycreatedpage = pageManager.create(parentpagepath, pagename, pagetemplate, pagetitle, true);
					getnewNode(newlycreatedpage, session);
					replicator.replicate(session, ReplicationActionType.ACTIVATE, newlycreatedpage.getPath());
					
				}
			}

		} catch (LoginException | WCMException | ReplicationException e) {
			logger.error(e.getMessage());
		}

	}

	public void getnewNode(Page newlycreatedpage, Session session) {
		try {
			Node pageJcrNode = (Node)session.getItem(newlycreatedpage.getPath()+"/jcr:content");
			Node addNode = pageJcrNode.addNode("homepage", "nt:unstructured");
			addNode.setProperty("jcr:title", "Homepage");
			session.save();
		} catch (RepositoryException e) {
			logger.debug(e.getMessage());
		}
		
		
	}
	

	

}
