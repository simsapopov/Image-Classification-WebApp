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
  @GetMapping("/getUniqueTags")
  public List<String> getAllUniqueTags(){
    return tagService.getAllUniqueTags();

  }

  @PostMapping("/getimage")
  public String getimagesfromtaag(@RequestBody String Tag) {
    System.out.println("Simsa");
    List<Images> img = imagesService.getAllImagesWithIdList(tagService.findAllImagesWithTag(Tag));
    System.out.println(img.get(1).getUrl());
   return imagesService.getAllImagesWithIdList(tagService.findAllImagesWithTag(Tag)).toString();
  }

}
