package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.dto.MedicaoRequestDTO;
import br.com.fiap.baselunar.dto.MedicaoResponseDTO;
import br.com.fiap.baselunar.model.Medicao;
import br.com.fiap.baselunar.model.Sensor;
import br.com.fiap.baselunar.model.StatusMedicao;
import br.com.fiap.baselunar.repository.MedicaoRepository;
import br.com.fiap.baselunar.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicaoService {

    // Margem (em %) pra considerar que a medicao esta "perto" do limite
    // e portanto em ALERTA. 10% me pareceu razoavel pro caso da base.
    private static final double MARGEM_ALERTA = 0.10;

    @Autowired
    private MedicaoRepository repository;

    @Autowired
    private SensorRepository sensorRepository;

    public MedicaoResponseDTO registrar(MedicaoRequestDTO req) {
        Sensor sensor = sensorRepository.findById(req.getSensorId())
                .orElseThrow(() -> new RuntimeException("Sensor nao encontrado"));

        Medicao m = new Medicao();
        m.setSensor(sensor);
        m.setValor(req.getValor());
        m.setData(LocalDateTime.now());

        // Aproveita pra atualizar o "estado atual" do sensor tambem,
        // assim o GET /api/sensores continua refletindo a ultima leitura.
        sensor.setValorAtual(req.getValor());
        sensor.setUltimaLeitura(m.getData());
        sensorRepository.save(sensor);

        Medicao salva = repository.save(m);
        return MedicaoResponseDTO.from(salva, calcularStatus(salva));
    }

    public List<MedicaoResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(m -> MedicaoResponseDTO.from(m, calcularStatus(m)))
                .toList();
    }

    public Optional<MedicaoResponseDTO> buscarPorId(Long id) {
        return repository.findById(id)
                .map(m -> MedicaoResponseDTO.from(m, calcularStatus(m)));
    }

    public List<MedicaoResponseDTO> listarPorSensor(Long sensorId) {
        return repository.findBySensorIdOrderByDataDesc(sensorId).stream()
                .map(m -> MedicaoResponseDTO.from(m, calcularStatus(m)))
                .toList();
    }

    // === Regra de negocio do status ===
    //
    // NORMAL  -> valor dentro da faixa [min, max]
    // ALERTA  -> valor dentro da faixa mas a menos de 10% da borda,
    //            OU fora da faixa porem a menos de 10% alem do limite
    // CRITICO -> valor fora da faixa em mais de 10% alem do limite
    //
    // Se o sensor nao tiver faixa configurada, devolve NORMAL (nao tem
    // como classificar). Tambem deixei publico (package) pra poder testar.
    public StatusMedicao calcularStatus(Medicao m) {
        if (m == null || m.getValor() == null || m.getSensor() == null) {
            return StatusMedicao.NORMAL;
        }
        Double min = m.getSensor().getValorMinimo();
        Double max = m.getSensor().getValorMaximo();
        if (min == null || max == null) {
            return StatusMedicao.NORMAL;
        }

        double valor = m.getValor();
        double faixa = Math.max(max - min, 1e-9); // evita divisao por zero
        double margem = faixa * MARGEM_ALERTA;

        if (valor < min - margem || valor > max + margem) {
            return StatusMedicao.CRITICO;
        }
        if (valor < min || valor > max) {
            return StatusMedicao.ALERTA;
        }
        // Dentro da faixa: ve se ta perto da borda
        if (valor - min <= margem || max - valor <= margem) {
            return StatusMedicao.ALERTA;
        }
        return StatusMedicao.NORMAL;
    }
}
