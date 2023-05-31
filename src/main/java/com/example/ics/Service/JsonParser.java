package com.example.ics.Service;

import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonParser {
    private final ImagesService imageService;


    public List<Tag> parseTagsToList(String jsonResponse,String url) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray tagsArray = jsonObject.getAsJsonObject("result").getAsJsonArray("tags");
        List<Tag> tagList = new ArrayList<>();
        Images image =imageService.findImageByUrl(url);
        int i = 0;

        while (i < tagsArray.size()) {
            JsonObject tagObject = tagsArray.get(i).getAsJsonObject();
            String tag = tagObject.getAsJsonObject("tag").get("en").getAsString();
            double confidence = tagObject.get("confidence").getAsDouble();
            if(confidence<30){
                break;
            }
            Tag NewTag = new Tag(tag,confidence,imageService.findImageByUrl(url));
            i++;
            tagList.add(NewTag);
        }

        return tagList;


    }
    String extractUrlFromJson(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            return jsonNode.get("imageUrl").asText();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error extracting URL from JSON input.");
        }
    }
}
