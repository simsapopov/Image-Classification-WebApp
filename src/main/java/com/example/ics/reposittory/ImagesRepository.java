package com.example.ics.reposittory;

import com.example.ics.entity.Images;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUrl(String url);

    Images findByImgurlUrl(String url);

    @Transactional
    void deleteById(Long id);

    Optional<Images> findById(Long id);

    Images findByHash(String hash);

    @Query("SELECT i FROM Images i JOIN i.tags t WHERE t.tag LIKE :tagName%")
    Page<Images> findAllByTagName(String tagName, Pageable pageable);


    Images save(Images image);

}