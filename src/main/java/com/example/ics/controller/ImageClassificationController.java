package com.example.ics.controller;


import com.example.ics.entity.Image;
import com.example.ics.entity.Tag;
import com.example.ics.repository.ImageRepository;
import com.example.ics.service.ClarifaiService;
import com.example.ics.service.ImageService;
import com.example.ics.service.ImaggaService;
import com.example.ics.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final ClarifaiService clarifaiService;
    private final TagService tagService;


    @PostMapping("/classify/imagga")
    public ResponseEntity<?> classify(@RequestBody String imageUrl) {
        try {
            String result = imaggaService.classifyImageWithImagga(imageUrl);
            if (result.equals("Rate limit exceeded. Please try again later.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/classify/clarifai")
    public ResponseEntity<String> other(@RequestBody String imageUrl) {
        try {
            String result = clarifaiService.classifyImageWithClarifai(imageUrl);
            if (result.equals("Rate limit exceeded. Please try again later.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getUniqueTags")
    public List<String> getAllUniqueTags() {
        return tagService.getAllUniqueTags();

    }

    @GetMapping("/getimage/{tag}")
    public ResponseEntity<List<Image>> getimagesfromtaag(@PathVariable String tag) {
        List<Image> img = imageService.getAllImagesWithIdList(tagService.findAllImagesWithTag(tag));
        if (img != null) {
            return ResponseEntity.ok(img);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Image> getImage(@PathVariable Integer id) {
        Image image = imageService.getImageFromId(Long.valueOf((id)));
        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<String> deleteWithID(@PathVariable Integer id) {
        tagService.deleteTagsWithId(Long.valueOf(id));
        String a = imageService.deleteImageWithId(Long.valueOf(id));
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
    public ResponseEntity<List<Image>> getImages() {
        List<Image> image = imageService.getAllImages();

        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/all")
    public ResponseEntity<Page<Image>> getAllImages(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String tag) {

        Page<Image> imagesPage;

        if (tag == null || tag.isEmpty()) {
            imagesPage = imageService.getAllImagesPage(pageNo, pageSize, direction);
        } else {
            imagesPage = imageService.getAllImagesByTag(tag, pageNo, pageSize, direction);
        }

        if (imagesPage.hasContent()) {
            return ResponseEntity.ok(imagesPage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping({"/message/{id}"})
    public String getMessage(@PathVariable Long id) {
        return imageService.getMessege(id);
    }

    @GetMapping(value = {"/{tag}"})
    public ResponseEntity<List<Image>> getImagesWithTag(@PathVariable String tag) {
        List<Image> imageList = imageService.getAllImagesWithIdList(tagService.getImageIdsBySequence(tag));
        if (imageList == null) {
            return ResponseEntity.notFound().build();
        }
        List<Image> listDistinct = imageList.stream().distinct().collect(Collectors.toList());
        if (listDistinct != null) {
            return ResponseEntity.ok(listDistinct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = {"replacetags/{id}"})
    public String getImagesWithTag(@PathVariable Long id) throws Exception {
        List<Tag> tagList = new ArrayList<>();
        Image image = imageService.getImageFromId(id);
        if (image == null) {
            return "There isn't image with this id";
        }
        image.setTags(tagList);
        image.setAnalyzedAt(new Date());
        tagService.deleteTagsWithId(id);
        if (image.getName().equals("Imagga")) {
            tagList = clarifaiService.getTagsListClarifai(image);
            image.setTags(tagList);
        } else if (image.getName().equals("Clarifai")) {
            tagList = imaggaService.getTagsListImagga(image);
            image.setTags(tagList);
        }
        imageRepository.saveAndFlush(image);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTags = objectMapper.writeValueAsString(image.getTags());
        return jsonTags;
    }

}
