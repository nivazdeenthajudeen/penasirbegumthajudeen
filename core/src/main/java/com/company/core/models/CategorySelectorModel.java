package com.company.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CategorySelectorModel {
	
	@ScriptVariable
	ResourceResolver resolver;
	
	@ValueMapValue
	String parentpagepath;
	
	List<TagInfo> categoryTags = new ArrayList<>();
	
	List<Page> resultPages = new ArrayList<>();
	
	TagManager tagmanager;
	
	@ValueMapValue
	String tagbasepath;
	
	@PostConstruct
	public void init() {
		tagmanager = resolver.adaptTo(TagManager.class);
		Resource tagBaseRes = resolver.getResource(tagbasepath);
		setTagList(tagBaseRes);
		if(categoryTags.size() > 0) {
			
		}		
	}

	private void setTagList(Resource tagBaseRes) {
		Iterator<Resource> tagBaseReslistChildren = tagBaseRes.listChildren();
		while(tagBaseReslistChildren.hasNext()) {
			Resource tagRes = tagBaseReslistChildren.next();
			Tag categoryTag = tagmanager.resolve(tagRes.getPath());
			TagInfo tinfo = new TagInfo();
			tinfo.setTagName(categoryTag.getName());
			tinfo.setTagtitle(categoryTag.getTitle());
			tinfo.setTagId(categoryTag.getTagID());
			categoryTags.add(tinfo);
		}
	}

	public List<TagInfo> getCategoryTags() {
		return categoryTags;
	}
	
	private void setResultPages() {
		
	}
	
	public List<Page> getResultPages(){
		return resultPages;
	}
	
}
