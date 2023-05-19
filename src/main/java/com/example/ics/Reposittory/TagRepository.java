package com.example.ics.Reposittory;
import com.example.ics.Entity.Images;
import com.example.ics.Entity.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByimage_id(Long imageId);
}
