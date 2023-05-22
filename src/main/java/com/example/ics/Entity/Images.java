package com.example.ics.Entity;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Images {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
  private List<Tag> tags;
    // TODO @Basic
  private String name;
  private String url;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
        this.id = id;
    }

  public String getName() {
        return name;
    }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}