package com.developersworld.youtubetrending;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataInput {
  private String video_id;
  private String title;
  private String publishedAt;
  private String channelId;
  private String channelTitle;
  private String categoryId;
  private String trending_date;
  private String tags;
  private String view_count;
  private String likes;
  private String dislikes;
  private String comment_count;
  private String thumbnail_link;
  private String comments_disabled;
  private String ratings_disabled;
}
