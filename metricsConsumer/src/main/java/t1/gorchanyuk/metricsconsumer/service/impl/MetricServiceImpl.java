package t1.gorchanyuk.metricsconsumer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.entity.Metric;
import t1.gorchanyuk.metricsconsumer.exception.EntityNotFoundException;
import t1.gorchanyuk.metricsconsumer.mapper.MetricMapper;
import t1.gorchanyuk.metricsconsumer.repository.MetricRepository;
import t1.gorchanyuk.metricsconsumer.service.MetricService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricServiceImpl implements MetricService {

    private final MetricRepository repository;
    private final MetricMapper mapper;

    @Override
    public MetricDto findById(long id) {

        Metric metric = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Метрика с Id: " + id + " не найдена"));

        return mapper.mapToMetricDto(metric);
    }

    @Override
    public Page<BaseMetricDto> findAll(int pageNumber, int pageSize) {

        Page<Metric> metrics = repository.findAll(PageRequest.of(pageNumber, pageSize));
        List<BaseMetricDto> metricDtos = metrics.stream()
                .map(mapper::mapToBaseMetricDto)
                .toList();

        return new PageImpl<>(metricDtos, metrics.getPageable(), metrics.getTotalElements());
    }

    @Override
    @Transactional
    public void saveAll(List<Metric> metrics) {

        metrics.forEach(this::setMetricForAllMeasurements);

        repository.saveAll(metrics);
    }

    private void setMetricForAllMeasurements(Metric metric) {

        metric.getMeasure()
                .forEach(measure -> measure.setMetric(metric));
    }
}