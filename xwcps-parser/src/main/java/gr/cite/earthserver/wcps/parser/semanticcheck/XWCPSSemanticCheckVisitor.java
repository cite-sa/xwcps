package gr.cite.earthserver.wcps.parser.semanticcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.CoverageVariableNameLabelContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.WcpsQueryContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.XmlClauseContext;
import gr.cite.earthserver.wcps.parser.errors.SemanticError;

public class XWCPSSemanticCheckVisitor extends XWCPSBaseVisitor<Aggregator> {
	private static final Logger logger = LoggerFactory.getLogger(XWCPSSemanticCheckVisitor.class);

	private SemanticSyntaxCheckResults checkResults;
	
	public XWCPSSemanticCheckVisitor() {
		checkResults = new SemanticSyntaxCheckResults();
	}
	
	public SemanticSyntaxCheckResults getCheckResults() {
		return checkResults;
	}
	
	
	@Override
	protected Aggregator aggregateResult(Aggregator aggregate, Aggregator nextResult) {
		if (nextResult == null) {
			return aggregate;
		} else if (aggregate == null) {
			return nextResult;
		} else  {
			return aggregate.addAll(nextResult);
		}
	}
	
	@Override
	public Aggregator visitWcpsQuery(WcpsQueryContext ctx) {
		Aggregator forAggregator = visit(ctx.forClauseList());
		Aggregator returnAggregator = visit(ctx.returnClause());
		
		if (!forAggregator.containsAll(returnAggregator)) {
			checkResults.addError(SemanticError.VARIABLE_NOT_DEFINED);
		}
		
		return null;
	}
	
	@Override
	public Aggregator visitCoverageVariableNameLabel(CoverageVariableNameLabelContext ctx) {
		Aggregator aggregator = new Aggregator();
		aggregator.add(ctx);
		return aggregator;
	}
	
	@Override
	public Aggregator visitXmlClause(XmlClauseContext ctx) {
		if (!getTagName(ctx.openXmlElement().getText()).equals(getTagName(ctx.closeXmlElement().getText()))) {
			checkResults.addError(SemanticError.XML_ELEMENTS);
		}
		return super.visitXmlClause(ctx);
	}
	
	private String getTagName(String tag) {
		String untagged = null;
		if (tag.startsWith("</")) {
			untagged = tag.substring(tag.indexOf("/") + 1, tag.indexOf(">"));
		} else if (tag.startsWith("<")) {
			untagged = tag.substring(tag.indexOf("<") + 1, tag.indexOf(">"));
		}
		return untagged;
	}
}
