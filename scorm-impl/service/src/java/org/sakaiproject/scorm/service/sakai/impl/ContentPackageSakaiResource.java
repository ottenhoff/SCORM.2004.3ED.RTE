package org.sakaiproject.scorm.service.sakai.impl;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.scorm.exceptions.ResourceNotFoundException;
import org.sakaiproject.scorm.model.api.ContentPackageResource;

public class ContentPackageSakaiResource extends ContentPackageResource {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(ContentPackageSakaiResource.class);
	
	String contentResourceId;
	String mimeType;
	
	public ContentPackageSakaiResource(String path, String contentResourceId, long contentLength, String mimeType) {
		super(path);
		this.contentResourceId = contentResourceId;
		this.mimeType = mimeType;
		this.setLength(contentLength);
	}

	public ContentPackageSakaiResource(String path, ContentResource contentResource) {
		this(path, contentResource.getId(), contentResource.getContentLength(), contentResource.getContentType());
	}

	
	@Override
	public InputStream getInputStream() throws ResourceNotFoundException {
		try {
			ContentResource resource = ContentHostingService.getResource(contentResourceId);
			return resource.streamContent();
		} catch (IdUnusedException e) {
			log.error("Could not stream content from this path: " + getPath(), e);
			throw new ResourceNotFoundException(getPath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}
}
