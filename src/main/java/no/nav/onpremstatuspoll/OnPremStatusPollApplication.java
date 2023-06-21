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
			System.out.println("Line 1");
			List<ServiceDto> services = PortalserverKlient.getPollingServices();
			System.out.println("Line 2");
			List<RecordDto> recordDtos = services.stream().map(Poller::poll).collect(Collectors.toList());
			System.out.println("Line 3");
			PortalserverKlient.postStatus(recordDtos);
			System.out.println("Done getting services");
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
