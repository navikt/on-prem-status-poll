package no.nav.onpremstatuspoll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.nav.onpremstatuspoll.util.TokenClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PortalserverKlient {

    private static final String PORTAL_API_URL = System.getenv("portalserver_path");
    private static final String PORTAL_TARGET = System.getenv("PORTAL_TARGET");

    private final HttpClient httpClient;
    private final TokenClient tokenClient;

    public PortalserverKlient(HttpClient httpClient, TokenClient tokenClient) {
        this.httpClient = httpClient;
        this.tokenClient = tokenClient;
    }

    public List<ServiceDto> getPollingServices() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PORTAL_API_URL + "/Services/PollingServicesOnPrem"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson g = new Gson();
        List<ServiceDto> services = Arrays.asList(g.fromJson(response.body(), ServiceDto[].class));
        return services.stream()
                .filter(s -> s.getPollingUrl() != null
                        && !s.getPollingUrl().isEmpty()
                        && !s.getPollingUrl().equals("null"))
                .collect(Collectors.toList());
    }

    public void postStatus(List<RecordDto> recordDtos) throws Exception {
        String accessToken = tokenClient.getAccessTokenForPortal(PORTAL_TARGET);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RecordDto.class, new RecordDto.RecordDtoAdapter())
                .create();
        String jsonBody = gson.toJson(recordDtos);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PORTAL_API_URL + "/UpdateRecords"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("PostStatus response: " + response.statusCode());
    }
}
