package com.example.ics.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Table(name = "TAGS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "image_id"})})
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "tag",nullable = false,updatable = false)
    @Setter
    private String tag;
    @Column(name = "confidence_percentage",nullable = false,updatable = false)
    @Setter
    private double confidencePercentage;


    @ManyToOne(fetch = FetchType.EAGER )
    @JsonBackReference
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    @Setter
    private Images image;

    public Tag() {
    }

    public Tag(String tag, double confidencePercentage, Images image) {
        this.tag = tag;
        this.confidencePercentage = confidencePercentage;
        this.image = image;
    }
}