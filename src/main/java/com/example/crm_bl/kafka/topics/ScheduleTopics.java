package com.example.crm_bl.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ScheduleTopics {
    @Bean
    public NewTopic getAllScheduleTopic() {
        return TopicBuilder.name("get-allSchedule")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getScheduleTopic() {
        return TopicBuilder.name("get-schedule")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic createScheduleTopic() {
        return TopicBuilder.name("save-schedule")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
