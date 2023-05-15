package com.example.ics;
import com.example.ics.service.ImaggaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class ImageClassificationController {

    private final ImaggaService imaggaService;

    public ImageClassificationController(ImaggaService imaggaService) {
        this.imaggaService = imaggaService;
    }

    @PostMapping("/classify")
    public String classify(@RequestBody String imageUrl) {
        return imaggaService.classifyImage(imageUrl);
    }
}