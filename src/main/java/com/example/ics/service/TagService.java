package com.example.ics.service;

import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
  private final TagRepository tagRepository;

  public List<Tag> getAllTagsWithId(Long Id) {
    return tagRepository.findByimage_id(Id);
  }
  public String getStringOfAllTagsWithId(Long Id) {
     StringBuilder stringWithTags = new StringBuilder();
     List<Tag> list = tagRepository.findByimage_id(Id);
    for (int i = 0; i <list.size() ; i++) {
      Tag index = list.get(i);
      stringWithTags.append(index.getName()+ " : "+ index.getConfidencePercentage() + ", ");
    }
    return stringWithTags.toString();
  }

    public Tag addTag(long Id, String tag, double confidence, Images image){
    Tag newTag = new Tag();
    newTag.setName(tag);
    newTag.setConfidencePercentage(confidence);
    newTag.setImage(image);
    tagRepository.save(newTag);
    return newTag;
    }

    public List<Long> findAllImagesWithTag(String tag){
    return tagRepository.findImageIdsByTagName(tag);

    }
    public List<String> getAllUniqueTags(){
    return tagRepository.findAllUniqueTags();
    }


  public List<String> getAllTagNames() {
    List<String> list = new ArrayList<>();
    return list;
  }
}
