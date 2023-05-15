package com.example.ics.service;

import com.example.ics.Entity.Images;
import com.example.ics.Reposittory.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagesService {
    private final ImagesRepository imagesRepository;

    @Autowired
    public ImagesService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    public List<Images> getAllImages() {
        return imagesRepository.findAll();
    }
}