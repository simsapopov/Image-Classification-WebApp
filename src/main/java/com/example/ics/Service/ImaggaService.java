package com.example.ics.Service;

import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImaggaService {
    private final ThrottleService throttleService;
    private final ImgurService imgurService;
    private final TagService tagService;
    private final ImagesService imagesService;
    private final ImagesRepository imagesRepository;
    private final JsonParser jsonParser;


    @Value("${imagga.api.key}")
    private String apiKey;

    @Value("${imagga.api.secret}")
    private String apiSecret;

    public String classifyImage(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        String imageUrl = jsonObject.getString("imageUrl");
        Images image = imagesService.findImageByUrl(imageUrl);
        if (image != null) {
            return image.getId().toString();
        }
        String ImgurUrl = imgurService.uploadImage(imageUrl);
        if (throttleService.shouldThrottle()) {
            return "Rate limit exceeded. Please try again later.";
        }
        image = imagesService.saveImage(ImgurUrl, imageUrl);
        List<Tag> tagList=new ArrayList<>();
        try {
            tagList = GetTagsListImagga(image);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        tagService.addTags(tagList, image);
        image.setTags(tagList);
        imagesRepository.saveAndFlush(image);
        return image.getId().toString();
    }
    public List<Tag> GetTagsListImagga(Images image) throws Exception {
        image.setName("Imagga");
        RestTemplate restTemplate = new RestTemplate();
        String ImgurUrl = image.getImgurlUrl();
        String url = "https://api.imagga.com/v2/tags?image_url=" + ImgurUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, apiSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {


            return jsonParser.parseTagsToList(response.getBody(), ImgurUrl);

        }
        throw new Exception(response.getStatusCode().toString());
    }
}





