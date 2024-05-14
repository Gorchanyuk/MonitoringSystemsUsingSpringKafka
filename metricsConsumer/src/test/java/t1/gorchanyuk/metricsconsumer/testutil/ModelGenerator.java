package t1.gorchanyuk.metricsconsumer.testutil;

import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MeasurementDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Measurement;
import t1.gorchanyuk.metricsconsumer.entity.Metric;

import java.time.LocalDateTime;
import java.util.List;

public class ModelGenerator {

    public static Metric getMetric(long id) {
        return Metric.builder()
                .id(id)
                .name("test.metric")
                .description("description test metric")
                .baseUnit("unit")
                .timestamp(LocalDateTime.now())
                .measure(List.of(getMeasurement(1), getMeasurement(2), getMeasurement(3)))
                .build();
    }

    private static Measurement getMeasurement(long num) {
        return Measurement.builder()
                .id(num)
                .statistic("statistic " + num)
                .value("value " + num)
                .build();
    }

    public static BaseMetricDto getBaseMetricDto(long id) {

        return BaseMetricDto.builder()
                .id(id)
                .name("name")
                .description("description")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static MetricDto getMetricDto() {
        return MetricDto.builder()
                .name("name")
                .description("description")
                .baseUnit("base unit")
                .timestamp(LocalDateTime.now())
                .measure(List.of(getMeasurementDto(1), getMeasurementDto(2), getMeasurementDto(3)))
                .build();
    }

    private static MeasurementDto getMeasurementDto(int id) {
        return MeasurementDto.builder()
                .statistic("statistic " + id)
                .value("value " + id)
                .build();
    }
}
