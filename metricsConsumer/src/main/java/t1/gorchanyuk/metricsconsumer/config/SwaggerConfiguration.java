package t1.gorchanyuk.metricsconsumer.config;

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
                        .title("Metrics Consumer")
                        .description("Предоставляет методы для взаимодействия с объектами типа Metric")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gorchanyuk")
                                .email("gorchanuk@gmail.com")));
    }
}