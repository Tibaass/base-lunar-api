package br.com.fiap.baselunar.model;

// Status calculado em cima do valor da medicao em relacao a faixa do sensor.
// - NORMAL: dentro da faixa
// - ALERTA: chegando perto do limite (margem de 10%)
// - CRITICO: fora da faixa
public enum StatusMedicao {
    NORMAL,
    ALERTA,
    CRITICO
}
