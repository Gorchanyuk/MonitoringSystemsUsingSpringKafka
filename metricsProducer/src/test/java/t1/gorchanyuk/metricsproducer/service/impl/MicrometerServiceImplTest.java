package t1.gorchanyuk.metricsproducer.service.impl;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import t1.gorchanyuk.metricsproducer.mapper.MetricMapper;
import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование класса MicrometerServiceImpl")
public class MicrometerServiceImplTest {

    @InjectMocks
    private MicrometerServiceImpl micrometerService;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private MetricMapper mapper;

    @Test
    @DisplayName("getMetrics должен получить все метрики из Micrometer")
    public void testGetMetrics() {

        Meter meter = mock(Meter.class);
        List<Meter> meters = List.of(meter, meter);
        when(meterRegistry.getMeters()).thenReturn(meters);
        MetricFromMeter metric = new MetricFromMeter();
        when(mapper.mapToMetricFromMeter(any(Meter.class))).thenReturn(metric);

        List<MetricFromMeter> metrics = micrometerService.getMetrics();

        Assertions.assertEquals(meters.size(), metrics.size());
    }
}