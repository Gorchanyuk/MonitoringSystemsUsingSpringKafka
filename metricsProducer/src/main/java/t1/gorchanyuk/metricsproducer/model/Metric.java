package t1.gorchanyuk.metricsproducer.model;

import java.time.LocalDateTime;

public interface Metric {

    String getName();

    String getDescription();


    LocalDateTime getTimestamp();

}
