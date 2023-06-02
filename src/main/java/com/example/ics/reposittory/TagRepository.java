package com.example.ics.reposittory;

import com.example.ics.entity.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByImage_id(Long imageId);
    @Transactional
    void deleteAllByImage_Id(Long imageId);

    @Query("SELECT tag.image.id FROM Tag tag WHERE tag.tag = :tagName")
    List<Long> findImageIdsByTagName(@Param("tagName") String tagName);

    @Query("SELECT DISTINCT tag.tag FROM Tag tag")
    List<String> findAllUniqueTags();

}
