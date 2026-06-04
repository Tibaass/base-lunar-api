package br.com.fiap.baselunar.repository;

import br.com.fiap.baselunar.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}
