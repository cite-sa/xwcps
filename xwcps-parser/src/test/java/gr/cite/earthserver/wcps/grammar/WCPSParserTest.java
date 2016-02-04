package gr.cite.earthserver.wcps.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;

public class WCPSParserTest {

	private static final String VALID_EXPRESSION = "for $a in b return true";

	private static final String INVALID_EXPRESSION = "a in b return true";

	@Test
	public void simpleValidExpressionTest() {

		CharStream stream = new ANTLRInputStream(VALID_EXPRESSION);
		WCPSLexer lexer = new WCPSLexer(stream);
		WCPSParser parser = new WCPSParser(new CommonTokenStream(lexer));

		ParseTree tree = parser.wcpsQuery();

		Assert.assertTrue(parser.getNumberOfSyntaxErrors() == 0);

	}

	@Test
	public void simpleInvalidExpressionTest() {

		CharStream stream = new ANTLRInputStream(INVALID_EXPRESSION);
		WCPSLexer lexer = new WCPSLexer(stream);
		WCPSParser parser = new WCPSParser(new CommonTokenStream(lexer));

		// suppress errors
		parser.removeErrorListeners();

		ParseTree tree = parser.wcpsQuery();

		Assert.assertTrue(parser.getNumberOfSyntaxErrors() > 0);

	}

}
