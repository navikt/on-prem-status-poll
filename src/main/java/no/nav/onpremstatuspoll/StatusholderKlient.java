package no.nav.onpremstatuspoll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class StatusholderKlient {

    private static final String STATUSHOLDER_URL = System.getenv("statusholder_url");


    public static void postStatusesToStatusholder(List<RecordDto> recordDtos) throws IOException {

        URL url = new URL (STATUSHOLDER_URL + "/statuses");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RecordDto.class,new RecordDto.RecordDtoAdapter())
                .create();
        String jsonInputString = gson.toJson(recordDtos);
        try(OutputStream os = con.getOutputStream()) {

            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
            System.out.println("Postet status ok");
        }


    }
}
