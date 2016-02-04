package gr.cite.earthserver.wcps.parser.evaluation;

import java.util.List;

import gr.cite.earthserver.wcps.grammar.XWCPSBaseVisitor;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AbsoluteLocationPathNorootContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.AndExprContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.RelativeLocationPathContext;
import gr.cite.earthserver.wcps.grammar.XWCPSParser.StepContext;

public class XpathForClauseEvalVisitor extends XWCPSBaseVisitor<XpathForClause> {

	@Override
	public XpathForClause visitAbsoluteLocationPathNoroot(AbsoluteLocationPathNorootContext ctx) {

		System.out.println("--> " + ctx.getText());

		// TODO Auto-generated method stub
		return super.visitAbsoluteLocationPathNoroot(ctx);
	}

	@Override
	public XpathForClause visitAndExpr(AndExprContext ctx) {
		if (ctx.equalityExpr().size() > 1) {
			System.out.println("XpathForClauseEvalVisitor.visitAndExpr()");
			System.out.println("\t" + ctx.equalityExpr(0).getText());
			System.out.println("\t" + ctx.equalityExpr(1).getText());

		}

		// TODO Auto-generated method stub
		return super.visitAndExpr(ctx);
	}

	@Override
	public XpathForClause visitRelativeLocationPath(RelativeLocationPathContext ctx) {

		List<StepContext> steps = ctx.step();
		for (StepContext stepContext : steps) {
			System.out.println(stepContext.getText());
		}

		// TODO Auto-generated method stub
		return super.visitRelativeLocationPath(ctx);
	}

}
