package t1.gorchanyuk.metricsproducer.model.metric;

import io.micrometer.core.instrument.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import t1.gorchanyuk.metricsproducer.model.Metric;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricFromMeter implements Metric {

    private String name;                    //Наименование

    private String description;             //Описание

    private String baseUnit;                //Единица измерения, или тип передаваемых данных (необязательное поле)

    private LocalDateTime timestamp;        //Время создания метрики

    private Iterable<Measurement> measure;  //Измерения метрики (Тип из micrometer)
}
