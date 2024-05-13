package t1.gorchanyuk.metricsproducer.model.metric;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import t1.gorchanyuk.metricsproducer.model.Metric;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель Метрики")
public class MetricRequest implements Metric {

    @Schema(description = "Наименование", example = "jvm.memory.used")
    private String name;

    @Schema(description = "Описание", example = "Used JVM memory")
    private String description;

    @Schema(description = "Единица измерения, или тип передаваемых данных (необязательное поле)", example = "bytes")
    private String baseUnit;

    @Schema(description = "Время создания метрики")
    private LocalDateTime timestamp;

    @Schema(description = "Измерения метрики")
    private List<Measurement> measure;
}
