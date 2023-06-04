package com.example.ics.service;

import com.example.ics.entity.Images;
import com.example.ics.reposittory.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final ImagesRepository imagesRepository;
    private final TagService tagService;

    public List<Images> getAllImages() {
        return imagesRepository.findAll();
    }
    public Page<Images> getAllImagesPage(Integer pageNo, Integer pageSize, String direction) {
        System.out.println(pageNo+direction);
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by("analyzedAt").ascending() : Sort.by("analyzedAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        System.out.println(pageNo);
        return imagesRepository.findAll(pageable);
    }
    public Page<Images> getAllImagesByTag(String tagName, Integer pageNo, Integer pageSize, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by("analyzedAt").ascending() : Sort.by("analyzedAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return imagesRepository.findAllByTagName(tagName, pageable);
    }
    public Images getImageFromId(Long id) {
        if (id != null) {
            return imagesRepository.findById(id).orElse(null);
        }
        return null;
    }

    public List<Images> getAllImagesWithIdList(List<Long> id) {
        List<Images> images = new ArrayList<>();
        if (id != null) {
            for (Long imageId : id) {
                if (imageId != null) {
                    Images image = getImageFromId(imageId);
                    if (image != null) {
                        images.add(image);
                    }
                }
            }
        }
        return images;
    }

    public String getAllImagesWithTags(String tag) {

        StringBuilder string = new StringBuilder();
        List<Images> listImages = getAllImagesWithIdList(tagService.findAllImagesWithTag(tag));
        for (int i = 0; i < listImages.size(); i++) {
            string.append(listImages.get(i).getUrl());
            string.append(" Tags:");
            string.append(tagService.getStringOfAllTagsWithId(listImages.get(i).getId()));

        }
        return string.toString();
    }

    public String getMessege(Long id) {
        Images image = imagesRepository.findById(id).get();
        return "This image was processed on: " + image.getAnalyzedAt() + " by " + image.getName();
    }

    public Images findImageByUrl(String Url) {
        Images imageFromUrl = imagesRepository.findByUrl(Url);
        if (imageFromUrl != null) {
            return imageFromUrl;
        }
        return imagesRepository.findByImgurlUrl(Url);

    }

    public Images saveImage(String Imgururl, String imageUrl,String hash) {
        Images newImage = new Images();
        if (!imageUrl.contains("http")) {
            newImage.setUrl(Imgururl);
        } else newImage.setUrl(imageUrl);
        newImage.setImgurlUrl(Imgururl);
        newImage.setHash(hash);
        newImage.setAnalyzedAt(new Date());
        this.imagesRepository.saveAndFlush(newImage);
        return newImage;

    }

    public String deleteImageWithId(Long Id) {
        imagesRepository.deleteById(Id);
        return "Deleted";
    }

}