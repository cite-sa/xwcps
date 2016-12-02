package gr.cite.earthserver.wcps.parser.application.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class ParserResourceTest {

//	@Test
	public void testMixed() {
		WebTarget target = ClientBuilder.newClient().target("http://locallhost:8083/xwcps-application/parser");
		String entity = target.path("queryXwcps")
			.queryParam("q", "for data in (frt0000cc22_07_if165l_trr3) return mixed(encode( { red: (int)(255 / (max((data.band_233 != 65535) * data.band_233) - min(data.band_233))) * (data.band_233 - min(data.band_233)); green: (int)(255 / (max((data.band_13 != 65535) * data.band_13) - min(data.band_13))) * (data.band_13 - min(data.band_13)); blue: (int)(255 / (max((data.band_78 != 65535) * data.band_78) - min(data.band_78))) * (data.band_78 - min(data.band_78)) ; alpha: (data.band_100 != 65535) * 255}, \"png\", \"nodata=null\"), metadata(data))")
			.request()
			.get().readEntity(String.class);
		
		System.out.println(entity);
	}
	
//	@Test
	public void test() throws IOException {
		String fromUrl = ClientBuilder.newClient().target("http://access.planetserver.eu:8080/rasdaman/ows?service=WCS&version=2.0.1&request=ProcessCoverages&query=for%20data%20in%20(frt0000cc22_07_if165l_trr3)%20return%20encode(%20%7B%20red:%20(int)(255%20/%20(max((data.band_233%20!=%2065535)%20*%20data.band_233)%20-%20min(data.band_233)))%20*%20(data.band_233%20-%20min(data.band_233));%20green:%20(int)(255%20/%20(max((data.band_13%20!=%2065535)%20*%20data.band_13)%20-%20min(data.band_13)))%20*%20(data.band_13%20-%20min(data.band_13));%20blue:%20(int)(255%20/%20(max((data.band_78%20!=%2065535)%20*%20data.band_78)%20-%20min(data.band_78)))%20*%20(data.band_78%20-%20min(data.band_78))%20;%20alpha:%20(data.band_100%20!=%2065535)%20*%20255%7D,%20%22png%22,%20%22nodata=null%22)")
				.request().get().readEntity(String.class);
		
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(fromUrl.getBytes()));

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(image, "png", baos);
	    byte[] imageData = baos.toByteArray();
//		String image = "data:image/png;base64," + Base64.getEncoder().encodeToString(img.getBytes());
		String string = Base64.getEncoder().encodeToString(baos.toByteArray());
		
		/*System.out.println(string);*/
		try(  PrintWriter out = new PrintWriter("/home/kapostolopoulos/shit.txt") ){
		    out.println(string);
		}
		
	}
}
