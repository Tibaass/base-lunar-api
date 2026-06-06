package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.model.ConsumoEnergia;
import br.com.fiap.baselunar.repository.ConsumoEnergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumoEnergiaService {

    @Autowired
    private ConsumoEnergiaRepository repository;

    public List<ConsumoEnergia> listarTodos() {
        return repository.findAll();
    }

    public Optional<ConsumoEnergia> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public ConsumoEnergia salvar(ConsumoEnergia consumo) {
        return repository.save(consumo);
    }

    public ConsumoEnergia atualizar(Long id, ConsumoEnergia novosDados) {
        ConsumoEnergia c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de consumo nao encontrado"));

        c.setSetor(novosDados.getSetor());
        c.setConsumoKwh(novosDados.getConsumoKwh());
        c.setFonteEnergia(novosDados.getFonteEnergia());
        c.setDataHora(novosDados.getDataHora());

        return repository.save(c);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
