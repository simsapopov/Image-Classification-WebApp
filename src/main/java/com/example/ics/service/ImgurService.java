package com.example.ics.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Service
@RequiredArgsConstructor
public class ImgurService {
    @Value("${umgur.cliend.id}")
    private String ClientId;
    private final JsonParser jsonParser;

    public String uploadImage(String inputData) throws Exception {
        if (inputData.startsWith("{\"imageUrl\":")) {
            inputData = jsonParser.extractUrlFromJson(inputData);
        } else {

            inputData = inputData.substring(inputData.indexOf(",") + 1);


        }
        URL url;
        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(inputData, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + ClientId);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }

        wr.close();
        rd.close();
        JSONObject obj = new JSONObject(stb.toString());
        return obj.getJSONObject("data").getString("link");

    }

}