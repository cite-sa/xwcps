package gr.cite.earthserver.wcps.grammar;

import static org.junit.Assert.*;

import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;

public class XPathParserTest {

	private static final String VALID_EXPRESSION = "/server//coverage[@endpoint='test']/@*[local-name()='test']";
//	private static final String VALID_EXPRESSION = "/server//coverage[@endpoint='test']/@*[local-name()='test']";
	private static final String INVALID_EXPRESSION = "/server//coverage/@**[local-name()='test']";;

	@Test
	public void valid() {

		CharStream stream = new ANTLRInputStream(VALID_EXPRESSION);
		XPathLexer lexer = new XPathLexer(stream);
		XPathParser parser = new XPathParser(new CommonTokenStream(lexer));
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ParseTree tree = parser.main();
		
		System.out.println(tokenStream.getTokens().stream().map(token -> {
			String channelStr = "";
			String txt = token.getText();
			if (txt != null) {
				txt = txt.replace("\n", "\\n");
				txt = txt.replace("\r", "\\r");
				txt = txt.replace("\t", "\\t");
			} else {
				txt = "<no text>";
			}
			return "[@" + token.getTokenIndex() + "," + token.getStartIndex() + ":" + token.getStopIndex() + "='" + txt
					+ "',<" + (token.getType() >= 0 ? XWCPSLexer.ruleNames[token.getType() - 1] : "-1") + ">"
					+ channelStr + "," + token.getLine() + ":" + token.getCharPositionInLine() + "]";
		}).collect(Collectors.toList()).toString());

		System.out.println(tokenStream.getTokens());
		System.out.println(tree.toStringTree(parser));
		
		//assertTrue(parser.getNumberOfSyntaxErrors() == 0);
	}

	@Test
	public void invalid() {

		CharStream stream = new ANTLRInputStream(INVALID_EXPRESSION);
		XPathLexer lexer = new XPathLexer(stream);
		XPathParser parser = new XPathParser(new CommonTokenStream(lexer));

		// suppress errors
		parser.removeErrorListeners();

		ParseTree tree = parser.main();

		assertTrue(parser.getNumberOfSyntaxErrors() > 0);
	}

}
