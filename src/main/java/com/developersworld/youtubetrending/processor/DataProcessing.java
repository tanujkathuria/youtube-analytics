package com.developersworld.youtubetrending.processor;

import com.developersworld.youtubetrending.DataInput;
import com.developersworld.youtubetrending.model.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class DataProcessing implements ItemProcessor<DataInput, Data> {
  private static final Logger log = LoggerFactory.getLogger(
    DataProcessing.class
  );

  @Override
  public Data process(DataInput item) throws Exception {
    // need to conver the input to output DATA over here
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm:ss'Z'",
      Locale.ENGLISH
    );
    Data data = new Data();
    data.setVideo_id(item.getVideo_id());
    data.setTitle(item.getTitle());
    data.setChannelTitle(item.getChannelTitle());
    data.setTrending_date(
      LocalDateTime.parse(item.getTrending_date(), formatter)
    );
    // data.setTags(item.getTags());
    data.setComment_count(item.getComment_count());
    data.setLikes(item.getLikes());
    data.setDislikes(item.getDislikes());
    data.setView_count(item.getView_count());
    data.setThumbnail_link(item.getThumbnail_link());
    data.setPublishedAt(LocalDateTime.parse(item.getPublishedAt(), formatter));
    log.info("Converting (" + item + ") into (" + data + ")");
    return data;
  }
}
