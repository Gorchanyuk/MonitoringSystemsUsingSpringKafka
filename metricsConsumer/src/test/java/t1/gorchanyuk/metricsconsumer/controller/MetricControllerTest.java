package t1.gorchanyuk.metricsconsumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.exception.EntityNotFoundException;
import t1.gorchanyuk.metricsconsumer.service.MetricService;
import t1.gorchanyuk.metricsconsumer.testutil.ModelGenerator;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetricController.class)
@DisplayName("Тестирование REST API MetricController")
public class MetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MetricService metricService;

    @SneakyThrows
    @Test
    @DisplayName("Тестирование получения всех метрик (с пагинацией)")
    public void testFindAll() {
        List<BaseMetricDto> listMetrics = List.of(ModelGenerator.getBaseMetricDto(1), ModelGenerator.getBaseMetricDto(2));

        Page<BaseMetricDto> expectedMetrics = new PageImpl<>(listMetrics);

        when(metricService.findAll(anyInt(), anyInt())).thenReturn(expectedMetrics);

        mockMvc.perform(MockMvcRequestBuilders.get("/metrics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedMetrics)));

        verify(metricService, times(1)).findAll(anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    @DisplayName("Тестирование успешного получения метрики по заданному id")
    public void testFindById() {

        long id = 1L;
        MetricDto expectedMetric = ModelGenerator.getMetricDto();
        when(metricService.findById(id)).thenReturn(expectedMetric);

        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedMetric)));

        verify(metricService, times(1)).findById(id);
    }

    @SneakyThrows
    @Test
    @DisplayName("Тестирование получения метрики по заданному id")
    public void testFindByIdFailed() {

        long id = 1L;
        when(metricService.findById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/metrics/" + id))
                .andExpect(status().isNotFound());

        verify(metricService, times(1)).findById(id);
    }
}
