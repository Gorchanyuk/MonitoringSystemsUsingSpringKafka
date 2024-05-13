package t1.gorchanyuk.metricsconsumer.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.testutil.ModelGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Тестирование маппера MetricMapper")
public class MetricMapperTest {

    private static MetricMapper metricMapper;

    @BeforeAll
    public static void setUp() {
        metricMapper = Mappers.getMapper(MetricMapper.class);
    }

    @Test
    @DisplayName("Тест маппинга из Metric в MetricDto")
    public void testMapToMetricDto() {
        Metric metric = ModelGenerator.getMetric(1);

        MetricDto result = metricMapper.mapToMetricDto(metric);

        assertEquals(metric.getName(), result.getName());
        assertEquals(metric.getDescription(), result.getDescription());
        assertEquals(metric.getBaseUnit(), result.getBaseUnit());
        assertNotNull(result.getTimestamp());
        assertEquals(metric.getMeasure().size(), result.getMeasure().size());
    }

    @Test
    @DisplayName("Тест маппинга из Metric в MetricDto")
    public void testMapToBaseMetricDto() {
        Metric metric = ModelGenerator.getMetric(1);

        BaseMetricDto result = metricMapper.mapToBaseMetricDto(metric);

        assertEquals(metric.getId(), result.getId());
        assertEquals(metric.getName(), result.getName());
        assertEquals(metric.getDescription(), result.getDescription());
        assertNotNull(result.getTimestamp());
    }
}
