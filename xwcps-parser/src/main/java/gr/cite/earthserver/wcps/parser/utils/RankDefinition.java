package gr.cite.earthserver.wcps.parser.utils;

import gr.cite.earthserver.wcps.grammar.XWCPSParser.XpathClauseContext;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcs.core.Coverage;

import java.util.ArrayList;
import java.util.List;

public class RankDefinition {
	
	public enum OrderDirection
	{
		Ascending,
		Descending,
	}
	
	public RankDefinition(OrderDirection direction) {
		this.direction = direction;
	}
	
	private OrderDirection direction;
	
	private List<Query> rankingQueries;

	public List<Query> getRankingQueries() {
		if (this.rankingQueries == null)
			this.rankingQueries = new ArrayList<Query>();
		return this.rankingQueries;
	}

	public OrderDirection getDirection() {
		return direction;
	}

	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}
}
