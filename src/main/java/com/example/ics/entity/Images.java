package com.example.ics.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
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
  @Column(name = "url",updatable = false)
  @Setter
  private String url;
  @Column(name = "imgurlUrl",updatable = false,nullable = false)
  @Setter
  private String imgurlUrl;
  @Column(name = "analyzedAt")
  @Temporal(TemporalType.TIMESTAMP)
  @Setter
  private Date analyzedAt;
  @Column(name = "width")
  @Setter
  private Integer  width;

  @Column(name = "hash")
  @Setter
  private String  hash;
}