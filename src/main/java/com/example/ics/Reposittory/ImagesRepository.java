package com.example.ics.Reposittory;
import com.example.ics.Entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUrl(String url);
    Images save(Images image);
}