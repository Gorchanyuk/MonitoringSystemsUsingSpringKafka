package t1.gorchanyuk.metricsconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MetricsConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsConsumerApplication.class, args);
	}

}
