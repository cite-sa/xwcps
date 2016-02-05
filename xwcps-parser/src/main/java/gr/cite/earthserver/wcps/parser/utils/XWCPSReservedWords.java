package gr.cite.earthserver.wcps.parser.utils;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class XWCPSReservedWords {
	public static final String SERVER = "server";

	public static final String COVERAGE = "coverage";

	public static final String ENDPOINT = "endpoint";

	public static final String ID = "id";

	public static final String GUID = "guid";

	private static final Set<String> reservedWords = ImmutableSet.<String> builder().add(SERVER).add(COVERAGE)
			.add(ENDPOINT).add(ID).add(GUID).build();

	public static Set<String> words() {
		return reservedWords;
	}
}
