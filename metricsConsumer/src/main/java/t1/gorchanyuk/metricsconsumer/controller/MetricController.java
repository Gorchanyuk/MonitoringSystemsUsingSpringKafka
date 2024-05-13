package t1.gorchanyuk.metricsconsumer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import t1.gorchanyuk.metricsconsumer.controller.api.MetricControllerApi;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;
import t1.gorchanyuk.metricsconsumer.service.MetricService;

@RestController
@RequiredArgsConstructor
public class MetricController implements MetricControllerApi {

    private final MetricService metricService;

    @Override
    public ResponseEntity<Page<BaseMetricDto>> findAll(int pageNumber, int pageSize) {

        Page<BaseMetricDto> metrics = metricService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(metrics);
    }

    @Override
    public ResponseEntity<MetricDto> findById(long id) {
        MetricDto metric = metricService.findById(id);
        return ResponseEntity.ok().body(metric);
    }
}