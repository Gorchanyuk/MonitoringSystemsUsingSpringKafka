package t1.gorchanyuk.metricsproducer.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import t1.gorchanyuk.metricsproducer.model.error.ErrorResponseDto;
import t1.gorchanyuk.metricsproducer.model.metric.MetricRequest;

@Tag(name = "MetricController", description = "Методы для взаимодействия с объектами типа Metric")
public interface MetricControllerApi {

    @PostMapping(value = "/metrics",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Создание новой записи метрики",
            responses = {@ApiResponse(responseCode = "200", description = "Метрика успешно принята"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<Void> sendMetric(@Parameter(description = "Параметр для указания типа искомой статистики")
                                    @RequestBody MetricRequest metric);

    @GetMapping(value = "/metrics/send/micrometer", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Отправка метрик из Micrometer",
            description = "При отправке запроса в коде получаются метрики из Micrometer и отправляются в Kafka",
            responses = {@ApiResponse(responseCode = "200", description = "Метрика успешно сгенерированны и отправлены"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<Void> sendMetricFromMicrometer();

}


