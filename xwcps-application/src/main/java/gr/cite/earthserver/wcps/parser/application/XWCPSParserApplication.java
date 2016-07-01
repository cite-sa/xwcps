package gr.cite.earthserver.wcps.parser.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.google.common.collect.Lists;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.application.config.XWCPSParserConfiguration;
import gr.cite.earthserver.wcps.parser.application.resource.ParserResource;
import gr.cite.femme.core.Element;
import gr.cite.femme.core.Metadatum;
import gr.cite.femme.query.criteria.CriteriaQuery;
import gr.cite.femme.query.criteria.UnsupportedQueryOperationException;
import gr.cite.femme.query.criteria.Where;
import gr.cite.femme.query.criteria.WhereBuilder;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class XWCPSParserApplication extends Application<XWCPSParserConfiguration> {

	@Override
	public void run(XWCPSParserConfiguration configuration, Environment environment) throws Exception {

		CriteriaQuery<Coverage> criteriaQuery = mock();

		environment.jersey().register(MultiPartFeature.class);

		environment.jersey().register(new ParserResource(new XWCPSQueryParser(criteriaQuery)));
	}

	private CriteriaQuery<Coverage> mock() {

		List<String> coverages = Lists.newArrayList(
				"hrs0000908b_07_if176l_trr3",
				"frt00007b8f_07_if164l_trr3",
				"frt00009392_07_if166l_trr3",
				"hrl0000c067_07_if185l_trr3",
				"frt00003590_07_if164l_trr3",
				"frt0000b1bd_07_if166l_trr3"
				);

		for (String coverage : coverages) {
			MyCriteriaQuery.COVERAGE_MAPS.put(coverage, new Coverage() {
				{
					setLocalId(coverage);
					setId(coverage);
					setName(coverage);
				}
			});
		}

		return new MyCriteriaQuery();
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			new XWCPSParserApplication().run("server", "xwcps-config.yaml");
		} else {
			new XWCPSParserApplication().run(args);
		}
	}

}

class MyCriteriaQuery implements CriteriaQuery<Coverage> {

	public static Map<String, Coverage> COVERAGE_MAPS = new HashMap<>();

	List<Coverage> coverages = new ArrayList<>();

	Where<Coverage> where = new MyWhere(new MyWhereBuilder(this));

	@Override
	public Where<Coverage> whereBuilder() {
		return where;
	}

	@Override
	public List<Coverage> find(Coverage t) {
		return Lists.newArrayList(t);
	}

	@Override
	public Coverage find(String id) {
		return COVERAGE_MAPS.get(id);
	}

	@Override
	public List<Coverage> find() {
		if (getCoverages().isEmpty()) {
			coverages.addAll(COVERAGE_MAPS.values());
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
			query.getCoverages().addAll(MyCriteriaQuery.COVERAGE_MAPS.values());
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
		if (metadatum.getName().equals("endpoint")) {
			return builder;
		}

		builder.getQuery().getCoverages().add(MyCriteriaQuery.COVERAGE_MAPS.get(metadatum));

		return builder;

	}

	@Override
	public WhereBuilder<Coverage> expression(WhereBuilder<Coverage> expression) {
		return builder;

	}
}