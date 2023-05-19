package com.example.ics.Entity;

import jakarta.persistence.*;

import java.awt.*;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;
    private double confidencePercentage;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Images image;

    public Tag(Long id, String name, double confidencePercentage, Images image) {
        this.id = id;
        this.tag = name;
        this.confidencePercentage = confidencePercentage;
        this.image = image;
    }

    public Tag() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return tag;
    }

    public void setName(String name) {
        this.tag = name;
    }

    public double getConfidencePercentage() {
        return confidencePercentage;
    }

    public void setConfidencePercentage(double confidencePercentage) {
        this.confidencePercentage = confidencePercentage;
    }

    public Images getImage() {
        return image;
    }

    public void setImage(Images image) {
        this.image = image;
    }
}