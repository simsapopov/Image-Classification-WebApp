package com.example.ics.reposittory;

import com.example.ics.entity.Images;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUrl(String url);
    Images findByImgurlUrl(String url);
    @Transactional
    void deleteById(Long id);
    Optional<Images> findById(Long id);

    Images findByHash(String hash);

    Page<Images> findAllByOrderByAnalyzedAtAsc(Pageable pageable);
    Page<Images> findAllByOrderByAnalyzedAtDesc(Pageable pageable);
    Images save(Images image);
    List<Images> findByName(String name, Pageable pageable);


    List<Images> findByName(String name);
}