package com.example.ics.Service;
import com.google.common.hash.Hashing;
import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ImaggaService {

    private final ImagesRepository imagesRepository;
    private final ImagesService imagesService;
    private final JsonParser jsonParser;


    @Value("${imagga.api.key}")
    private String apiKey;

    @Value("${imagga.api.secret}")
    private String apiSecret;

    public String classifyImage(String imageUrl) {
        Images image = imagesService.findImageByUrl(imageUrl);
        long hashedUrl = (long) Hashing.murmur3_32().hashBytes(imageUrl.getBytes()).asInt();
        if (image != null) {

            return jsonParser.parseTagsToString(hashedUrl);
        } else {
            image= imagesService.saveImage(imageUrl,hashedUrl);



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





