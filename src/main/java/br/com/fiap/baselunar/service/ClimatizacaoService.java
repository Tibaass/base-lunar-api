package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.model.Climatizacao;
import br.com.fiap.baselunar.repository.ClimatizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClimatizacaoService {

    @Autowired
    private ClimatizacaoRepository repository;

    public List<Climatizacao> listarTodos() {
        return repository.findAll();
    }

    public Optional<Climatizacao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Climatizacao salvar(Climatizacao climatizacao) {
        return repository.save(climatizacao);
    }

    public Climatizacao atualizar(Long id, Climatizacao novosDados) {
        Climatizacao c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Climatizacao nao encontrada"));

        c.setSetor(novosDados.getSetor());
        c.setTemperatura(novosDados.getTemperatura());
        c.setUmidade(novosDados.getUmidade());
        c.setPressao(novosDados.getPressao());
        c.setStatus(novosDados.getStatus());

        return repository.save(c);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
