package com.example.ics.service;

import com.example.ics.Entity.Images;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ImaggaService {
     private final ImagesService imagesService;
     private final JsonParser jsonParser;


    @Value("${imagga.api.key}")
    private String apiKey;

    @Value("${imagga.api.secret}")
    private String apiSecret;

    public String classifyImage(String imageUrl) {
        Images image = imagesService.findImageByUrl(imageUrl);
        if (image != null) {

            return jsonParser.parseTagsToString(imagesService.getIdFromUrl(imageUrl));
        } else {
            image= imagesService.saveImage(imageUrl);



            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.imagga.com/v2/tags?image_url=" + imageUrl;

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(apiKey, apiSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
        jsonParser.addTagsToDataBase(response.getBody(), image.getId(),imageUrl);
        return response.getBody();

            } else {
                return response.getStatusCode().toString();

            }

        }
    }
}





