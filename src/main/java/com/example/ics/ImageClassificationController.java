package com.example.ics;


import com.example.ics.entity.Images;
import com.example.ics.entity.Tag;
import com.example.ics.reposittory.ImagesRepository;
import com.example.ics.service.ClarifaiService;
import com.example.ics.service.ImagesService;
import com.example.ics.service.ImaggaService;
import com.example.ics.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ImageClassificationController {
    private final ImaggaService imaggaService;
    private final ImagesRepository imagesRepository;
    private final ImagesService imagesService;
    private final ClarifaiService clarifaiService;
    private final TagService tagService;


    @PostMapping("/classify/imagga")
    public String classify(@RequestBody String imageUrl) {
        try {
            return imaggaService.classifyImage(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/classify/clarifai")
    public String other(@RequestBody String imageUrl) {
        try {
            return clarifaiService.classifyClarifai(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getUniqueTags")
    public List<String> getAllUniqueTags() {
        return tagService.getAllUniqueTags();

    }

    @GetMapping("/getimage/{tag}")
    public ResponseEntity<List<Images>> getimagesfromtaag(@PathVariable String tag) {
        List<Images> img = imagesService.getAllImagesWithIdList(tagService.findAllImagesWithTag(tag));
        if (img != null) {
            return ResponseEntity.ok(img);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Images> getImage(@PathVariable Integer id) {
        Images image = imagesService.getImageFromId(Long.valueOf((id)));
        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<String> deleteWithID(@PathVariable Integer id) {
        tagService.deleteTagsWithId(Long.valueOf(id));
        String a = imagesService.deleteImageWithId(Long.valueOf(id));
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/images/{id}/tags")
    public ResponseEntity<List<Tag>> getImageWithTags(@PathVariable Integer id) {
        List<Tag> tags = tagService.getAllTagsWithId(Long.valueOf(id));

        if (tags != null) {
            return ResponseEntity.ok(tags);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/images")
    public ResponseEntity<List<Images>> getImages() {
        List<Images> image = imagesService.getAllImages();

        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping({"/message/{id}"})
    public String getMessage(@PathVariable Long id) {
        return imagesService.getMessege(id);
    }

    @GetMapping(value = {"/{tag}"})
    public ResponseEntity<List<Images>> getImagesWithTag(@PathVariable String tag) {
        List<Images> imagesList = imagesService.getAllImagesWithIdList(tagService.getImageIdsBySequence(tag));
        if (imagesList == null) {
            return ResponseEntity.notFound().build();
        }
        List<Images> listDistinct = imagesList.stream().distinct().collect(Collectors.toList());
        if (listDistinct != null) {
            return ResponseEntity.ok(listDistinct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(value = {"replacetags/{id}"})
    public String getImagesWithTag(@PathVariable Long id) throws Exception {
        List<Tag> tagList = new ArrayList<>();
       Images image = imagesService.getImageFromId(id);
        if(image == null){
            return "There isn't immage with this id";
        }
        image.setTags(tagList);
        image.setAnalyzedAt(new Date());
        tagService.deleteTagsWithId(id);
        if(image.getName().equals("Imagga")){
            tagList = clarifaiService.GetTagsListClarifai(image);
            image.setTags(tagList);
        }else if(image.getName().equals("Clarifai")){
            tagList = imaggaService.GetTagsListImagga(image);
            image.setTags(tagList);
        }
        imagesRepository.saveAndFlush(image);
        return "Okay";
    }

}
