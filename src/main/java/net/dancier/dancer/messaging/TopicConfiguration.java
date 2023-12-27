package net.dancier.dancer.messaging;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class TopicConfiguration {
    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        
        return new NewTopic("profile-updated", 1, (short) 1);
    }

}
