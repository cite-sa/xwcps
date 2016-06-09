package gr.cite.earthserver.wcs.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import gr.cite.earthserver.wcps.parser.core.MixedValue;
import gr.cite.earthserver.wcps.parser.core.XwcpsQueryResult;

@Deprecated
public class WCSRequest {
	private static final Logger logger = LoggerFactory.getLogger(WCSRequest.class);

	public static WCSRequestBuilder newBuilder() {
		return new WCSRequestBuilder();
	}

	private static final Cache<URI, XwcpsQueryResult> CACHE = CacheBuilder.newBuilder().maximumSize(20)
			.expireAfterAccess(1, TimeUnit.HOURS).build();

	private WebTarget webTarget;

	WCSRequest(WebTarget webTarget) {
		this.webTarget = webTarget;
	}

	public XwcpsQueryResult get() throws WCSRequestException {

		try {
			return CACHE.get(webTarget.getUri(), new Callable<XwcpsQueryResult>() {

				@Override
				public XwcpsQueryResult call() throws Exception {
					Response response = webTarget.request(MediaType.TEXT_PLAIN).get();

					XwcpsQueryResult xwcpsQueryResult = new XwcpsQueryResult();

					MediaType mediaType = MediaType.valueOf(response.getHeaderString("Content-Type"));

					if (mediaType.toString().contains("image")) {
						xwcpsQueryResult.setMixedValues(new HashSet<>());

						MixedValue image = new MixedValue();
						image.setWcpsMediaType(mediaType);

						// image.setWcpsValue((InputStream)
						// response.getEntity());

						ByteArrayInputStream byteArrayInputStream;
						try (InputStream inputStream = (InputStream) response.getEntity()) {
							byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(inputStream));
						}
						image.setWcpsValue(byteArrayInputStream);

						xwcpsQueryResult.getMixedValues().add(image);

					} else {
						String responseString = response.readEntity(String.class);

						if (response.getStatus() >= 300) {
							throw new WCSRequestException(responseString, response.getStatus());
						}

						// TODO Content-type:
						// multipart/x-mixed-replace;boundary=End
						logger.warn("-----------------------------------------------------------------------");
						logger.warn("-----------------------------------------------------------------------");
						logger.warn("--  TODO  read Content-type: multipart/x-mixed-replace;boundary=End  --");
						logger.warn("-----------------------------------------------------------------------");
						logger.warn("-----------------------------------------------------------------------");
						// FIXME delete if
						if (responseString.startsWith("\r\n--End")) {
							responseString = responseString.replaceAll("--End--", "").replaceAll("--End", "")
									.replaceAll("Content-type: text/plain", "").trim();
						}

						xwcpsQueryResult.setAggregatedValue(responseString);
					}

					return xwcpsQueryResult;
				}
			});
		} catch (ExecutionException e) {
			throw new WCSRequestException(e);
		}

	}

}
