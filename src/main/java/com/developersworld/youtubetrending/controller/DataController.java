package com.developersworld.youtubetrending.controller;

import com.developersworld.youtubetrending.model.Data;
import com.developersworld.youtubetrending.repos.DataRepos;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@CrossOrigin
public class DataController {
  @Autowired
  DataRepos dataRepos;

  @GetMapping("/all")
  public Page<Data> getAllData() {
    return dataRepos.findAll(
      PageRequest.of(0, 20, Sort.by("publishedAt").descending())
    );
  }

  @GetMapping("/all/{page}/{size}")
  public Page<Data> getDataBasedOnPageNumberAndSize(
    @PathVariable Integer page,
    @PathVariable Integer size
  ) {
    return dataRepos.findAll(
      PageRequest.of(page - 1, size, Sort.by("publishedAt").descending())
    );
  }

  @GetMapping("/all/{trendingDate}/{page}/{size}")
  public Page<Data> getDataBasedOnYearPageAndSize(
    @PathVariable String trendingDate,
    @PathVariable Integer page,
    @PathVariable Integer size
  ) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm:ss'Z'",
      Locale.ENGLISH
    );
    try {
      LocalDateTime trendingDateNew = LocalDateTime.parse(
        trendingDate,
        formatter
      );
      return dataRepos.findByTrending__date(
        trendingDateNew,
        PageRequest.of(page - 1, size)
      );
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @GetMapping("/all/{trendingDate}")
  public List<Data> getDataBasedOnDate(@PathVariable String trendingDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm:ss'Z'",
      Locale.ENGLISH
    );
    try {
      LocalDateTime trendingDateNew = LocalDateTime.parse(
        trendingDate,
        formatter
      );
      return dataRepos.findByTrending__date2(trendingDateNew);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
