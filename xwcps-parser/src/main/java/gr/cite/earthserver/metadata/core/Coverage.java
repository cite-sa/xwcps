package gr.cite.earthserver.metadata.core;

public class Coverage {
	private String id;

	private PetascopeServer server;

	public Coverage(String id) {
		this.id = id;
	}

	public Coverage() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PetascopeServer getServer() {
		return server;
	}

	public void setServer(PetascopeServer server) {
		this.server = server;
	}

}
