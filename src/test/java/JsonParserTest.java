import com.example.ics.entity.Image;
import com.example.ics.entity.Tag;
import com.example.ics.repository.ImageRepository;
import com.example.ics.repository.TagRepository;
import com.example.ics.service.ImageService;
import com.example.ics.service.JsonParser;
import com.example.ics.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class JsonParserTest {
    private JsonParser jsonParser;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jsonParser = new JsonParser(imageService);
    }

    @Test
    void testParseTagsToList() {
        String jsonResponse = "{\"result\":{\"tags\":[{\"tag\":{\"en\":\"nature\"},\"confidence\":90.5},{\"tag\":{\"en\":\"landscape\"},\"confidence\":80.2},{\"tag\":{\"en\":\"mountain\"},\"confidence\":70.8}]}}";
        String url = "image1.jpg";
        Image image = new Image();
        image.setUrl(url);
        when(imageService.findImageByUrl(url)).thenReturn(image);

        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(createTag("nature", 90.5, image));
        expectedTags.add(createTag("landscape", 80.2, image));
        expectedTags.add(createTag("mountain", 70.8, image));

        List<Tag> actualTags = jsonParser.parseTagsToList(jsonResponse, url);

        assertEquals(expectedTags.get(0).getTag(), actualTags.get(0).getTag());
        assertEquals(expectedTags.get(1).getTag(), actualTags.get(1).getTag());
        assertEquals(expectedTags.get(2).getTag(), actualTags.get(2).getTag());
    }

    private Tag createTag(String tag, double confidence, Image image) {
        Tag newTag = new Tag();
        newTag.setTag(tag);
        newTag.setConfidencePercentage(confidence);
        newTag.setImage(image);
        return newTag;
    }
}
