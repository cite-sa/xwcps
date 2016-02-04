package gr.cite.earthserver.metadata.client;

import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.metadata.core.PetascopeServer;

@Deprecated
public interface MetadataClient {

	Map<PetascopeServer, List<Coverage>> listCoverages();

	List<Coverage> listCoverages(PetascopeServer server);

	/**
	 * 
	 * @param server
	 * @param localId
	 *            on {@linkplain PetascopeServer}
	 * @return
	 */
	Coverage getCoverage(PetascopeServer server, String localId);

	/**
	 * 
	 * @param localId
	 *            per {@linkplain PetascopeServer}
	 * @return the coverages, on any server, that contain the localId
	 */
	List<Coverage> getCoverages(String localId);

	default Map<PetascopeServer, List<Coverage>> listCoverages(String xpath) {

		try {
			XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile(xpath);

//			xPathExpression.
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}