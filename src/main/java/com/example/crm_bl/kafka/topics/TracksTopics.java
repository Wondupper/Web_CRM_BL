package com.example.crm_bl.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TracksTopics {
    @Bean
    public NewTopic getAllTracksTopic() {
        return TopicBuilder.name("get-tracksbl")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getTrackTopic() {
        return TopicBuilder.name("get-trackbl")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
