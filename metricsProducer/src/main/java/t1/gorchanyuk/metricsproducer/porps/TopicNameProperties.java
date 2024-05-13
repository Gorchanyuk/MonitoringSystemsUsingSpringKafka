package t1.gorchanyuk.metricsproducer.porps;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "topic.name")
public class TopicNameProperties {

    private String metric;
}
