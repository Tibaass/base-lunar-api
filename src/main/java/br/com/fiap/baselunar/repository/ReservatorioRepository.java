package br.com.fiap.baselunar.repository;

import br.com.fiap.baselunar.model.Reservatorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservatorioRepository extends JpaRepository<Reservatorio, Long> {
}
