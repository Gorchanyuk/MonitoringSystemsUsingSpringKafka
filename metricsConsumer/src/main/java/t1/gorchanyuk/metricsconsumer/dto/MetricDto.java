package t1.gorchanyuk.metricsconsumer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель Митрики")
public class MetricDto implements Serializable {

    @Schema(description = "Наименование", example = "jvm.memory.used")
    private String name;

    @Schema(description = "Описание", example = "Used JVM memory")
    private String description;

    @Schema(description = "Единица измерения, или тип передаваемых данных (необязательное поле)", example = "bytes")
    private String baseUnit;

    @Schema(description = "Время создания метрики", example = "")
    private LocalDateTime timestamp;

    @Schema(description = "Измерения метрики")
    private List<MeasurementDto> measure;
}

