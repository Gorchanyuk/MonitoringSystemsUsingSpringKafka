package t1.gorchanyuk.metricsconsumer.service;

import org.springframework.data.domain.Page;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Metric;

import java.util.List;

public interface MetricService {

    MetricDto findById(long id);

    Page<BaseMetricDto> findAll(int pageNumber, int pageSize);

    void saveAll(List<Metric> metrics);
}
