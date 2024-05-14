package t1.gorchanyuk.metricsconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.porps.TopicNameProperties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    private final ObjectMapper mapper;
    private final MetricService metricService;
    private final KafkaTemplate<String, String> template;
    private final TopicNameProperties properties;

    @KafkaListener(topics = "${topic.name.metric}", id = "metric-consumer")
    public void listenMetrics(List<String> messages) {
        log.info("Получено {} сообщенй из Kafka для обработки и сохранения в БД", messages.size());
        List<Metric> metrics = messages.stream()
                .flatMap(message -> {
                    try {
                        return Optional.ofNullable(mapper.readValue(message, Metric.class)).stream();
                    } catch (JsonProcessingException e) {
                        template.send(properties.getMetricDlt(), message); // отправляем в DLT
                        return Stream.empty();
                    }
                })
                .toList();
        metricService.saveAll(metrics);
    }

    @KafkaListener(topics = "${topic.name.metric-dlt}", id = "metric-consumer-DLT")
    public void listenDlt(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("Ошибка при попытке десериализации сообщения {}, в топике: {} ", msg, topic);
    }
}
