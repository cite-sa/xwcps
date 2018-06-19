package gr.cite.earthserver.query;

import gr.cite.earthserver.query.UnsupportedQueryOperationException;
import gr.cite.femme.core.model.Metadatum;

public interface Where<T> {

	WhereBuilder<T> expression(WhereBuilder<T> expression);

	<S extends Metadatum> WhereBuilder<T> expression(S metadatum);

	/* <S extends Metadatum> WhereBuilder<T> exists(S metadatum); */

	<S extends Metadatum> WhereBuilder<T> isParentOf(S metadatum) throws UnsupportedQueryOperationException;

	<S> WhereBuilder<T> isParentOf(S dataElement) throws UnsupportedQueryOperationException;

	<S> WhereBuilder<T> isChildOf(S dataElement);

	/**
	 * is child of a S element which validates the {@linkplain WhereBuilder
	 * where}
	 * 
	 * @param where
	 * @return
	 */
	<S> WhereBuilder<T> isChildOf(WhereBuilder<S> where);

}
