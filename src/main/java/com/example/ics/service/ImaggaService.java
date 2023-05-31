package com.example.ics.service;

import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.ImagesRepository;
import com.example.ics.Reposittory.TagRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImaggaService {
    private final ThrottleService throttleService;
    private final TagService tagService;
    private final ImagesService imagesService;
    private final ImagesRepository imagesRepository;
    private final TagRepository tagRepository;
    private final JsonParser jsonParser;


    @Value("${imagga.api.key}")
    private String apiKey;

    @Value("${imagga.api.secret}")
    private String apiSecret;

    public String classifyImage(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        String imageUrl = jsonObject.getString("imageUrl");
        Images image = imagesService.findImageByUrl(imageUrl);
        if (image != null) {
            return image.getId().toString();
        } else {
            if (throttleService.shouldThrottle()) {
                if (throttleService.shouldThrottle()) {
                    return "Rate limit exceeded. Please try again later.";
                }
            }
            System.out.println(imageUrl);
            image = imagesService.saveImage(imageUrl);
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.imagga.com/v2/tags?image_url=" + imageUrl;
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(apiKey, apiSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Tag> tags =jsonParser.parseTagsToList(response.getBody(), imageUrl);
                tagService.addTags(tags,image);
                image.setTags(tags);
                imagesRepository.saveAndFlush(image);
                return image.getId().toString();

            } else {
                return response.getStatusCode().toString();

            }

        }
    }
}





