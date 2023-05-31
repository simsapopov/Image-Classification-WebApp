package com.example.ics.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Entity
@Getter
@Table(name = "IMAGE")
public class Images implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id",nullable = false,updatable = false)
  @Setter()
  private Long id;
  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JsonManagedReference
  @Setter
  private List<Tag> tags;

  @Column(name = "name")
  @Setter
  private String name;
  @Column(name = "url",updatable = false,nullable = false)
  @Setter
  private String url;



}