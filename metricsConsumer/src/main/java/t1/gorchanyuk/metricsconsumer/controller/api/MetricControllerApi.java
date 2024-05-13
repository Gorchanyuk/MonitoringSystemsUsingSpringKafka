package t1.gorchanyuk.metricsconsumer.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import t1.gorchanyuk.metricsconsumer.dto.BaseMetricDto;
import t1.gorchanyuk.metricsconsumer.dto.ErrorResponseDto;
import t1.gorchanyuk.metricsconsumer.dto.MetricDto;

@Tag(name = "MetricController", description = "Методы для взаимодействия с объектами типа Metric")
public interface MetricControllerApi {

    @GetMapping(value = "/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение всех метрик",
            description = "Возвращает список метрик, согласно заданным параметрам пагинации",
            responses = {@ApiResponse(responseCode = "200", description = "Операция завершена успешно")
            })
    ResponseEntity<Page<BaseMetricDto>> findAll(@Parameter(description = "Номер страницы для пагинации (по умолчанию 0)")
                                                @RequestParam(name = "pageNumber", required = false, defaultValue = "0")
                                                int pageNumber,

                                                @Parameter(description = "Количество элементов на странице (по умолчанию 50)")
                                                @RequestParam(name = "pageSize", required = false, defaultValue = "50")
                                                int pageSize);

    @GetMapping(value = "/metrics/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение метрики по заданному Id",
            responses = {@ApiResponse(responseCode = "200", description = "Операция завершена успешно"),
                    @ApiResponse(responseCode = "404", description = "Объект не найден",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<MetricDto> findById(@Parameter(description = "id искомой метрики")
                                       @PathVariable("id") long id);
}
