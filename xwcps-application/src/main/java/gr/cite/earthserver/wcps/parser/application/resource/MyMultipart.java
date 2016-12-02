package gr.cite.earthserver.wcps.parser.application.resource;

import org.glassfish.jersey.media.multipart.MultiPart;

public class MyMultipart extends MultiPart {
	
	public MyMultipart() {
		super();
	}
	@Override
	public Object getEntity() {
		return this.getBodyParts();
	}
}
