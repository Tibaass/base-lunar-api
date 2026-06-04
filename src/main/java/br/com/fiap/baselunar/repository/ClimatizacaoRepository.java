package br.com.fiap.baselunar.repository;

import br.com.fiap.baselunar.model.Climatizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimatizacaoRepository extends JpaRepository<Climatizacao, Long> {
}
