package t1.gorchanyuk.metricsproducer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Metrics Producer")
                        .description("Отправляет метрики в Kafka")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gorchanyuk")
                                .email("gorchanuk@gmail.com")));
    }
}