package no.nav.onpremstatuspoll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StatusholderKlient {

    private static final String STATUSHOLDER_URL = System.getenv("statusholder_url");

    private final HttpClient httpClient;

    public StatusholderKlient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void postStatusesToStatusholder(List<RecordDto> recordDtos) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RecordDto.class, new RecordDto.RecordDtoAdapter())
                .create();
        String jsonBody = gson.toJson(recordDtos);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(STATUSHOLDER_URL + "/statuses"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Statusholder response: " + response.statusCode());
    }
}
