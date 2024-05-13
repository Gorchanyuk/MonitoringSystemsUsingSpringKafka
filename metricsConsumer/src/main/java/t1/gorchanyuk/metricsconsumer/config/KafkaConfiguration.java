package t1.gorchanyuk.metricsconsumer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;
import t1.gorchanyuk.metricsconsumer.porps.TopicNameProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final TopicNameProperties properties;

    @Bean
    public NewTopic metric() {
        return new NewTopic(properties.getMetric(), 1, (short) 1);
    }

    @Bean
    public NewTopic dlt() {
        return new NewTopic(properties.getMetricDlt(), 1, (short) 1);
    }

    @Bean
    public CommonErrorHandler errorHandler(KafkaOperations<Object, String> kafkaOperations) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaOperations), new FixedBackOff(1000L, 1));
    }
}
