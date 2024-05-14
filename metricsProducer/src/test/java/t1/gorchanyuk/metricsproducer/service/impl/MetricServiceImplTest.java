package t1.gorchanyuk.metricsproducer.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import t1.gorchanyuk.metricsproducer.exception.SendMetricException;
import t1.gorchanyuk.metricsproducer.model.Metric;
import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;
import t1.gorchanyuk.metricsproducer.model.metric.MetricRequest;
import t1.gorchanyuk.metricsproducer.porps.TopicNameProperties;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceImplTest {

    @Mock
    private KafkaTemplate<String, Metric> kafkaTemplate;

    @Mock
    private TopicNameProperties properties;

    @Mock
    private MicrometerServiceImpl micrometerService;

    @InjectMocks
    private MetricServiceImpl metricsProducerService;

    @Test
    @DisplayName("Проверка успешной отправки метрик")
    void testSendMetric() {

        MetricRequest metric = new MetricRequest();
        metric.setName("testMetric");
        CompletableFuture<SendResult<String, Metric>> sendResultCompletableFuture =
                CompletableFuture.completedFuture(new SendResult<>(null, null));
        when(kafkaTemplate.send(anyString(), any(Metric.class))).thenReturn(sendResultCompletableFuture);
        when(properties.getMetric()).thenReturn("topic-name");
        metricsProducerService.sendMetric(metric);

        verify(kafkaTemplate).send(anyString(), eq(metric));
    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка обработки ошибок во время отправки метрики")
    void testSendMetricFailure() {

        MetricRequest metric = new MetricRequest();
        metric.setName("testMetric");
        CompletableFuture<SendResult<String, Metric>> future = mock(CompletableFuture.class);
        when(properties.getMetric()).thenReturn("topic-name");
        when(kafkaTemplate.send(anyString(), any(Metric.class))).thenReturn(future);
        when(future.get()).thenThrow(new InterruptedException("Test Exception"));

        Assertions.assertThrows(SendMetricException.class, () -> metricsProducerService.sendMetric(metric));
    }

    @Test
    @DisplayName("Проверка получения метрик из Micrometer и отправки")
    void testGetMetricsAndSend() {

        MetricFromMeter metric = new MetricFromMeter();
        List<MetricFromMeter> metrics = List.of(metric, metric);
        when(properties.getMetric()).thenReturn("topic-name");
        when(micrometerService.getMetrics()).thenReturn(metrics);
        when(kafkaTemplate.send(anyString(), any(Metric.class))).thenReturn(mock(CompletableFuture.class));

        metricsProducerService.getMetricsAndSend();

        verify(micrometerService, times(1)).getMetrics();
        verify(kafkaTemplate, times(metrics.size())).send(anyString(), any(Metric.class));
    }
}