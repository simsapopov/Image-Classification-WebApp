package com.example.ics.service;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.TagRepository;
import com.google.common.hash.Hashing;
import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ImaggaService {

    private final ImagesRepository imagesRepository;


    private final ParseTagsFromJson parseTagsFromJson;

    @Autowired
    public ImaggaService(ImagesRepository imagesRepository, ParseTagsFromJson parseTagsFromJson) {
        this.imagesRepository = imagesRepository;
        this.parseTagsFromJson = parseTagsFromJson;
    }

    @Value("${imagga.api.key}")
    private String apiKey;

    @Value("${imagga.api.secret}")
    private String apiSecret;

    public String classifyImage(String imageUrl) {
        Images image = imagesRepository.findByUrl(imageUrl);

        if (image != null) {
            return parseTagsFromJson.parseTagsToString((long) Hashing.murmur3_32().hashBytes(imageUrl.getBytes()).asInt());
        } else {

            Images newImage= new Images();
            newImage.setId((long) Hashing.murmur3_32().hashBytes(imageUrl.getBytes()).asInt());
            newImage.setUrl(imageUrl);
            imagesRepository.save(newImage);



            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.imagga.com/v2/tags?image_url=" + imageUrl;

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(apiKey, apiSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                parseTagsFromJson.addTagsToDataBase(response.getBody(), newImage.getId(),imageUrl);
                return response.getBody();

            } else {
                // TODO handle error
                return null;
            }

        }
    }
}





