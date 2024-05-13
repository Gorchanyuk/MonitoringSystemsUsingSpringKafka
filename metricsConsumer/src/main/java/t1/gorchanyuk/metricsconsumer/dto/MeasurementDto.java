package t1.gorchanyuk.metricsconsumer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель содержащая измерения")
public class MeasurementDto implements Serializable {

    @Schema(description = "наименование статистики", example = "VALUE")
    private String statistic;

    @Schema(description = "Значение измерения", example = "12924203008")
    private String value;
}

