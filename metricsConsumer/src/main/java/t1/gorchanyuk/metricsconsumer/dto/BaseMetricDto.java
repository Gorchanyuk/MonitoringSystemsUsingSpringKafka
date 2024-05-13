package t1.gorchanyuk.metricsconsumer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Базовая модель Митрики, без связанных моделей")
public class BaseMetricDto {

    @Schema(description = "Id метрики", example = "1")
    private long id;

    @Schema(description = "Наименование", example = "jvm.memory.used")
    private String name;

    @Schema(description = "Описание", example = "Used JVM memory")
    private String description;

    @Schema(description = "Время создания метрики", example = "")
    private LocalDateTime timestamp;
}
