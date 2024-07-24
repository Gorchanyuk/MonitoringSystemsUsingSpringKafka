package t1.gorchanyuk.metricsconsumer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.porps.TopicNameProperties;
import t1.gorchanyuk.metricsconsumer.service.KafkaService;
import t1.gorchanyuk.metricsconsumer.service.MetricService;
import t1.gorchanyuk.metricsconsumer.testutil.ModelGenerator;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование методов класса KafkaService")
public class KafkaServiceTest {

    @Mock
    private ObjectMapper mapper;
    @Mock
    private MetricService metricService;
    @Mock
    private KafkaTemplate<String, String> template;
    @Mock
    private TopicNameProperties properties;

    @InjectMocks
    private KafkaService kafkaService;

    @SneakyThrows
    @Test
    @DisplayName("Проверка успешной работы метода listenMetrics")
    public void testListenMetrics() {

        String message = "message with Metric";
        Metric metric = ModelGenerator.getMetric(1);
        List<Metric> metrics = List.of(metric);
        when(mapper.readValue(message, Metric.class)).thenReturn(metric);

        kafkaService.listenMetricsAndObserved(List.of(message));

        verify(mapper, times(1)).readValue(message, Metric.class);
        verify(metricService, times(1)).saveAll(metrics);

    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка отправки сообщения в DLT топик во время исключения в методе listenMetric")
    public void testListenMetricWithInvalidMessage() {

        String topicNameDLT = "topic.DLT";
        String invalidMessage = "Невалидное сообщение";
        when(mapper.readValue(invalidMessage, Metric.class)).thenThrow(JsonProcessingException.class);
        when(properties.getMetricDlt()).thenReturn(topicNameDLT);

        kafkaService.listenMetricsAndObserved(List.of(invalidMessage));

        verify(template, times(1)).send(topicNameDLT, invalidMessage);
    }
}
