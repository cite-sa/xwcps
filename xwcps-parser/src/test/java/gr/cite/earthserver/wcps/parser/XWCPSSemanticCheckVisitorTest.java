package gr.cite.earthserver.wcps.parser;

import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import gr.cite.earthserver.wcps.grammar.XWCPSLexer;
import gr.cite.earthserver.wcps.grammar.XWCPSParser;
import gr.cite.earthserver.wcps.parser.errors.SemanticError;
import gr.cite.earthserver.wcps.parser.semanticcheck.XWCPSSemanticCheckVisitor;

public class XWCPSSemanticCheckVisitorTest {
//	@Test
	public void test() {
		String query = // "for c in ( AvgLandTemp ) return encode(1, \"csv\")";
//		"for c in (NIR, AvgLandTemp) return describeCoverage(c)//*[local-name()='domainSet']";
//		 "for c in (NIR, AvgLandTemp) return describeCoverage(c)";
		 "for c in ( AvgLandTemp ) return <a><b>describeCoverage(d)//*[local-name()='domainSet']</d></a>";
		// "/server//coverage/@*[local-name()='test']";
		// "/server";

		CharStream stream = new ANTLRInputStream(query);
		XWCPSLexer lexer = new XWCPSLexer(stream);
		XWCPSParser parser = new XWCPSParser(new CommonTokenStream(lexer));

		ParseTree tree = parser.xwcps();

		XWCPSSemanticCheckVisitor visitor = new XWCPSSemanticCheckVisitor();
		visitor.visit(tree);
		for (SemanticError error: visitor.getCheckResults().getErrors()) {			
			System.out.println(error);
		}
	}

	public static void main(String[] args) {
		new WCPSEvalVisitorTest().test();
	}
}
