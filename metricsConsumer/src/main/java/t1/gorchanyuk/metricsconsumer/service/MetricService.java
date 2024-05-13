package t1.gorchanyuk.metricsconsumer.service;

import org.springframework.data.domain.Page;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Metric;

public interface MetricService {

    MetricDto findById(long id);

    Page<BaseMetricDto> findAll(int pageNumber, int pageSize);

    void save(Metric metric);
}
