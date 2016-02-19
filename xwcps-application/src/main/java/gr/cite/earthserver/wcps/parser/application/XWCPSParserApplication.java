package gr.cite.earthserver.wcps.parser.application;

import gr.cite.earthserver.wcps.parser.XWCPSQueryParser;
import gr.cite.earthserver.wcps.parser.application.config.XWCPSParserConfiguration;
import gr.cite.earthserver.wcps.parser.application.resource.ParserResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class XWCPSParserApplication extends Application<XWCPSParserConfiguration> {

	@Override
	public void run(XWCPSParserConfiguration configuration, Environment environment) throws Exception {

		environment.jersey().register(new ParserResource(new XWCPSQueryParser()));
	}

	public static void main(String[] args) throws Exception {
		new XWCPSParserApplication().run("server", "xwcps-config.yaml");
	}

}
