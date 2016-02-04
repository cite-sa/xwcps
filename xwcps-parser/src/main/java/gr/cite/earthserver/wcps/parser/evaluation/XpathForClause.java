package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.ArrayList;
import java.util.List;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.metadata.core.PetascopeServer;

public class XpathForClause {
	private List<Coverage> coverages = new ArrayList<>();

	private List<PetascopeServer> servers = new ArrayList<>();

	private String xpathQuery;

	public List<Coverage> getCoverages() {
		return coverages;
	}

	public XpathForClause setCoverages(List<Coverage> coverages) {
		this.coverages = coverages;
		return this;
	}

	public List<PetascopeServer> getServers() {
		return servers;
	}

	public XpathForClause setServers(List<PetascopeServer> servers) {
		this.servers = servers;
		return this;
	}

	public String getXpathQuery() {
		return xpathQuery;
	}

	public XpathForClause setXpathQuery(String xpathQuery) {
		this.xpathQuery = xpathQuery;
		return this;
	}

}
