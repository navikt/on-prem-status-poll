package no.nav.onpremstatuspoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;


@SpringBootApplication
public class OnPremStatusPollApplication {

	public static void main(String[] args) {

		SpringApplication.run(OnPremStatusPollApplication.class, args);
		try{
			List<ServiceDto> services = PortalserverKlient.getPollingServices();
			services.forEach(s ->{

							System.out.println("-----------------------------");
							System.out.println(s.getName()+" uuid: "+s.getId() +" with polling url: "+ s.getPollingUrl());
							RecordDto recordDto = Poller.poll(s);
							System.out.println("status: "+ recordDto.getStatus());
							System.out.println("Id: " + recordDto.getLogLink());
							System.out.println("-----------------------------");
					}


					);

			System.out.println("Done getting services");
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
