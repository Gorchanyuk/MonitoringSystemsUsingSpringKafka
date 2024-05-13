package t1.gorchanyuk.metricsconsumer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Модель для передачи сообщения об ошибке")
public class ErrorResponseDto {

    @Schema(description = "Сообщение с информацией об ошибке", example = "Внутренняя ошибка сервера")
    private String message;

    @Schema(description = "Время возникновения ошибки")
    private long timestamp;
}
