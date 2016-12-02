package gr.cite.earthserver.wcps.parser;

import javax.inject.Inject;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcps.parser.evaluation.Query;
import gr.cite.earthserver.wcps.parser.evaluation.visitors.XWCPSEvalVisitor;
import gr.cite.earthserver.wcs.adapter.api.WCSAdapterAPI;

public class XWCPSQueryParser {

//	private static final String WCS_ENDPOINT = "http://flanche.com:9090/rasdaman/ows";

//	private static final String WCS_ENDPOINT = "http://access.planetserver.eu:8080/rasdaman/ows";
	
	private WCSAdapterAPI wcsAdapter;
	
	@Inject
	public XWCPSQueryParser(WCSAdapterAPI wcsAdapter) {
		this.wcsAdapter = wcsAdapter;
	}

	public Query parse(String query) {
		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		XWCPSParser parser = new XWCPSParser(tokenStream);

		ParseTree tree = parser.xwcps();

		// System.out.println(tokenStream.getTokens().stream().map(token -> {
		// String channelStr = "";
		// String txt = token.getText();
		// if (txt != null) {
		// txt = txt.replace("\n", "\\n");
		// txt = txt.replace("\r", "\\r");
		// txt = txt.replace("\t", "\\t");
		// } else {
		// txt = "<no text>";
		// }
		// return "[@" + token.getTokenIndex() + "," + token.getStartIndex() +
		// ":" + token.getStopIndex() + "='" + txt
		// + "',<" + (token.getType() >= 0 ?
		// XWCPSLexer.ruleNames[token.getType() - 1] : "-1") + ">"
		// + channelStr + "," + token.getLine() + ":" +
		// token.getCharPositionInLine() + "]";
		// }).collect(Collectors.toList()).toString());

		// System.out.println(tokenStream.getTokens());
		// System.out.println(tree.toStringTree(parser));

		//XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(XWCPSQueryParser.WCS_ENDPOINT, this.criteriaQuery);
//		XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(XWCPSQueryParser.WCS_ENDPOINT, this.wcsAdapter);
		
		XWCPSEvalVisitor visitor = new XWCPSEvalVisitor(this.wcsAdapter);
		Query result = visitor.visit(tree);

		return result;
	}
}
