package t1.gorchanyuk.metricsproducer.exception;

public class SendMetricException extends RuntimeException {

    public SendMetricException(Exception e) {
        super(e);
    }
}
