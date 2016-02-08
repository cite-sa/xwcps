package gr.cite.earthserver.wcps.parser.utils;

import java.util.List;

import gr.cite.exmms.manager.core.DataElement;
import gr.cite.exmms.manager.core.Metadatum;
import gr.cite.exmms.manager.criteria.CriteriaQuery;
import gr.cite.exmms.manager.criteria.UnsupportedQueryOperationException;
import gr.cite.exmms.manager.criteria.Where;
import gr.cite.exmms.manager.criteria.WhereBuilder;

public class PrintCriteriaQuery implements CriteriaQuery<DataElement> {

	StringBuilder query = new StringBuilder();

	@Override
	public Where<DataElement> whereBuilder() {
		return new WherePrint(this, query);
	}

	@Override
	public Where<DataElement> expressionFactory() {
		return new WherePrint(this);
	}

	@Override
	public DataElement find(String id) {
		System.out.println(query);
		return null;
	}

	@Override
	public DataElement find(DataElement t) {
		System.out.println(query);
		return null;
	}

	@Override
	public List<DataElement> find() {
		System.out.println(query);
		return null;
	}

	@Override
	public String toString() {
		return query.toString();
	}
	
	public static class WherePrint implements Where<DataElement> {

		StringBuilder query;
		private WhereBuilder<DataElement> whereBuilder;

		public WherePrint(PrintCriteriaQuery printCriteriaQuery, StringBuilder query) {
			this.query = query;
			this.whereBuilder = new WhereBuilderPrint(printCriteriaQuery, this, query);
		}
		
		public WherePrint(PrintCriteriaQuery printCriteriaQuery) {
			this.query = new StringBuilder();
			this.whereBuilder = new WhereBuilderPrint(printCriteriaQuery, this, query);
		}

		@Override
		public WhereBuilder<DataElement> expression(WhereBuilder<DataElement> expression) {
			query.append(" (").append(((WhereBuilderPrint) expression).getQuery()).append(")");
			return whereBuilder;
		}

		@Override
		public <S extends Metadatum> WhereBuilder<DataElement> expression(S metadatum) {
			query.append(" " + metadatum.getKey() + " = " + metadatum.getValue());
			return whereBuilder;
		}

		@Override
		public <S extends Metadatum> WhereBuilder<DataElement> exists(S metadatum) {
			query.append(" exists " + metadatum.getKey());
			return whereBuilder;
		}

		@Override
		public <S extends Metadatum> WhereBuilder<DataElement> isParentOf(S metadatum)
				throws UnsupportedQueryOperationException {
			query.append(" isParentOf " + metadatum.getKey());
			return whereBuilder;
		}

		@Override
		public <S extends DataElement> WhereBuilder<DataElement> isParentOf(S dataElement)
				throws UnsupportedQueryOperationException {
			query.append(" isParentOf " + dataElement.getId());
			return whereBuilder;
		}

		@Override
		public <S extends DataElement> WhereBuilder<DataElement> isChildOf(S dataElement) {
			query.append(" isChildOf " + dataElement.getId());
			return whereBuilder;
		}

		@Override
		public <S extends DataElement> WhereBuilder<DataElement> isChildOf(WhereBuilder<S> where) {
			query.append(" isChildOf(").append(((WhereBuilderPrint) where).getQuery()).append(")");
			return whereBuilder;
		}
		
		@Override
		public String toString() {
			return query.toString();
		}

	}

	public static class WhereBuilderPrint implements WhereBuilder<DataElement> {

		StringBuilder query;
		WherePrint where;
		private CriteriaQuery<DataElement> printCriteriaQuery;

		public WhereBuilderPrint(PrintCriteriaQuery printCriteriaQuery, WherePrint where, StringBuilder query) {
			super();
			this.query = query;
			this.where = where;
			this.printCriteriaQuery = printCriteriaQuery;
		}

		public WhereBuilderPrint() {
			this.query = new StringBuilder();
		}

		public StringBuilder getQuery() {
			return query;
		}

		@Override
		public Where<DataElement> or() {
			query.append(" or");
			return where;
		}

		@Override
		public Where<DataElement> and() {
			query.append(" and");
			return where;
		}

		@Override
		public CriteriaQuery<DataElement> build() {
			return printCriteriaQuery;
		}
		
		@Override
		public String toString() {
			return query.toString();
		}

	}

}
