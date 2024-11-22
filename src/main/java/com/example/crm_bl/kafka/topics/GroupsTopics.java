package com.example.crm_bl.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class GroupsTopics {
    @Bean
    public NewTopic getAllGroupsTopic() {
        return TopicBuilder.name("get-groupsbl")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getGroupTopic() {
        return TopicBuilder.name("get-groupbl")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
