package t1.gorchanyuk.metricsconsumer.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1.gorchanyuk.metricsconsumer.entity.Metric;

import java.util.Optional;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

    @EntityGraph(attributePaths = {"measure"})
    Optional<Metric> findById(long id);
}