package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.List;

//import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.metadata.core.PetascopeServer;
import gr.cite.earthserver.wcs.core.Coverage;

public class XpathForClause {
	private List<Coverage> coverages = new ArrayList<>();

	private String xpathQuery;

	public List<Coverage> getCoverages() {
		return coverages;
	}

	public XpathForClause setCoverages(List<Coverage> coverages) {
		this.coverages = coverages;
		return this;
	}

	public String getXpathQuery() {
		return xpathQuery;
	}

	public XpathForClause setXpathQuery(String xpathQuery) {
		this.xpathQuery = xpathQuery;
		return this;
	}

	public XpathForClause aggregate(XpathForClause aggregate) {
		if (xpathQuery == null) {
			xpathQuery = aggregate.getXpathQuery();
		} else if (aggregate.getXpathQuery() != null) {
			xpathQuery += aggregate.getXpathQuery();
		}
		
		coverages.addAll(aggregate.getCoverages());
		
		return this;
	}

}
