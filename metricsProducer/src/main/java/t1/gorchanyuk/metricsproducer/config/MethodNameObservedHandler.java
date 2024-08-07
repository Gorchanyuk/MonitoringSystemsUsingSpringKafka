package t1.gorchanyuk.metricsproducer.config;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Slf4j
@Component
class MethodNameObservedHandler implements ObservationHandler<Observation.Context> {

    @Override
    public void onStart(Observation.Context context) {
        log.info("Before running the observation for context [{}], methodName [{}]",
                context.getName(),
                getMethodNameFromContext(context)
        );
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("After running the observation for context [{}], methodName [{}]",
                context.getName(),
                getMethodNameFromContext(context)
        );
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }

    private String getMethodNameFromContext(Observation.Context context) {

        return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                .filter(keyValue -> "method".equals(keyValue.getKey()))
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN");
    }
}