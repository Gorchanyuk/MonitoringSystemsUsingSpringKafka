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
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.exception.DeserializationException;
import t1.gorchanyuk.metricsconsumer.service.MetricService;
import t1.gorchanyuk.metricsconsumer.testutil.ModelGenerator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование етодов класса KafkaServiceImpl")
public class KafkaServiceImplTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private MetricService metricService;

    @InjectMocks
    private KafkaServiceImpl kafkaService;

    @SneakyThrows
    @Test
    @DisplayName("Проверка успешной работы метода listenMetric")
    public void testListenMetric() {

        String message = "message with Metric";
        Metric metric = ModelGenerator.getMetric(1);
        when(mapper.readValue(message, Metric.class)).thenReturn(metric);

        kafkaService.listenMetric(message);

        verify(mapper, times(1)).readValue(message, Metric.class);
        verify(metricService, times(1)).save(metric);

    }

    @SneakyThrows
    @Test
    @DisplayName("Проверка выброса исключения во время работы метода listenMetric")
    public void testListenMetricWithInvalidMessage() {

        String invalidMessage = "Невалидное сообщение";

        when(mapper.readValue(invalidMessage, Metric.class)).thenThrow(JsonProcessingException.class);

        assertThrows(DeserializationException.class, () -> kafkaService.listenMetric(invalidMessage));
    }
}
