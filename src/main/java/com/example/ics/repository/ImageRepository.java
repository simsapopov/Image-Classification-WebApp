package com.example.ics.repository;

import com.example.ics.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByUrl(String url);

    Image findByImgurlUrl(String url);

    @Transactional
    void deleteById(Long id);

    Optional<Image> findById(Long id);

    Image findByHash(String hash);

    @Query("SELECT i FROM Image i JOIN i.tags t WHERE t.tag LIKE :tagName%")
    Page<Image> findAllByTagName(String tagName, Pageable pageable);


    Image save(Image image);

}