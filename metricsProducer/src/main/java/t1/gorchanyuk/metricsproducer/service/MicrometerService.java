package t1.gorchanyuk.metricsproducer.service;

import t1.gorchanyuk.metricsproducer.model.metric.MetricFromMeter;

import java.util.List;

public interface MicrometerService {

    List<MetricFromMeter> getMetrics();
}
