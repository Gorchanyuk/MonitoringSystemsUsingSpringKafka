package t1.gorchanyuk.metricsproducer.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import t1.gorchanyuk.metricsproducer.mapper.MetricMapper;
import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;
import t1.gorchanyuk.metricsproducer.service.MicrometerService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MicrometerServiceImpl implements MicrometerService {

    private final MeterRegistry meterRegistry;
    private final MetricMapper mapper;

    @Observed(name = "MicrometerService.getMetrics",
            contextualName = "getMetrics",
            lowCardinalityKeyValues = {"package", "service"})
    @Override
    public List<MetricFromMeter> getMetrics() {
        log.info("Получение метрик из meterRegistry");
        return meterRegistry.getMeters().stream()
                .map(mapper::mapToMetricFromMeter)
                .toList();
    }
}