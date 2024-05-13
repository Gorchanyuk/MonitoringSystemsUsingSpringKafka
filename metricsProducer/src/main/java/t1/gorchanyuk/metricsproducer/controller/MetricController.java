package t1.gorchanyuk.metricsproducer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import t1.gorchanyuk.metricsproducer.controller.api.MetricControllerApi;
import t1.gorchanyuk.metricsproducer.model.metric.MetricRequest;
import t1.gorchanyuk.metricsproducer.service.MetricService;

@RestController
@RequiredArgsConstructor
public class MetricController implements MetricControllerApi {

    private final MetricService metricService;

    @Override
    public ResponseEntity<Void> sendMetric(MetricRequest metric) {

        metricService.sendMetric(metric);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> sendMetricFromMicrometer() {

        metricService.getMetricsAndSend();
        return ResponseEntity.ok().build();
    }
}