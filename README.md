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
