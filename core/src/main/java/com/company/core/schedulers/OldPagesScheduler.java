package com.company.core.schedulers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.core.configuration.OldPagesConfig;
import com.day.cq.commons.mail.MailTemplate;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = OldPagesConfig.class)
public class OldPagesScheduler implements Runnable {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Reference
	Scheduler scheduler;

	@Reference
	ResourceResolverFactory resourceresolverfactory;

	@Reference
	MessageGatewayService messagegateway;

	OldPagesConfig pageconfig;

	@Activate
	public void activatemethod(OldPagesConfig config) {
		pageconfig = config;
		if (pageconfig.schedulerEnabled().equalsIgnoreCase("true")) {
			ScheduleOptions sopts = scheduler.EXPR(pageconfig.schedulerExpression());
			sopts.canRunConcurrently(false);
			sopts.name(pageconfig.schedulerName());
			//scheduler.schedule(this, sopts);
			logger.debug(pageconfig.schedulerName());
		}
	}

	@Override
	public void run() {
		try {
			logger.debug("Running scheduler");
			Map<String, Object> servicemap = new HashMap<>();
			servicemap.put(ResourceResolverFactory.SUBSERVICE, "penasirsub");
			ResourceResolver serviceResourceResolver = resourceresolverfactory.getServiceResourceResolver(servicemap);
			Session session = serviceResourceResolver.adaptTo(Session.class);
			QueryBuilder queryBuilder = serviceResourceResolver.adaptTo(QueryBuilder.class);
			List<Hit> queryOutput = getQuerypage(queryBuilder, session);
			DataSource ds = getAttachment(queryOutput);
			getMailattach(ds, session);
		} catch (LoginException e) {
			logger.error(e.getMessage());
			;
		}
	}

	public List<Hit> getQuerypage(QueryBuilder queryBuilder, Session session) {
		List<Hit> hits = new ArrayList<>();
		Map<String, Object> querymap = new HashMap<>();
		querymap.put("path", "/content/lg-fridge");
		Query query = queryBuilder.createQuery(PredicateGroup.create(querymap), session);
		SearchResult Result = query.getResult();
		hits = Result.getHits();
		return hits;
	}

	public void getMailattach(DataSource ds, Session session) {
		try {
			MailTemplate mailTemplate = MailTemplate.create("/apps/lg/mailtemplate/template.txt", session);
			Map<String, Object> lookmap = new HashMap<>();
			lookmap.put("firstname", "ninos");
			StrLookup strLookup = StrLookup.mapLookup(lookmap);
			HtmlEmail email = mailTemplate.getEmail(strLookup, HtmlEmail.class);
			List<InternetAddress> tolist = new ArrayList<>();
			tolist.add(new InternetAddress("javatamilanchennai@gmail.com"));
			email.setTo(tolist);
			email.setFrom("javatamilanchennai@gmail.com");
			email.attach(ds, "begum.xlsx", "h hello");
			MessageGateway<HtmlEmail> gateway = messagegateway.getGateway(HtmlEmail.class);
			gateway.send(email);

		} catch (IOException | MessagingException | EmailException e) {
			logger.error(e.getMessage());
			;
		}
	}

	public DataSource getAttachment(List<Hit> queryOutput) {
		DataSource ds = null;
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet();
			Row firstrow = sheet.createRow(0);
			Cell firstcell = firstrow.createCell(0);
			firstcell.setCellValue("page title");
			Cell secondcell = firstrow.createCell(1);
			secondcell.setCellValue("page paths");
			for (int i = 1; i < queryOutput.size(); i++) {
				Row frow = sheet.createRow(i + 1);
				Cell fcell = frow.createCell(0);
				fcell.setCellValue(queryOutput.get(i).getTitle());
				Cell scell = frow.createCell(1);
				scell.setCellValue(queryOutput.get(i).getPath());
			}
			FileOutputStream stream = new FileOutputStream("penasir.xlsx");
			wb.write(stream);;
			wb.close();
			stream.close();
			ds = new FileDataSource("penasir.xlsx");
		} catch (RepositoryException e) {
			logger.error(e.getMessage());
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return ds;
	}

	@Deactivate
	public void deactivatemethod() {
		scheduler.unschedule(pageconfig.schedulerName());
		logger.debug("deactivate methood");
	}

}
