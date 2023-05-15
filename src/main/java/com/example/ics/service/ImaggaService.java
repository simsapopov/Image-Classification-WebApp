package com.example.ics.service;

import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;

@Service
public class ImaggaService {

    private final ImagesRepository imagesRepository;

    @Autowired
    public ImaggaService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    @Value("${imagga.api.key}")
    private String apiKey;

    @Value("${imagga.api.secret}")
    private String apiSecret;

    public String classifyImage(String imageUrl) {
        Images image = imagesRepository.findByUrl(imageUrl);

        if (image != null) {

            return "Data from the database: " + image.getName();
        } else {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.imagga.com/v2/tags?image_url=" + imageUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, apiSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            // handle error
            return null;
        }
        }
    }
}