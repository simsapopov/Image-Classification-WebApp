package com.example.ics.service;

import com.example.ics.entity.Image;
import com.example.ics.entity.Tag;
import com.example.ics.repository.ImageRepository;
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
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final JsonParser jsonParser;
    private final CheckSum checkSum;

    @Value("${IMAGGA_KEY}")
    private String apiKey;


    @Value("${IMAGGA_SECRET}")
    private String apiSecret;

    public String classifyImageWithImagga(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String imageUrl = jsonObject.getString("imageUrl");
        String imageHash = checkSum.getChecksum(imageUrl);
        Image image = imageRepository.findByHash(imageHash);
        if (image != null) {

            return image.getId().toString();
        }
        image = imageService.findImageByUrl(imageUrl);
        if (image != null) {

            return image.getId().toString();
        }
        String ImgurUrl = imgurService.uploadImage(imageUrl);
        String CheckSum = checkSum.getChecksum(ImgurUrl);
        if (throttleService.shouldThrottle()) {
            return "Rate limit exceeded. Please try again later.";
        }
        image = imageService.saveImage(ImgurUrl, imageUrl, imageHash);
        List<Tag> tagList = new ArrayList<>();
        try {
            tagList = getTagsListImagga(image);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        tagService.addTags(tagList, image);
        image.setTags(tagList);
        imageRepository.saveAndFlush(image);
        return image.getId().toString();
    }

    public List<Tag> getTagsListImagga(Image image) throws Exception {
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





