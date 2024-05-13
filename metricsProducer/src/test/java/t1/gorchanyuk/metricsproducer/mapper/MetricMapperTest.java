package t1.gorchanyuk.metricsproducer.mapper;

import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tags;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование маппера MetricMapper")
public class MetricMapperTest {

    private static MetricMapper metricMapper;

    @BeforeAll
    public static void setUp(){
        metricMapper = Mappers.getMapper(MetricMapper.class);
    }
    @Test
    @DisplayName("Тест маппинга Meter в MetricFromMeter")
    public void testMapToMetricFromMeter() {

        Meter meter = mock(Meter.class);
        Tags tags = mock(Tags.class);
        Meter.Id id = new Meter.Id("name", tags, "baseUnit", "description", Meter.Type.GAUGE);
        Iterable<Measurement> measurements = Collections.emptyList();
        when(meter.getId()).thenReturn(id);
        when(meter.measure()).thenReturn(measurements);

        MetricFromMeter result = metricMapper.mapToMetricFromMeter(meter);

        assertEquals(id.getName(), result.getName());
        assertEquals(id.getDescription(), result.getDescription());
        assertEquals(id.getBaseUnit(), result.getBaseUnit());
        assertNotNull(result.getTimestamp());
        assertEquals(measurements, result.getMeasure());
    }
}