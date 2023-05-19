package com.example.ics;
import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.ImagesRepository;
import com.example.ics.Reposittory.TagRepository;
import com.example.ics.service.ImaggaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class ImageClassificationController {

    private final ImaggaService imaggaService;
    private final ImagesRepository imageRepository;
    private final TagRepository tagRepository;

    public ImageClassificationController(ImaggaService imaggaService,
                                         ImagesRepository imageRepository,
                                         TagRepository tagRepository) {
        this.imaggaService = imaggaService;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/classify")
    public String classify(@RequestBody String imageUrl) {
        return imaggaService.classifyImage(imageUrl);
         }
}
