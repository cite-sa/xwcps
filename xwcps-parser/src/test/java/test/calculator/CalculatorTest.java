/*
package test.calculator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

import test.calculator.CalculatorLexer;
import test.calculator.CalculatorParser;

import org.antlr.v4.runtime.ANTLRInputStream;

public class CalculatorTest {

	private static final String EXPRESSION = "3+ (5+5) * 2";

	@Test
	public void testEval() {
		Assert.assertEquals(23, listenerWay());
		Assert.assertEquals(23, visitorWay());
	}

	int listenerWay() {
		CharStream stream = new ANTLRInputStream(EXPRESSION);

		CalculatorLexer lexer = new CalculatorLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		CalculatorParser parser = new CalculatorParser(tokenStream);

		// System.out.println(parser.c());

		ParseTree tree = parser.c();
		ParseTreeWalker walker = new ParseTreeWalker();
		CalculatorWalker calcWalker = new CalculatorWalker();
		walker.walk(calcWalker, tree);
		return calcWalker.getSum();
	}

	int visitorWay() {
		CharStream stream = new ANTLRInputStream(EXPRESSION);

		CalculatorLexer lexer = new CalculatorLexer(stream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		CalculatorParser parser = new CalculatorParser(tokenStream);

		// System.out.println(parser.c());

		ParseTree tree = parser.c();

		CalculatorBaseVisitor<Integer> visitor = new CalculatorEvaluationVisitor();
		Integer sum = visitor.visit(tree);
		return sum;
	}
}
*/
