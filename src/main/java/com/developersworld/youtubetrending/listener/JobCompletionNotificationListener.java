package com.developersworld.youtubetrending.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
  private static final Logger log = LoggerFactory.getLogger(
    JobCompletionNotificationListener.class
  );

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

      jdbcTemplate
        .query(
          "SELECT video_id, title FROM data",
          (rs, row) ->
            "video_id 1 " + rs.getString(1) + "title " + rs.getString(2)
        )
        .forEach(data -> log.info("Found <{{}}> in the database.", data));
    }
  }
}
