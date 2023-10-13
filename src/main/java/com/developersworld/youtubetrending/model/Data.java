package com.developersworld.youtubetrending.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String video_id;
  private String title;
  private LocalDateTime publishedAt;
  private String channelId;
  private String channelTitle;
  private String categoryId;
  private LocalDateTime trending_date;
  // private String tags;
  private String view_count;
  private String likes;
  private String dislikes;
  private String comment_count;
  private String thumbnail_link;
  private String comments_disabled;
  private String ratings_disabled;
}
