package t1.gorchanyuk.metricsconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.exception.DeserializationException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    private final ObjectMapper mapper;
    private final MetricService metricService;

    @KafkaListener(topics = "${topic.name.metric}", id = "metric-consumer")
    public void listenMetric(String message) {

        Metric metric;
        try {
            metric = mapper.readValue(message, Metric.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException();
        }
        metricService.save(metric);
    }

    @KafkaListener(topics = "${topic.name.metric-dlt}", id = "metric-consumer-DLT")
    public void listenDlt(String msg, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Ошибка при попытке десериализации сообщения {}, в топике: {} ", msg, topic);
    }
}
