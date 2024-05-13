package t1.gorchanyuk.metricsproducer.model.metric;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель содержащая измерения")
public class Measurement {

    @Schema(description = "наименование статистики", example = "VALUE")
    private String statistic;

    @Schema(description = "Значение измерения", example = "12924203008")
    private String value;
}

