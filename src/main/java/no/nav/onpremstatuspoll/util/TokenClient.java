package no.nav.onpremstatuspoll.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TokenClient {

    private static final String TOKEN_ENDPOINT = System.getenv("NAIS_TOKEN_ENDPOINT");

    private final HttpClient httpClient;

    public TokenClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getAccessTokenForPortal(String target) throws IOException, InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("identity_provider", "entra_id");
        body.addProperty("target", target);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Token request failed with status " + response.statusCode() + ": " + response.body());
        }

        JsonObject tokenResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        return tokenResponse.get("access_token").getAsString();
    }
}
