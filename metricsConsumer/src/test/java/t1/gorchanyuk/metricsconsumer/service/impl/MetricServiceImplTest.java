package t1.gorchanyuk.metricsconsumer.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Measurement;
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.exception.EntityNotFoundException;
import t1.gorchanyuk.metricsconsumer.mapper.MetricMapper;
import t1.gorchanyuk.metricsconsumer.repository.MetricRepository;
import t1.gorchanyuk.metricsconsumer.testutil.ModelGenerator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование етодов класса MetricServiceImpl")
public class MetricServiceImplTest {

    @Mock
    private MetricRepository repository;
    @Mock
    private MetricMapper mapper;
    @InjectMocks
    private MetricServiceImpl metricService;

    @Test
    @DisplayName("Проверка успешной работы метода findById")
    public void testFindById() {

        long id = 1L;
        Metric metric = ModelGenerator.getMetric(id);
        MetricDto expected = ModelGenerator.getMetricDto();
        when(repository.findById(id)).thenReturn(Optional.of(metric));
        when(mapper.mapToMetricDto(metric)).thenReturn(expected);

        MetricDto result = metricService.findById(id);

        assertEquals(expected, result);
        verify(repository, times(1)).findById(anyLong());
        verify(mapper, times(1)).mapToMetricDto(any());
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения EntityNotFoundException в методе findById")
    public void testFindByIdFailure() {

        long id = 1L;
        when(repository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> metricService.findById(id));

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Проверка успешной работы метода findAll")
    public void testFindAll() {
        int pageNumber = 0;
        int pageSize = 50;
        List<Metric> listMetrics =
                List.of(ModelGenerator.getMetric(1), ModelGenerator.getMetric(2), ModelGenerator.getMetric(3));
        Page<Metric> metrics = new PageImpl<>(listMetrics);
        when(repository.findAll(any(PageRequest.class))).thenReturn(metrics);
        when(mapper.mapToBaseMetricDto(any(Metric.class))).thenReturn(ModelGenerator.getBaseMetricDto(1));

        Page<BaseMetricDto> result = metricService.findAll(pageNumber,pageSize);

        assertEquals(listMetrics.size(), result.getSize());
        verify(repository, times(1)).findAll(any(PageRequest.class));
        verify(mapper, times(listMetrics.size())).mapToBaseMetricDto(any(Metric.class));
    }

    @Test
    @DisplayName("Проверка сохзанения метрики")
    public void testSave() {

        Metric metric = new Metric();
        Measurement measurement = mock(Measurement.class);
        metric.setMeasure(List.of(measurement, measurement, measurement));
        when(repository.save(metric)).thenReturn(metric);

        metricService.save(metric);

        verify(repository, times(1)).save(metric);
        verify(measurement, times(metric.getMeasure().size())).setMetric(metric);
    }
}
