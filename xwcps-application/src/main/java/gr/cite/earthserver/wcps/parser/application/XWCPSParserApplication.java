package gr.cite.earthserver.wcps.parser.application;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import com.google.common.collect.Lists;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.application.config.XWCPSParserConfiguration;
import gr.cite.earthserver.wcps.parser.application.resource.ParserResource;
import gr.cite.exmms.core.DataElement;
import gr.cite.exmms.core.Element;
import gr.cite.exmms.core.Metadatum;
import gr.cite.exmms.criteria.CriteriaQuery;
import gr.cite.exmms.criteria.UnsupportedQueryOperationException;
import gr.cite.exmms.criteria.Where;
import gr.cite.exmms.criteria.WhereBuilder;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class XWCPSParserApplication extends Application<XWCPSParserConfiguration> {

	@Override
	public void run(XWCPSParserConfiguration configuration, Environment environment) throws Exception {

		CriteriaQuery<Coverage> criteriaQuery = mock();

		environment.jersey().register(new ParserResource(new XWCPSQueryParser(criteriaQuery)));
	}

	private CriteriaQuery<Coverage> mock() {

		return new MyCriteriaQuery();
	}

	public static void main(String[] args) throws Exception {
		new XWCPSParserApplication().run("server", "xwcps-config.yaml");
	}

}

class MyCriteriaQuery implements CriteriaQuery<Coverage> {

	public static Coverage AvgLandTemp = new Coverage() {
		{
			setId("AvgLandTemp");
			setLocalId("AvgLandTemp");
		}
	};

	public static Coverage NIR = new Coverage() {
		{
			setId("NIR");
			setLocalId("NIR");
		}
	};

	List<Coverage> coverages = new ArrayList<>();

	Where<Coverage> where = new MyWhere(new MyWhereBuilder(this));

	@Override
	public Where<Coverage> whereBuilder() {
		return where;
	}

	@Override
	public Coverage find(Coverage t) {
		return t;
	}

	@Override
	public Coverage find(String id) {
		return AvgLandTemp;
	}

	@Override
	public List<Coverage> find() {
		if (getCoverages().isEmpty()) {
			getCoverages().add(MyCriteriaQuery.AvgLandTemp);
			getCoverages().add(MyCriteriaQuery.NIR);
		}
		return coverages;
	}

	@Override
	public <S> Where<S> expressionFactory() {
		return (Where<S>) new MyWhere(new MyWhereBuilder(this));
	}

	public List<Coverage> getCoverages() {
		return coverages;
	}
}

class MyWhereBuilder implements WhereBuilder<Coverage> {

	private MyWhere where = new MyWhere(this);
	private MyCriteriaQuery query;

	public MyWhereBuilder(MyCriteriaQuery query) {
		super();
		this.query = query;
	}

	@Override
	public Where<Coverage> or() {
		return where;
	}

	@Override
	public CriteriaQuery<Coverage> build() {
		if (query.getCoverages().isEmpty()) {
			query.getCoverages().add(MyCriteriaQuery.AvgLandTemp);
			query.getCoverages().add(MyCriteriaQuery.NIR);
		}

		return query;
	}

	@Override
	public Where<Coverage> and() {
		return where;
	}

	public MyCriteriaQuery getQuery() {
		return query;
	}

	public MyWhere getWhere() {
		return where;
	}

}

class MyWhere implements Where<Coverage> {

	private MyWhereBuilder builder;

	public MyWhere(MyWhereBuilder builder) {
		super();
		this.builder = builder;
	}

	@Override
	public <S extends Element> WhereBuilder<Coverage> isParentOf(S dataElement)
			throws UnsupportedQueryOperationException {
		return builder;
	}

	@Override
	public <S extends Metadatum> WhereBuilder<Coverage> isParentOf(S dataElement)
			throws UnsupportedQueryOperationException {
		return builder;

	}

	@Override
	public <S extends Element> WhereBuilder<Coverage> isChildOf(WhereBuilder<S> where) {
		return builder;

	}

	@Override
	public <S extends Element> WhereBuilder<Coverage> isChildOf(S dataElement) {
		return builder;

	}

	@Override
	public <S extends Metadatum> WhereBuilder<Coverage> expression(S metadatum) {

		if (metadatum.getValue().equals("NIR")) {
			builder.getQuery().getCoverages().add(MyCriteriaQuery.NIR);
		} else {
			builder.getQuery().getCoverages().add(MyCriteriaQuery.AvgLandTemp);
		}

		return builder;

	}

	@Override
	public WhereBuilder<Coverage> expression(WhereBuilder<Coverage> expression) {
		return builder;

	}

	@Override
	public <S extends Metadatum> WhereBuilder<Coverage> exists(S metadatum) {
		return builder;

	}
}