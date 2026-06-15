# Monitoramento de Recursos da Base Lunar - API Backend

Projeto da disciplina **Técnicas Avançadas de Programação** (FIAP - 2EMR - Engenharia Mecatrônica).

API REST em **Java + Spring Boot** que controla os recursos de uma base lunar (sensores, reservatórios, consumo de energia, climatização e alertas operacionais). É a parte de backend que se integra com o app mobile da disciplina de *Advanced Programming And Mobile Dev*.

## Integrantes

| Nome | RM |
|------|----|
| Luiz Henrique de Almeida | 564390 |
| Tiago Ferreira | 562129 |


## Tecnologias

- Java 17
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Banco H2 em modo **file** (persistente em disco)
- Maven

## Arquitetura

Aplicação dividida em camadas, conforme pedido na prova:

```
src/main/java/br/com/fiap/baselunar
├── BaseLunarApplication.java   (main do Spring Boot)
├── model         -> entidades JPA
├── repository    -> interfaces JpaRepository
├── service       -> regras de negócio
└── controller    -> endpoints REST
```

## Como rodar

Pré-requisitos: JDK 17 e Maven instalados.

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

O banco H2 é salvo em `./data/baselunar.mv.db` (modo file), então os dados não se perdem ao reiniciar.

Console do H2 disponível em: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/baselunar`
- User: `sa`
- Password: (vazio)

## Endpoints

Todos os recursos têm CRUD completo (GET lista, GET por id, POST, PUT, DELETE).

| Recurso | URL base |
|---------|----------|
| Sensores | `/api/sensores` |
| Reservatórios | `/api/reservatorios` |
| Consumo de energia | `/api/consumo-energia` |
| Climatização | `/api/climatizacao` |
| Alertas | `/api/alertas` |
| Medições (Sprint 2) | `/medicoes` |

## Sprint 2 - Medições e cálculo de status

A partir da Sprint 2 a API passou a guardar o histórico de leituras dos sensores na entidade `Medicao` e a classificar cada leitura em três estados.

### Como funciona o cálculo de status

Cada sensor tem uma **faixa esperada** definida pelos campos `valorMinimo` e `valorMaximo`. Quando uma medição é registrada, o `MedicaoService` compara o valor com essa faixa e devolve um dos estados do enum `StatusMedicao`:

- `NORMAL` -> valor confortavelmente dentro da faixa.
- `ALERTA` -> está dentro da faixa mas a menos de 10% da borda, **ou** já saiu da faixa porém ainda a menos de 10% além do limite. Ou seja, está chegando perto do crítico.
- `CRITICO` -> ultrapassou o limite em mais de 10% da faixa, pra cima ou pra baixo.

Exemplo prático com um sensor de temperatura de faixa 18-26 °C (margem de 10% = 0,8 °C):

| Valor lido | Status |
|------------|--------|
| 22,0 °C | NORMAL |
| 18,5 °C | ALERTA (perto do mínimo) |
| 26,3 °C | ALERTA (pouco acima do máximo) |
| 5,0 °C ou 35,0 °C | CRITICO |

Se um sensor não tiver faixa configurada, a medição é classificada como `NORMAL` (não dá pra avaliar).

### Endpoints de medições

```http
POST   /medicoes              # registra uma medicao
GET    /medicoes              # lista todas
GET    /medicoes/{id}         # busca uma especifica
GET    /medicoes/sensor/{id}  # historico de um sensor (mais recente primeiro)
```

**Registrar uma medição**
```http
POST http://localhost:8080/medicoes
Content-Type: application/json

{
  "sensorId": 1,
  "valor": 22.0
}
```

Resposta:
```json
{
  "id": 1,
  "sensorId": 1,
  "sensorNome": "Sensor temp Modulo A",
  "valor": 22.0,
  "unidade": "C",
  "data": "2026-06-15T20:37:12.978",
  "status": "NORMAL"
}
```

Observação: a resposta é um `MedicaoResponseDTO`, separado da entidade. Isso evita expor o `Sensor` inteiro e mantém o cálculo do status fora do banco.

### Como testar (rodando local)

1. Subir a API (`mvn spring-boot:run`).
2. Criar um sensor já com `valorMinimo` e `valorMaximo`:
   ```http
   POST /api/sensores
   { "nome":"Sensor temp Modulo A", "tipo":"temperatura",
     "unidade":"C", "valorMinimo":18.0, "valorMaximo":26.0 }
   ```
3. Registrar medições com diferentes valores em `POST /medicoes` e conferir o campo `status` da resposta.
4. `GET /medicoes/sensor/{id}` mostra o histórico.

### Testes automatizados

Há testes JUnit em `src/test/java/.../service/MedicaoServiceTest.java` cobrindo as três faixas de status (normal, alerta e crítico, com valor abaixo e acima) e o caso do sensor sem faixa configurada. Rodar com:

```bash
mvn test
```

### Exemplos de uso (Postman / Insomnia)

**Cadastrar um sensor**
```http
POST http://localhost:8080/api/sensores
Content-Type: application/json

{
  "nome": "Sensor de temperatura - Modulo A",
  "tipo": "temperatura",
  "localizacao": "Modulo Habitacional A",
  "valorAtual": 22.5,
  "unidade": "C",
  "ultimaLeitura": "2026-06-09T14:30:00"
}
```

**Cadastrar um reservatório**
```http
POST http://localhost:8080/api/reservatorios
Content-Type: application/json

{
  "nome": "Tanque de oxigenio principal",
  "tipoRecurso": "oxigenio",
  "capacidadeMaxima": 5000.0,
  "nivelAtual": 4200.0,
  "unidade": "litros"
}
```

**Cadastrar consumo de energia**
```http
POST http://localhost:8080/api/consumo-energia
Content-Type: application/json

{
  "setor": "Laboratorio de pesquisa",
  "consumoKwh": 12.4,
  "fonteEnergia": "solar",
  "dataHora": "2026-06-09T15:00:00"
}
```

**Cadastrar climatização**
```http
POST http://localhost:8080/api/climatizacao
Content-Type: application/json

{
  "setor": "Modulo Habitacional A",
  "temperatura": 22.0,
  "umidade": 45.0,
  "pressao": 101.3,
  "status": "ligado"
}
```

**Cadastrar um alerta**
```http
POST http://localhost:8080/api/alertas
Content-Type: application/json

{
  "titulo": "Nivel de oxigenio abaixo do ideal",
  "descricao": "O tanque principal esta abaixo de 85% da capacidade",
  "nivel": "media"
}
```

**Buscar todos**
```http
GET http://localhost:8080/api/sensores
```

**Buscar por id**
```http
GET http://localhost:8080/api/sensores/1
```

**Atualizar**
```http
PUT http://localhost:8080/api/sensores/1
Content-Type: application/json

{ ... mesmo formato do POST ... }
```

**Deletar**
```http
DELETE http://localhost:8080/api/sensores/1
```

## Integração com o app mobile

O app mobile (da disciplina de Advanced Programming And Mobile Dev) consome esses endpoints REST. Basta apontar a base URL do app para o endereço onde essa API estiver rodando (em desenvolvimento, normalmente `http://10.0.2.2:8080` no emulador Android ou o IP da máquina na rede local).
