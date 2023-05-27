package com.example.ics.Reposittory;
import com.example.ics.Entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUrl(String url);
    Images findByid(Long id);
    Images save(Images image);
    List<Images> findByName(String name);
}