package br.com.fiap.baselunar.dto;

import br.com.fiap.baselunar.model.Medicao;
import br.com.fiap.baselunar.model.StatusMedicao;

import java.time.LocalDateTime;

// DTO de resposta - separa o que sai pra API do que ta no banco.
// Aqui ja vai com o status calculado, pra nao expor o Medicao cru
// (que carrega o Sensor inteiro por causa do ManyToOne).
public class MedicaoResponseDTO {

    private Long id;
    private Long sensorId;
    private String sensorNome;
    private Double valor;
    private String unidade;
    private LocalDateTime data;
    private StatusMedicao status;

    public MedicaoResponseDTO() {
    }

    public static MedicaoResponseDTO from(Medicao m, StatusMedicao status) {
        MedicaoResponseDTO dto = new MedicaoResponseDTO();
        dto.id = m.getId();
        if (m.getSensor() != null) {
            dto.sensorId = m.getSensor().getId();
            dto.sensorNome = m.getSensor().getNome();
            dto.unidade = m.getSensor().getUnidade();
        }
        dto.valor = m.getValor();
        dto.data = m.getData();
        dto.status = status;
        return dto;
    }

    public Long getId() { return id; }
    public Long getSensorId() { return sensorId; }
    public String getSensorNome() { return sensorNome; }
    public Double getValor() { return valor; }
    public String getUnidade() { return unidade; }
    public LocalDateTime getData() { return data; }
    public StatusMedicao getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setSensorId(Long sensorId) { this.sensorId = sensorId; }
    public void setSensorNome(String sensorNome) { this.sensorNome = sensorNome; }
    public void setValor(Double valor) { this.valor = valor; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public void setData(LocalDateTime data) { this.data = data; }
    public void setStatus(StatusMedicao status) { this.status = status; }
}
