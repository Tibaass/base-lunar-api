package br.com.fiap.baselunar.repository;

import br.com.fiap.baselunar.model.ConsumoEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoEnergiaRepository extends JpaRepository<ConsumoEnergia, Long> {
}
