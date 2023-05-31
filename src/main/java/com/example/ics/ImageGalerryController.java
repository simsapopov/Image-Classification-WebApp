package com.example.ics;

import com.example.ics.Entity.Images;
import com.example.ics.service.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ImageGalerryController {
    private final ImagesService imagesService;

    @PostMapping("/galerry")
   public String classify(@RequestBody String imageUrl) {
      return imagesService.getAllImagesWithTags(imageUrl);
    }
    @GetMapping("/gallery")
    public String gallery(Model model) {
        List<Images> images = imagesService.getAllImages();
        model.addAttribute("images", images);

        return "gallery";
    }
}


