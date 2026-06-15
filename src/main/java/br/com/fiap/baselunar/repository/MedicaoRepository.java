package br.com.fiap.baselunar.repository;

import br.com.fiap.baselunar.model.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    List<Medicao> findBySensorIdOrderByDataDesc(Long sensorId);
}
