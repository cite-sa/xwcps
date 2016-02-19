package gr.cite.earthserver.metadata.core;

import gr.cite.earthserver.wcps.parser.utils.XWCPSReservedWords;
import gr.cite.exmms.core.Collection;
import gr.cite.exmms.core.DataElement;
import gr.cite.exmms.core.DataElementMetadatum;

public class Coverage extends DataElement {

	private PetascopeServer petascopeServer = null;

	private String localId = null;

	public Coverage(String id) {
		setId(id);
	}

	public Coverage() {
	}

	public synchronized PetascopeServer getPetascopeServer() {
		if (petascopeServer == null) {
			Collection server = getCollections().get(0);

			DataElementMetadatum endpointMetadatum = server.getMetadata().stream()
					.filter(metadatum -> metadatum.getName().equals(XWCPSReservedWords.ENDPOINT)).findFirst().get();

			this.petascopeServer = new PetascopeServer(endpointMetadatum.getValue());
		}

		return petascopeServer;
	}

	// TODO localId as a metadata value?
	public String getLocalId() {
		if (localId == null) {

			DataElementMetadatum endpointMetadatum = getMetadata().stream()
					.filter(metadatum -> metadatum.getName().equals(XWCPSReservedWords.ID)).findFirst().get();

			this.localId = endpointMetadatum.getValue();
		}

		return localId;
	}
	
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	
	public void setPetascopeServer(PetascopeServer petascopeServer) {
		this.petascopeServer = petascopeServer;
	}

	@Override
	public String toString() {
		return getLocalId();
	}
}
