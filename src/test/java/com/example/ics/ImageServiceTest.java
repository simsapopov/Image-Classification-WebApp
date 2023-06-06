package com.example.ics;

import com.example.ics.entity.Image;
import com.example.ics.repository.ImageRepository;
import com.example.ics.service.ImageService;
import com.example.ics.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceTest {
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService(imageRepository, tagService);
    }

    @Test
    void testGetAllImages() {
        List<Image> expectedImages = Arrays.asList(
                createImage(1L, "image1.jpg"),
                createImage(2L, "image2.jpg")
        );
        when(imageRepository.findAll()).thenReturn(expectedImages);

        List<Image> actualImages = imageService.getAllImages();

        assertEquals(expectedImages, actualImages);
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void testGetImageFromId() {
        Image expectedImage = createImage(1L, "image1.jpg");
        when(imageRepository.findById(1L)).thenReturn(Optional.of(expectedImage));

        Image actualImage = imageService.getImageFromId(1L);

        assertEquals(expectedImage, actualImage);
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllImagesWithIdList() {
        List<Long> idList = Arrays.asList(1L, 2L);
        Image image1 = createImage(1L, "image1.jpg");
        Image image2 = createImage(2L, "image2.jpg");
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(imageRepository.findById(2L)).thenReturn(Optional.of(image2));

        List<Image> expectedImages = Arrays.asList(image1, image2);
        List<Image> actualImages = imageService.getAllImagesWithIdList(idList);

        assertEquals(expectedImages, actualImages);
        verify(imageRepository, times(2)).findById(anyLong());
    }

    @Test
    void testGetAllImagesWithTags() {
        String tag = "nature";
        Image image1 = createImage(1L, "www.image.com");
        Image image2 = createImage(2L, "www.image.com");
        when(tagService.findAllImagesWithTag(tag)).thenReturn(Arrays.asList(1L, 2L));
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image1));
        when(imageRepository.findById(2L)).thenReturn(Optional.of(image2));
        when(tagService.getStringOfAllTagsWithId(1L)).thenReturn("nature, landscape");
        when(tagService.getStringOfAllTagsWithId(2L)).thenReturn("nature, animals");

        String expectedOutput = "www.image.com Tags:nature, landscapewww.image.com Tags:nature, animals";
        String actualOutput = imageService.getAllImagesWithTags(tag);

        assertEquals(expectedOutput, actualOutput);
        verify(tagService, times(1)).findAllImagesWithTag(tag);
        verify(imageRepository, times(2)).findById(anyLong());
        verify(tagService, times(1)).getStringOfAllTagsWithId(1L);
        verify(tagService, times(1)).getStringOfAllTagsWithId(2L);
    }

    @Test
    void testFindImageByUrl() {
        Image expectedImage = createImage(1L, "www.image.com");
        when(imageRepository.findByUrl("www.image.com")).thenReturn(expectedImage);

        Image actualImage = imageService.findImageByUrl("www.image.com");

        assertEquals(expectedImage, actualImage);
        verify(imageRepository, times(1)).findByUrl("www.image.com");
    }


    @Test
    void testDeleteImageWithId() {
        Long imageId = 1L;
        doNothing().when(imageRepository).deleteById(imageId);

        String result = imageService.deleteImageWithId(imageId);

        assertEquals("Deleted", result);
        verify(imageRepository, times(1)).deleteById(imageId);
    }

    private Image createImage(Long id, String url) {
        Image image = new Image();
        image.setId(id);
        image.setUrl(url);
        return image;
    }
}
