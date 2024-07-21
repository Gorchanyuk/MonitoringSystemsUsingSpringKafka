package t1.gorchanyuk.metricsconsumer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Metrics Consumer")
                        .description("Предоставляет методы для взаимодействия с объектами типа Metric")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gorchanyuk")
                                .email("gorchanuk@gmail.com")))
                .servers(List.of(
                        new Server().url("http://localhost:" + port)
                ));
    }
}