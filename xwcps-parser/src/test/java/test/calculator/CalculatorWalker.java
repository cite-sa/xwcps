/*
package test.calculator;

import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import test.calculator.CalculatorBaseListener;
import test.calculator.CalculatorListener;
import test.calculator.CalculatorParser.CContext;
import test.calculator.CalculatorParser.EContext;
import test.calculator.CalculatorParser.FactorContext;
import test.calculator.CalculatorParser.OpfactContext;
import test.calculator.CalculatorParser.OpsumContext;
import test.calculator.CalculatorParser.SubexpressionContext;
import test.calculator.CalculatorParser.SubtermContext;
import test.calculator.CalculatorParser.TermContext;

public class CalculatorWalker extends CalculatorBaseListener {

	Stack<Integer> sums = new Stack<>();

	@Override
	public void exitSubterm(SubtermContext ctx) {
		super.enterSubterm(ctx);

		OpfactContext opfactContext = ctx.opfact();
		if (opfactContext == null) {
			return;
		}

		int factorSum = sums.pop();
		int sum = sums.pop();
		if (opfactContext.getText().equals("*")) {
			sum = sum * factorSum;
		} else {
			sum = sum / factorSum;
		}

		sums.push(sum);

	}

	@Override
	public void exitFactor(FactorContext ctx) {
		super.exitFactor(ctx);

		if (ctx.NUMBER() != null) {
			sums.push(Integer.valueOf(ctx.NUMBER().getText()));
		}
	}

	@Override
	public void exitSubexpression(SubexpressionContext ctx) {
		super.enterSubexpression(ctx);

		OpsumContext opsumContext = ctx.opsum();

		if (opsumContext == null) {
			return;
		}

		int factor = 1;

		switch (opsumContext.getText()) {
		case "-":
			factor = -1;
			break;
		}

		int termSum = sums.pop();
		int sum = sums.pop();

		sum += factor * termSum;

		sums.push(sum);
	}

	@Override
	public void enterE(EContext ctx) {
		super.enterE(ctx);

		sums.push(0);
	}

	@Override
	public void exitE(EContext ctx) {
		super.exitE(ctx);

		int subexprSum = 0;
		if (ctx.subexpression() != null) {
			subexprSum = sums.pop();
		}
		int termSum = sums.pop();

		sums.push(termSum + subexprSum);
	}

	@Override
	public void exitC(CContext ctx) {
		super.exitC(ctx);

		System.out.println(sums.firstElement());
	}

	public int getSum() {
		return sums.pop();
	}
}
*/
