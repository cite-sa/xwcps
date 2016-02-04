package gr.cite.earthserver.metadata.client;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import gr.cite.earthserver.metadata.core.Coverage;
import gr.cite.earthserver.metadata.core.PetascopeServer;

@Deprecated
public class MetadataClientImpl implements MetadataClient {

	// TODO dummy data
	private final Map<PetascopeServer, List<Coverage>> DUMMY_DATA = ImmutableMap
			.<PetascopeServer, List<Coverage>> builder()
			.put(new PetascopeServer("http://flanche.com:9090/rasdaman/ows"),
					Lists.newArrayList(new Coverage("NIR"), new Coverage("AvgLandTemp")))
			.build();

	/* (non-Javadoc)
	 * @see gr.cite.earthserver.metadata.client.MetadataClientAPI#listCoverages()
	 */
	@Override
	public Map<PetascopeServer, List<Coverage>> listCoverages() {
		// TODO
		return DUMMY_DATA;
	}

	/* (non-Javadoc)
	 * @see gr.cite.earthserver.metadata.client.MetadataClientAPI#listCoverages(gr.cite.earthserver.metadata.core.PetascopeServer)
	 */
	@Override
	public List<Coverage> listCoverages(PetascopeServer server) {
		// TODO
		return DUMMY_DATA.get(server);
	}
	
	/* (non-Javadoc)
	 * @see gr.cite.earthserver.metadata.client.MetadataClientAPI#listCoverages(java.lang.String)
	 */
	@Override
	public Map<PetascopeServer, List<Coverage>> listCoverages(String xpath) {
		
		// TODO
		
		return DUMMY_DATA;
	}

	@Override
	public Coverage getCoverage(PetascopeServer server, String localId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coverage> getCoverages(String localId) {
		// TODO Auto-generated method stub
		return null;
	}


}
