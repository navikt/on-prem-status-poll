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
			services.forEach(s ->

							System.out.println(s.getName()+ " with polling url: "+ s.getPollingUrl())

					);

			System.out.println("Done getting services");
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
//	private static void postStatus() throws IOException {
//		URL url = new URL ("https://statusholder.dev.intern.nav.no/status");
//		HttpURLConnection con = (HttpURLConnection)url.openConnection();
//		con.setRequestMethod("POST");
//		con.setRequestProperty("Content-Type", "application/json");
//		con.setRequestProperty("Accept", "application/json");
//		con.setDoOutput(true);
//		UUID uuid = UUID.randomUUID();
//		String jsonInputString = "{\"serviceId\":\""+uuid.toString()+"\", \"status\": \"ISSUE\"}";
//		try(OutputStream os = con.getOutputStream()) {
//			byte[] input = jsonInputString.getBytes("utf-8");
//			os.write(input, 0, input.length);
//		}
//
//		try(BufferedReader br = new BufferedReader(
//				new InputStreamReader(con.getInputStream(), "utf-8"))) {
//			StringBuilder response = new StringBuilder();
//			String responseLine = null;
//			while ((responseLine = br.readLine()) != null) {
//				response.append(responseLine.trim());
//			}
//			System.out.println(response);
//			System.out.println("Postet status ok");
//		}
//
//	}
//
//	private static HttpURLConnection getPollingServices() throws IOException {
//		String urlString = "https://status-api.dev.nav.no/rest/PollingServicesOnPrem";
//		URL url = new URL(urlString);
//		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		con.setRequestMethod("GET");
//		return con;
//	}
//
//	private static String readBody(HttpURLConnection con) throws IOException {
//
//		BufferedReader in = new BufferedReader(
//				new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer content = new StringBuffer();
//		while ((inputLine = in.readLine()) != null) {
//			content.append(inputLine);
//		}
//		in.close();
//		return content.toString();
//	}

}
