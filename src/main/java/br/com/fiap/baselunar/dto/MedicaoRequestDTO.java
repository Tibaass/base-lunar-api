package br.com.fiap.baselunar.dto;

// Payload de entrada pra criar uma medicao.
// So precisa do id do sensor e do valor. A data eu preencho no service
// se o cliente nao mandar.
public class MedicaoRequestDTO {

    private Long sensorId;
    private Double valor;

    public MedicaoRequestDTO() {
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
