package no.nav.onpremstatuspoll;

import no.nav.onpremstatuspoll.util.TokenClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class OnPremStatusPollApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnPremStatusPollApplication.class, args);

		HttpClient httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(10))
				.build();

		TokenClient tokenClient = new TokenClient(httpClient);
		PortalserverKlient portalserverKlient = new PortalserverKlient(httpClient, tokenClient);
		Poller poller = new Poller(httpClient);

		try {
			List<ServiceDto> services = portalserverKlient.getPollingServices();
			System.out.println("Polling " + services.size() + " services");

			List<RecordDto> recordDtos = services.stream()
					.map(poller::poll)
					.collect(Collectors.toList());

			portalserverKlient.postStatus(recordDtos);
			System.out.println("Done polling and posting status");
		} catch (Exception e) {
			System.err.println("Failed to poll services: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
