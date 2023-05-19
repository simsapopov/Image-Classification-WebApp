package com.example.ics.Service;

import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final ImagesRepository imagesRepository ;


    public List<Images> getAllImages() {
        return imagesRepository.findAll();
    }
    public Images findImageByUrl(String Url){
        return imagesRepository.findByUrl(Url);
    }
    public Images saveImage(String imageUrl,Long ID){
        Images newImage= new Images();
        newImage.setId(ID);
        newImage.setUrl(imageUrl);
        this.imagesRepository.save(newImage);
        return newImage;

    }
    public Long getIdFromUrl(String imageUrl){
        return findImageByUrl(imageUrl).getId();
    }
}