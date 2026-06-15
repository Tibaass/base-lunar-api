package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.model.Sensor;
import br.com.fiap.baselunar.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository repository;

    public List<Sensor> listarTodos() {
        return repository.findAll();
    }

    public Optional<Sensor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Sensor salvar(Sensor sensor) {
        return repository.save(sensor);
    }

    public Sensor atualizar(Long id, Sensor novosDados) {
        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor nao encontrado"));

        sensor.setNome(novosDados.getNome());
        sensor.setTipo(novosDados.getTipo());
        sensor.setLocalizacao(novosDados.getLocalizacao());
        sensor.setValorAtual(novosDados.getValorAtual());
        sensor.setUnidade(novosDados.getUnidade());
        sensor.setValorMinimo(novosDados.getValorMinimo());
        sensor.setValorMaximo(novosDados.getValorMaximo());
        sensor.setUltimaLeitura(novosDados.getUltimaLeitura());

        return repository.save(sensor);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
