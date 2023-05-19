package com.example.ics.Service;

import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.ImagesRepository;
import com.example.ics.Reposittory.TagRepository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonParser {
    private final TagRepository tagRepository;
    private  final ImagesRepository imagesRepository;


    public JsonParser(TagRepository tagRepository, ImagesRepository imagesRepository) {
        this.tagRepository = tagRepository;

        this.imagesRepository = imagesRepository;
    }
    public String parseTagsToString(Long id) {

        StringBuilder Tags = new StringBuilder();
        List<Tag> tagsList = tagRepository.findByimage_id(id);
        for (Tag tag : tagsList) {
            Tags.append(tag.getName());
            Tags.append(" = ");
            Tags.append(tag.getConfidencePercentage());
            Tags.append("    ");

        }

        return "Data from the database: " + Tags.toString();


    }

    public void addTagsToDataBase(String jsonResponse, Long ID,String url) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray tagsArray = jsonObject.getAsJsonObject("result").getAsJsonArray("tags");
        int i = 0;

        while (i < tagsArray.size()) {
            JsonObject tagObject = tagsArray.get(i).getAsJsonObject();
            String tag = tagObject.getAsJsonObject("tag").get("en").getAsString();
            double confidence = tagObject.get("confidence").getAsDouble();

            Tag newTag = new Tag();
            newTag.setId(ID);
            newTag.setName(tag);
            newTag.setConfidencePercentage(confidence);
            newTag.setImage(imagesRepository.findByUrl(url));
            tagRepository.save(newTag);


            i++;
        }
    }
}
