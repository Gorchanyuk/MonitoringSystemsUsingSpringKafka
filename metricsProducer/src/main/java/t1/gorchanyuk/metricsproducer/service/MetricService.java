package t1.gorchanyuk.metricsproducer.service;

import t1.gorchanyuk.metricsproducer.model.Metric;

public interface MetricService {

    void sendMetric(Metric metric);

    void getMetricsAndSend();
}