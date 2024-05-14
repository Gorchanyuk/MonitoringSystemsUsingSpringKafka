package t1.gorchanyuk.metricsconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurement_id_seq")
    @SequenceGenerator(name = "measurement_id_seq", initialValue = 1, allocationSize = 150)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "statistic")
    private String statistic;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "metric")
    private Metric metric;
}
