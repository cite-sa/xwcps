package gr.cite.earthserver.metadata.core;

import gr.cite.earthserver.wcps.parser.utils.XWCPSReservedWords;
//import gr.cite.femme.core.Collection;
//import gr.cite.femme.core.DataElement;
//import gr.cite.femme.core.Metadatum;
import gr.cite.femme.core.model.Collection;
import gr.cite.femme.core.model.DataElement;
import gr.cite.femme.core.model.Metadatum;

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

			Metadatum endpointMetadatum = server.getMetadata().stream()
					.filter(metadatum -> metadatum.getName().equals(XWCPSReservedWords.ENDPOINT)).findFirst().get();

			this.petascopeServer = new PetascopeServer(endpointMetadatum.getValue());
		}

		return petascopeServer;
	}

	// TODO localId as a metadata value?
	public String getLocalId() {
		if (localId == null) {

			Metadatum endpointMetadatum = getMetadata().stream()
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localId == null) ? 0 : localId.hashCode());
		result = prime * result + ((petascopeServer == null) ? 0 : petascopeServer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coverage other = (Coverage) obj;
		if (localId == null) {
			if (other.localId != null)
				return false;
		} else if (!localId.equals(other.localId))
			return false;
		if (petascopeServer == null) {
			if (other.petascopeServer != null)
				return false;
		} else if (!petascopeServer.equals(other.petascopeServer))
			return false;
		return true;
	}
}
