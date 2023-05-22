package com.example.ics.service;

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

  public List<Tag> getAllTagsWithId(Long Id) {
    return tagRepository.findByimage_id(Id);
  }
    public Tag addTag(long Id, String tag, double confidence, Images image){
    Tag newTag = new Tag();
  //  newTag.setId(Id);
    newTag.setName(tag);
    newTag.setConfidencePercentage(confidence);
    newTag.setImage(image);
    tagRepository.save(newTag);
    return newTag;
    }


}
