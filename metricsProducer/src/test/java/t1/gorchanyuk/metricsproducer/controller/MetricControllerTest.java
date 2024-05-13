package t1.gorchanyuk.metricsproducer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import t1.gorchanyuk.metricsproducer.exception.SendMetricException;
import t1.gorchanyuk.metricsproducer.model.metric.Measurement;
import t1.gorchanyuk.metricsproducer.model.metric.MetricRequest;
import t1.gorchanyuk.metricsproducer.service.impl.MetricServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetricController.class)
@DisplayName("Тестирование REST API MetricController")
class MetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricServiceImpl metricsService;

    @Autowired
    private ObjectMapper mapper;

    private MetricRequest metricRequest;

    @BeforeEach
    public void setUp() {
        metricRequest = new MetricRequest();
        metricRequest.setName("name");
        metricRequest.setDescription("description");
        metricRequest.setMeasure(List.of(new Measurement()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование успешного добавления метрик")
    void testSendMetric() {

        mockMvc.perform(MockMvcRequestBuilders.post("/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(metricRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(metricsService, times(1)).sendMetric(metricRequest);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование выбрасывания ошибки во время добавления метрик")
    void testSendMetricFailure() {

        doThrow(SendMetricException.class).when(metricsService).sendMetric(metricRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(metricRequest)))
                .andExpect(status().isInternalServerError());

        verify(metricsService, times(1)).sendMetric(metricRequest);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование успешного добавления метрик из Micrometer")
    void testSendMetricFromMicrometer() {

        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/send/micrometer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(metricsService, times(1)).getMetricsAndSend();
    }
}