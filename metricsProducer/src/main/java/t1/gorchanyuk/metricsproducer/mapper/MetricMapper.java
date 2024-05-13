package t1.gorchanyuk.metricsproducer.mapper;

import io.micrometer.core.instrument.Meter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;

import java.time.LocalDateTime;

@Mapper(imports = {LocalDateTime.class})
public interface MetricMapper {

    @Mapping(target = "name", source = "id.name")
    @Mapping(target = "description", source = "id.description")
    @Mapping(target = "baseUnit", source = "id.baseUnit")
    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    @Mapping(target = "measure", expression = "java(meter.measure())")
    MetricFromMeter mapToMetricFromMeter(Meter meter);

}
