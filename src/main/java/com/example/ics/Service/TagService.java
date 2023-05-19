package com.example.ics.Service;
import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;


    public List<Tag> getAllTagsWithID(Long ID) {
        return tagRepository.findByimage_id(ID);
    }
    public void addTag(long ID, String tag,double confidence,Images image){
        Tag newTag = new Tag();
        newTag.setId(ID);
        newTag.setName(tag);
        newTag.setConfidencePercentage(confidence);
        newTag.setImage(image);
        tagRepository.save(newTag);
    }


}
