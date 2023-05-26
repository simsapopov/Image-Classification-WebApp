package com.example.ics;

import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.ImagesRepository;
import com.example.ics.Reposittory.TagRepository;
import com.example.ics.service.ImagesService;
import com.example.ics.service.ImaggaService;
import com.example.ics.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ImageClassificationController {
  private final ImaggaService imaggaService;
  private final ImagesService imagesService;
  private final TagService tagService;



  @PostMapping("/classify")
    public String classify(@RequestBody String imageUrl) {
    return imaggaService.classifyImage(imageUrl);
  }
  @GetMapping("/tagPhoto")
  public String tagPhoto(Model model) {

    Images image = imagesService.findImageByUrl("https://m.netinfo.bg/media/images/32781/32781450/991-ratio-vlak.jpg"); // Replace with your method
    List<String> tags = tagService.getAllTagNames(); // Replace with your method
   // model.addAttribute("image", image);
    model.addAttribute("tags", tagService.getAllTagNames());
    return "tagPhoto";

  }
}
