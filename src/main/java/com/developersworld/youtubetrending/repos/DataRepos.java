package com.developersworld.youtubetrending.repos;

import com.developersworld.youtubetrending.model.Data;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepos extends PagingAndSortingRepository<Data, Integer> {
  @Query(
    value = "SELECT * FROM data WHERE trending_date=?1 ORDER BY view_count DESC",
    nativeQuery = true
  )
  Page<Data> findByTrending__date(
    LocalDateTime trendingDate,
    PageRequest pageRequest
  );

  @Query(
    value = "SELECT * FROM data WHERE trending_date=?1",
    nativeQuery = true
  )
  List<Data> findByTrending__date2(LocalDateTime trendingDate);
}
