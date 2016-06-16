package gr.cite.earthserver.wcps.parser.evaluation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.mockito.Matchers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.wcps.parser.core.MixedValue;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;
import gr.cite.earthserver.wcs.client.WCSRequest;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder.DescribeCoverage;
import gr.cite.earthserver.wcs.client.WCSRequestBuilder.ProcessCoverages;
import gr.cite.earthserver.wcs.client.WCSRequestException;
import gr.cite.femme.core.DataElement;
import gr.cite.femme.core.Metadatum;
import gr.cite.femme.query.criteria.CriteriaQuery;
import gr.cite.femme.query.criteria.UnsupportedQueryOperationException;
import gr.cite.femme.query.criteria.Where;
import gr.cite.femme.query.criteria.WhereBuilder;

public class XWCPSEvaluationMocks {
	public static WCSRequestBuilder mockWCSRequestBuilder() {
		WCSRequestBuilder requestBuilder = mock(WCSRequestBuilder.class);

		// TODO mock GetCapabilities
		// TODO mock GetCoverage
		return requestBuilder;
	}

	public static WCSRequestBuilder mockDescribeCoverage() {
		WCSRequestBuilder requestBuilder = mockWCSRequestBuilder();
		return mockDescribeCoverage(requestBuilder);
	}

	public static WCSRequestBuilder mockDescribeCoverage(WCSRequestBuilder requestBuilder) {
		DescribeCoverage describeCoverage = mock(DescribeCoverage.class);

		when(requestBuilder.describeCoverage()).thenReturn(describeCoverage);

		WCSRequest wcsRequest = mock(WCSRequest.class);
		when(describeCoverage.coverageId(anyString())).thenReturn(describeCoverage);
		when(describeCoverage.build()).thenReturn(wcsRequest);

		try {
			when(wcsRequest.get()).thenReturn(new XwcpsQueryResult() {
				{
					setAggregatedValue(XWCPSQueryMockedResponses.AVGLANDTEMP_DESCRIBE_COVERAGE);
				}
			});
		} catch (WCSRequestException e) {
		}

		return requestBuilder;
	}

	public static WCSRequestBuilder mockProcessCoverages(String wcpsQueryResponse) {
		WCSRequestBuilder requestBuilder = mockWCSRequestBuilder();
		ProcessCoverages processCoverages = mock(ProcessCoverages.class);
		when(requestBuilder.processCoverages()).thenReturn(processCoverages);

		WCSRequest wcsRequest = mock(WCSRequest.class);
		when(processCoverages.query(anyString())).thenReturn(processCoverages);
		when(processCoverages.build()).thenReturn(wcsRequest);

		try {
			when(wcsRequest.get()).thenReturn(new XwcpsQueryResult() {
				{
					setAggregatedValue(wcpsQueryResponse);
					MixedValue mixed = new MixedValue();
					setMixedValues(Sets.newHashSet(mixed));
				}
			});
		} catch (WCSRequestException e) {
		}

		return requestBuilder;
	}

	public static WCSRequestBuilder mockProcessCoverages(String query, String wcpsQueryResponse) {
		WCSRequestBuilder requestBuilder = mockWCSRequestBuilder();
		ProcessCoverages processCoverages = mock(ProcessCoverages.class);
		when(requestBuilder.processCoverages()).thenReturn(processCoverages);

		WCSRequest wcsRequest = mock(WCSRequest.class);
		when(processCoverages.query(query)).thenReturn(processCoverages);
		when(processCoverages.build()).thenReturn(wcsRequest);

		try {
			when(wcsRequest.get()).thenReturn(new XwcpsQueryResult() {
				{
					setAggregatedValue(wcpsQueryResponse);
				}
			});
		} catch (WCSRequestException e) {
		}

		return requestBuilder;
	}

	public static CriteriaQuery<Coverage> mockCriteriaQuery() {
		CriteriaQuery<Coverage> query = mock(CriteriaQuery.class);
		Where<Coverage> where = mock(Where.class);
		WhereBuilder<Coverage> whereBuilder = mock(WhereBuilder.class);

		when(query.<Coverage> expressionFactory()).thenReturn(where);
		when(query.whereBuilder()).thenReturn(where);

//		when(where.exists(any())).thenReturn(whereBuilder);
		when(where.expression(Matchers.<Metadatum> any())).thenReturn(whereBuilder);
		when(where.expression(Matchers.<WhereBuilder<Coverage>> any())).thenReturn(whereBuilder);
		when(where.isChildOf(Matchers.<DataElement> any())).thenReturn(whereBuilder);
		when(where.isChildOf(Matchers.<WhereBuilder<Coverage>> any())).thenReturn(whereBuilder);
		try {
			when(where.isParentOf(Matchers.<Metadatum> any())).thenReturn(whereBuilder);
			when(where.isParentOf(Matchers.<DataElement> any())).thenReturn(whereBuilder);
		} catch (UnsupportedQueryOperationException e) {
		}

		when(whereBuilder.build()).thenReturn(query);

		return query;
	}

	public static CriteriaQuery<Coverage> mockCriteriaQuery(Coverage returnedCoverage) {
		CriteriaQuery<Coverage> query = mockCriteriaQuery();

		when(query.find(anyString())).thenReturn(returnedCoverage);
//		when(query.find(Matchers.<Coverage> any())).thenReturn(returnedCoverage);
		when(query.find()).thenReturn(Lists.newArrayList(returnedCoverage));

		return query;
	}

	public static CriteriaQuery<Coverage> mockCriteriaQuery(List<Coverage> returnedCoverages) {
		CriteriaQuery<Coverage> query = mockCriteriaQuery();

		when(query.find()).thenReturn(returnedCoverages);

		return query;
	}
}
