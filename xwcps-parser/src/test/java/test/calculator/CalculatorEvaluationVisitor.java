/*
package test.calculator;

import test.calculator.CalculatorParser.EContext;
import test.calculator.CalculatorParser.FactorContext;
import test.calculator.CalculatorParser.SubexpressionContext;
import test.calculator.CalculatorParser.SubtermContext;
import test.calculator.CalculatorParser.TermContext;

public class CalculatorEvaluationVisitor extends CalculatorBaseVisitor<Integer> {

	@Override
	public Integer visitE(EContext ctx) {
		int term = visit(ctx.term());
		int subexpr = visit(ctx.subexpression());
		return term + subexpr;
	}

	@Override
	public Integer visitTerm(TermContext ctx) {

		int factor = visit(ctx.factor());
		int subterm = visit(ctx.subterm());

		return factor * subterm;
	}

	@Override
	public Integer visitSubterm(SubtermContext ctx) {

		if (ctx.opfact() == null) {
			return 1;
		}

		int factor = visit(ctx.factor());
		int subterm = visit(ctx.subterm());

		switch (ctx.opfact().getText()) {
		case "/":
			return 1 / (factor * subterm);

		default:
			return factor * subterm;
		}
	}

	@Override
	public Integer visitSubexpression(SubexpressionContext ctx) {
		if (ctx.opsum() == null) {
			return 0;
		}

		int sign = 1;
		if (ctx.opsum().getText().equals("-")) {
			sign = -1;
		}

		int term = visit(ctx.term());

		int subexpression = visit(ctx.subexpression());

		return sign * term + subexpression;
	}

	@Override
	public Integer visitFactor(FactorContext ctx) {

		if (ctx.NUMBER() != null) {
			return Integer.valueOf(ctx.NUMBER().getText());
		}

		int expr = visit(ctx.e());

		return expr;
	}

}
*/
