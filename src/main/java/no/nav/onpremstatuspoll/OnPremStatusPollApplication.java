package no.nav.onpremstatuspoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
public class OnPremStatusPollApplication {

	public static void main(String[] args) {

		SpringApplication.run(OnPremStatusPollApplication.class, args);
		try{
			List<ServiceDto> services = PortalserverKlient.getPollingServices();
			List<RecordDto> recordDtos = services.stream().map(Poller::poll).collect(Collectors.toList());
			StatusholderKlient.postStatusesToStatusholder(recordDtos);
			System.out.println("Done getting services");
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
