package com.example.ics.service;

import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final ImagesRepository imagesRepository ;
    private final TagService tagService;

    public List<Images> getAllImages() {
        return imagesRepository.findAll();
    }
    public String getAllImagesWithTags() {
        StringBuilder string = new StringBuilder();
        List<Images> listImages= getAllImages();
        for (int i = 0; i < listImages.size(); i++) {
            string.append(listImages.get(i).getUrl());
            string.append(" Tags:");
            string.append(tagService.getStringOfAllTagsWithId(listImages.get(i).getId()));

        }
        return string.toString();
    }
    public Images findImageByUrl(String Url){
        return imagesRepository.findByUrl(Url);
    }

    public Images saveImage(String imageUrl){
        Images newImage= new Images();
        newImage.setUrl(imageUrl);
        this.imagesRepository.save(newImage);
        return newImage;

    }
    public Long getIdFromUrl(String imageUrl){
        return findImageByUrl(imageUrl).getId();
    }
}