package t1.gorchanyuk.metricsproducer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import t1.gorchanyuk.metricsproducer.exception.SendMetricException;
import t1.gorchanyuk.metricsproducer.model.Metric;
import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;
import t1.gorchanyuk.metricsproducer.porps.TopicNameProperties;
import t1.gorchanyuk.metricsproducer.service.MetricService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

    private final KafkaTemplate<String, Metric> kafkaTemplate;
    private final MicrometerServiceImpl micrometerService;
    private final TopicNameProperties properties;

    @Override
    public void sendMetric(Metric metric) {
        CompletableFuture<SendResult<String, Metric>> future = kafkaTemplate.send(properties.getMetric(), metric);
        try {
            future.get();
            log.info("Сообщение с метрикой {} успешно отправленно в Kafka", metric);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Произошла ошибка {} во время выполнения отправки метрики {} в Kafka",
                    e.getMessage(),
                    metric.getName());
            throw new SendMetricException(e);
        }
    }

    @Override
    public void getMetricsAndSend() {

        List<MetricFromMeter> metrics = micrometerService.getMetrics();
        metrics.forEach(this::sendMetric);
    }
}