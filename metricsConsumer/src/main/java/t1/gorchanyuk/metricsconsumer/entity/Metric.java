package t1.gorchanyuk.metricsconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metric")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metric_id_seq")
    @SequenceGenerator(name = "metric_id_seq", initialValue = 1, allocationSize = 100)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "base_unit")
    private String baseUnit;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL)
    private List<Measurement> measure;
}
