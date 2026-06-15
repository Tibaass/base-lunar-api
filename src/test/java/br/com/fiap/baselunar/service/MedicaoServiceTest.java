package br.com.fiap.baselunar.service;

import br.com.fiap.baselunar.model.Medicao;
import br.com.fiap.baselunar.model.Sensor;
import br.com.fiap.baselunar.model.StatusMedicao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes da regra de negocio do status.
// Esses testes nao precisam subir o Spring inteiro - so olham pra logica
// do calcularStatus(), que e a parte que importa pra Sprint 2.
class MedicaoServiceTest {

    private MedicaoService service;
    private Sensor sensor;

    @BeforeEach
    void setUp() {
        service = new MedicaoService();

        // Sensor de temperatura tipico do modulo habitacional:
        // a faixa "saudavel" pros tripulantes e mais ou menos 18-26 graus.
        // Com isso a margem de alerta de 10% da uma janela de 0.8 grau.
        sensor = new Sensor();
        sensor.setNome("Sensor de temperatura - Modulo A");
        sensor.setTipo("temperatura");
        sensor.setUnidade("C");
        sensor.setValorMinimo(18.0);
        sensor.setValorMaximo(26.0);
    }

    private Medicao medicaoCom(double valor) {
        Medicao m = new Medicao();
        m.setSensor(sensor);
        m.setValor(valor);
        return m;
    }

    @Test
    void valorNoMeioDaFaixaDeveSerNormal() {
        // 22 graus = bem no meio, longe das bordas
        assertEquals(StatusMedicao.NORMAL, service.calcularStatus(medicaoCom(22.0)));
    }

    @Test
    void valorDentroDaFaixaMasPertoDaBordaDeveSerAlerta() {
        // 18.5 ta dentro [18, 26] mas a 0.5 do limite inferior (< 0.8 margem)
        assertEquals(StatusMedicao.ALERTA, service.calcularStatus(medicaoCom(18.5)));
    }

    @Test
    void valorPoucoAcimaDoLimiteDeveSerAlerta() {
        // 26.5 ta fora mas a apenas 0.5 acima do max (dentro da margem)
        assertEquals(StatusMedicao.ALERTA, service.calcularStatus(medicaoCom(26.5)));
    }

    @Test
    void valorMuitoAbaixoDoLimiteDeveSerCritico() {
        // 5 graus = beeem abaixo do minimo
        assertEquals(StatusMedicao.CRITICO, service.calcularStatus(medicaoCom(5.0)));
    }

    @Test
    void valorMuitoAcimaDoLimiteDeveSerCritico() {
        // 35 graus, longe demais do maximo
        assertEquals(StatusMedicao.CRITICO, service.calcularStatus(medicaoCom(35.0)));
    }

    @Test
    void sensorSemFaixaConfiguradaCaiNoNormal() {
        // Sem min/max nao tem como classificar, entao devolve NORMAL
        Sensor semFaixa = new Sensor();
        Medicao m = new Medicao();
        m.setSensor(semFaixa);
        m.setValor(999.0);
        assertEquals(StatusMedicao.NORMAL, service.calcularStatus(m));
    }
}
