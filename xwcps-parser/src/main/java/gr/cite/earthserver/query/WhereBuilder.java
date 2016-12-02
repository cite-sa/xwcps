package gr.cite.earthserver.query;

public interface WhereBuilder<T> {

	Where<T> or();

	Where<T> and();

	CriteriaQuery<T> build();

}