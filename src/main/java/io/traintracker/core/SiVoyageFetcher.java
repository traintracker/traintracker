package io.traintracker.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

//@Component
class SiVoyageFetcher extends AbstractVoyageFetcher {

	private static final Charset CHARSET = Charset.forName("Cp1250");

	private static final Pattern PATTERN = Pattern.compile("<tbody>\r\n" +
			"  <tr>\r\n" +
			"    <td>(?<name>.*?)</td>\r\n" +
			"    <td>.*?</td>\r\n" +
			"    <td>(?<arrivalTime>.*?)</td>\r\n" +
			"    <td>(?<arrivalDelay>.*?)</td>\r\n" +
			"    <td>.*?</td>\r\n" +
			"    <td>(?<departureTime>.*?)</td>\r\n" +
			"    <td>(?<departureDelay>.*?)</td>\r\n" +
			"  </tr>");

	public SiVoyageFetcher(CloseableHttpClient httpClient) {
		super(httpClient);
	}

	@Override
	public Voyage getVoyage(String train) {
		URI uri;

		try {
			uri = new URIBuilder("http://ice.slo-zeleznice.si/CIDirect/default.asp")
					.addParameter("Category", "E-zeleznice")
					.addParameter("Service", "w_zamude_web_2_1")
					.addParameter("vlak", train)
					.build();
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		Matcher matcher = getMatcher(uri, CHARSET, PATTERN);

		List<Station> stations = new ArrayList<>();

		while (matcher.find()) {
			String name = matcher.group("name").trim();
			String arrivalTime = matcher.group("arrivalTime").trim();
			String arrivalDelay = matcher.group("arrivalDelay").trim();
			String departureTime = matcher.group("departureTime").trim();
			String departureDelay = matcher.group("departureDelay").trim();

			Station station = new Station(name);

			if (!Strings.isNullOrEmpty(arrivalTime) && !arrivalTime.equals(".")) {
				station.setArrivalTime(LocalTime.parse(arrivalTime));
				if (!arrivalDelay.equals("R")) {
					station.setArrivalDelay(Integer.parseInt(arrivalDelay));
				}
			}

			if (!Strings.isNullOrEmpty(departureTime) && !departureTime.equals(".")) {
				station.setDepartureTime(LocalTime.parse(departureTime));
				if (!departureDelay.equals("R")) {
					station.setDepartureDelay(Integer.parseInt(departureDelay));
				}
			}

			stations.add(station);
		}

		if (stations.isEmpty()) {
			throw new VoyageNotFoundException();
		}

		return new Voyage(stations);
	}

}