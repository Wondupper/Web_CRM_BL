package com.example.crm_bl.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class GenresTopics {
    @Bean
    public NewTopic getAllGenresTopic() {
        return TopicBuilder.name("get-genres")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getGenreTopic() {
        return TopicBuilder.name("get-genre")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic createGenreTopic() {
        return TopicBuilder.name("save-genre")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
