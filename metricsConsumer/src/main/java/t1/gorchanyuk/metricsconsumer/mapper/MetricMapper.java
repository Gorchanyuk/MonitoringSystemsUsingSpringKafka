package t1.gorchanyuk.metricsconsumer.mapper;

import org.mapstruct.Mapper;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Metric;

@Mapper
public interface MetricMapper {

    MetricDto mapToMetricDto(Metric metric);

    BaseMetricDto mapToBaseMetricDto(Metric metric);
}


