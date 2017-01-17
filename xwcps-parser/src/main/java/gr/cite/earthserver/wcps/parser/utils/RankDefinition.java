package gr.cite.earthserver.wcps.parser.utils;

import gr.cite.earthserver.wcps.grammar.XWCPSParser.XpathClauseContext;

public class RankDefinition {
	
	public enum OrderDirection
	{
		Ascending,
		Descending,
	}
	
	public RankDefinition(XpathClauseContext xpathRankClause, OrderDirection direction) {
		this.direction = direction;
		this.xpathRankClause = xpathRankClause;
	}
	
	private OrderDirection direction;
	
	private XpathClauseContext xpathRankClause;

	public XpathClauseContext getXpathRankClause() {
		return xpathRankClause;
	}

	public void setXpathRankClause(XpathClauseContext xpathRankClause) {
		this.xpathRankClause = xpathRankClause;
	}

	public OrderDirection getDirection() {
		return direction;
	}

	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}
}
