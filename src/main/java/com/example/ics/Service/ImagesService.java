package com.example.ics.Service;

import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final ImagesRepository imagesRepository ;
    private final TagService tagService;

    public List<Images> getAllImages() {
        return imagesRepository.findAll();
    }
    public Images getImageFromId(Long id){
        return imagesRepository.findById(id).orElse(null);
    }
    public List<Images> getAllImagesWithIdList(List<Long> id){
        List<Images> images= new ArrayList<Images>();
        for (int i = 0; i < id.size(); i++) {
            images.add(getImageFromId(id.get(i)));
        }
        return images;

    }
    public String getAllImagesWithTags(String tag) {

        StringBuilder string = new StringBuilder();
        List<Images> listImages= getAllImagesWithIdList(tagService.findAllImagesWithTag(tag));
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
        this.imagesRepository.saveAndFlush(newImage);
        return newImage;

    }

    public String deleteImageWithId(Long Id){
        imagesRepository.deleteById(Id);
        return "Deleted";
    }

}