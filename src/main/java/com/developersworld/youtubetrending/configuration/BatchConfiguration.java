package com.developersworld.youtubetrending.configuration;

import com.developersworld.youtubetrending.DataInput;
import com.developersworld.youtubetrending.listener.JobCompletionNotificationListener;
import com.developersworld.youtubetrending.model.Data;
import com.developersworld.youtubetrending.processor.DataProcessing;
import java.net.MalformedURLException;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {
  String[] FIELD_NAMES = new String[] {
    "video_id",
    "title",
    "publishedAt",
    "channelId",
    "channelTitle",
    "categoryId",
    "trending_date",
    "tags",
    "view_count",
    "likes",
    "dislikes",
    "comment_count",
    "thumbnail_link",
    "comments_disabled",
    "ratings_disabled",
  };

  @Bean
  public FlatFileItemReader<DataInput> reader() throws MalformedURLException {
    // UrlResource resource = new UrlResource(
    //   "https://youtube-analyzer-bucket.s3.ap-south-1.amazonaws.com/youtube_trending_data.csv"
    // );

    return new FlatFileItemReaderBuilder<DataInput>()
      .name("personItemReader")
      .resource(new ClassPathResource("youtube_trending_data.csv"))
      // this is required for the AWS hosting
      // .resource(resource)
      .delimited()
      .names(FIELD_NAMES)
      .fieldSetMapper(
        new BeanWrapperFieldSetMapper<DataInput>() {

          {
            setTargetType(DataInput.class);
          }
        }
      )
      .build();
  }

  @Bean
  public DataProcessing processor() {
    return new DataProcessing();
  }

  @Bean
  public JdbcBatchItemWriter<Data> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Data>()
      .itemSqlParameterSourceProvider(
        new BeanPropertyItemSqlParameterSourceProvider<>()
      )
      .sql(
        "INSERT INTO data (video_id,title,published_at,channel_id,channel_title,category_id,trending_date,view_count,likes,dislikes,comment_count,thumbnail_link,comments_disabled,ratings_disabled)" +
        " VALUES (:video_id,:title,:publishedAt,:channelId,:channelTitle,:categoryId,:trending_date,:view_count,:likes,:dislikes,:comment_count,:thumbnail_link,:comments_disabled,:ratings_disabled)"
      )
      .dataSource(dataSource)
      .build();
  }

  @Bean
  public Job importUserJob(
    JobRepository jobRepository,
    JobCompletionNotificationListener listener,
    Step step1
  ) {
    return new JobBuilder("importUserJob", jobRepository)
      .incrementer(new RunIdIncrementer())
      .listener(listener)
      .flow(step1)
      .end()
      .build();
  }

  @Bean
  public Step step1(
    JobRepository jobRepository,
    PlatformTransactionManager transactionManager,
    JdbcBatchItemWriter<Data> writer
  )
    throws MalformedURLException {
    return new StepBuilder("step1", jobRepository)
      .<DataInput, Data>chunk(10, transactionManager)
      .reader(reader())
      .processor(processor())
      .writer(writer)
      .build();
  }
}
