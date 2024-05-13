package t1.gorchanyuk.metricsproducer.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Модель содержащая сообщение ошибки")
public class ErrorResponseDto {

    @Schema(description = "Сообщение ошибки", example = "Внутренняя ошибка сервера")
    private String message;

    @Schema(description = "Время возникновения ошибки")
    private long timestamp;
}