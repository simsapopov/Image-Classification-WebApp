package com.example.ics.service;

import com.example.ics.entity.Image;
import com.example.ics.repository.ImageRepository;
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
public class ImageService {
    private final ImageRepository imageRepository;
    private final TagService tagService;

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Page<Image> getAllImagesPage(Integer pageNo, Integer pageSize, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by("analyzedAt").ascending() : Sort.by("analyzedAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return imageRepository.findAll(pageable);
    }

    public Page<Image> getAllImagesByTag(String tagName, Integer pageNo, Integer pageSize, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by("analyzedAt").ascending() : Sort.by("analyzedAt").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return imageRepository.findAllByTagName(tagName, pageable);
    }

    public Image getImageFromId(Long id) {
        if (id != null) {
            return imageRepository.findById(id).orElse(null);
        }
        return null;
    }

    public List<Image> getAllImagesWithIdList(List<Long> id) {
        List<Image> images = new ArrayList<>();
        if (id != null) {
            for (Long imageId : id) {
                if (imageId != null) {
                    Image image = getImageFromId(imageId);
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
        List<Image> listImages = getAllImagesWithIdList(tagService.findAllImagesWithTag(tag));
        for (int i = 0; i < listImages.size(); i++) {
            string.append(listImages.get(i).getUrl());
            string.append(" Tags:");
            string.append(tagService.getStringOfAllTagsWithId(listImages.get(i).getId()));

        }
        return string.toString();
    }

    public String getMessege(Long id) {
        Image image = imageRepository.findById(id).get();
        return "This image was processed on: " + image.getAnalyzedAt() + " by " + image.getName();
    }

    public Image findImageByUrl(String Url) {
        Image imageFromUrl = imageRepository.findByUrl(Url);
        if (imageFromUrl != null) {
            return imageFromUrl;
        }
        return imageRepository.findByImgurlUrl(Url);

    }

    public Image saveImage(String Imgururl, String imageUrl, String hash) {
        Image newImage = new Image();
        if (!imageUrl.contains("http")) {
            newImage.setUrl(Imgururl);
        } else newImage.setUrl(imageUrl);
        newImage.setImgurlUrl(Imgururl);
        newImage.setHash(hash);
        newImage.setAnalyzedAt(new Date());
        this.imageRepository.saveAndFlush(newImage);
        return newImage;

    }

    public String deleteImageWithId(Long Id) {
        imageRepository.deleteById(Id);
        return "Deleted";
    }

}