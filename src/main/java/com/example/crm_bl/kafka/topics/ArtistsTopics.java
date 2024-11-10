package com.example.crm_bl.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ArtistsTopics {
    @Bean
    public NewTopic getAllArtistsTopic() {
        return TopicBuilder.name("get-artists")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getArtistTopic() {
        return TopicBuilder.name("get-artist")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic createArtistTopic() {
        return TopicBuilder.name("save-artist")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
