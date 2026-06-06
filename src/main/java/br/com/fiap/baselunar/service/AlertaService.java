package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.model.Alerta;
import br.com.fiap.baselunar.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository repository;

    public List<Alerta> listarTodos() {
        return repository.findAll();
    }

    public Optional<Alerta> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Alerta salvar(Alerta alerta) {
        // Se nao vier data, usa a atual. Facilita a vida de quem ta enviando do mobile.
        if (alerta.getDataHora() == null) {
            alerta.setDataHora(LocalDateTime.now());
        }
        if (alerta.getStatus() == null) {
            alerta.setStatus("aberto");
        }
        return repository.save(alerta);
    }

    public Alerta atualizar(Long id, Alerta novosDados) {
        Alerta a = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta nao encontrado"));

        a.setTitulo(novosDados.getTitulo());
        a.setDescricao(novosDados.getDescricao());
        a.setNivel(novosDados.getNivel());
        a.setStatus(novosDados.getStatus());
        a.setDataHora(novosDados.getDataHora());

        return repository.save(a);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
