package no.nav.onpremstatuspoll;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;

public class Poller {

    private final HttpClient httpClient;

    public Poller(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public RecordDto poll(ServiceDto serviceDto) {
        try {
            Instant before = Instant.now();
            RecordDto recordDto = getPolledServiceStatus(serviceDto);
            Instant after = Instant.now();
            int responseTime = (int) Duration.between(before, after).toMillis();
            recordDto.setResponseTime(responseTime);
            recordDto.serviceId(serviceDto.getId());
            return recordDto;
        } catch (Exception e) {
            System.out.println("Poll exception for " + serviceDto.getName() + ": " + e.getMessage());
            return createPolledServiceStatusForUnresponsiveEndpoint(serviceDto);
        }
    }

    private RecordDto getPolledServiceStatus(ServiceDto serviceDto) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceDto.getPollingUrl()))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonObject = toJson(response.body());
        RecordDto recordDto = mapToRecordDto(jsonObject);
        recordDto.setTimestamp(OffsetDateTime.now());
        recordDto.serviceId(serviceDto.getId());
        return recordDto;
    }

    private static RecordDto mapToRecordDto(JsonObject jsonRecord) {
        RecordDto recordDto = new RecordDto();
        StatusDto status = StatusDto.fromValue(jsonRecord.getString("status"));
        recordDto.setStatus(StatusDto.UP.equals(status) ? StatusDto.OK : status);
        recordDto.setDescription(jsonRecord.getString("description", null));
        recordDto.setLogLink(jsonRecord.getString("logglink", null));
        recordDto.setSource(RecordSourceDto.ONPREM_POLL);
        return recordDto;
    }

    private static RecordDto createPolledServiceStatusForUnresponsiveEndpoint(ServiceDto serviceDto) {
        return new RecordDto()
                .serviceId(serviceDto.getId())
                .description("Service status endpoint is not responding")
                .status(StatusDto.UNKNOWN)
                .timestamp(OffsetDateTime.now());
    }

    private static JsonObject toJson(String str) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(str))) {
            return jsonReader.readObject();
        }
    }
}
