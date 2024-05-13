package t1.gorchanyuk.metricsproducer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import t1.gorchanyuk.metricsproducer.porps.TopicNameProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final TopicNameProperties properties;

    @Bean
    public NewTopic metric() {
        return new NewTopic(properties.getMetric(), 1, (short) 1);
    }
}
