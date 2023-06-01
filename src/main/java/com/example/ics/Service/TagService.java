package com.example.ics.Service;

import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;
import com.example.ics.Reposittory.TagRepository;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    @PersistenceContext
    private EntityManager entityManager;
    private final TagRepository tagRepository;

    public List<Tag> getAllTagsWithId(Long Id) {
        return tagRepository.findByImage_id(Id);
    }

    public void addTags(List<Tag> tags, Images image) {
        for (int i = 0; i < tags.size(); i++) {
            tagRepository.save(tags.get(i));
        }
    }

    public List<Long> getImageIdsBySequence(String sequence) {
        String queryString = "SELECT t.image.id FROM Tag t WHERE t.tag LIKE :sequence";
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("sequence", "%" + sequence + "%");
        return query.getResultList();
    }

    public String getStringOfAllTagsWithId(Long Id) {
        StringBuilder stringWithTags = new StringBuilder();
        List<Tag> list = tagRepository.findByImage_id(Id);
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public void deleteTagsWithId(Long id) {
        tagRepository.deleteById(id);
    }

    public List<Long> findAllImagesWithTag(String tag) {
        return tagRepository.findImageIdsByTagName(tag);

    }

    public List<String> getAllUniqueTags() {
        return tagRepository.findAllUniqueTags();
    }

}
