package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.model.Reservatorio;
import br.com.fiap.baselunar.repository.ReservatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservatorioService {

    @Autowired
    private ReservatorioRepository repository;

    public List<Reservatorio> listarTodos() {
        return repository.findAll();
    }

    public Optional<Reservatorio> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Reservatorio salvar(Reservatorio reservatorio) {
        return repository.save(reservatorio);
    }

    public Reservatorio atualizar(Long id, Reservatorio novosDados) {
        Reservatorio r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservatorio nao encontrado"));

        r.setNome(novosDados.getNome());
        r.setTipoRecurso(novosDados.getTipoRecurso());
        r.setCapacidadeMaxima(novosDados.getCapacidadeMaxima());
        r.setNivelAtual(novosDados.getNivelAtual());
        r.setUnidade(novosDados.getUnidade());

        return repository.save(r);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
