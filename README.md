# Sistema de Avaliação – Tech Challenge Fase 4

## 📌 Visão Geral

Este projeto foi desenvolvido como parte do **Tech Challenge – Fase 4**, com o objetivo de consolidar conhecimentos em **Cloud Computing**, **arquitetura serverless** e **deploy de aplicações em nuvem**.

A aplicação consiste em uma **plataforma de feedback**, onde estudantes podem enviar avaliações sobre aulas, administradores recebem notificações automáticas para avaliações críticas e relatórios semanais são gerados para análise da satisfação dos alunos.

Toda a solução foi implementada utilizando serviços gerenciados da **AWS**, priorizando escalabilidade, baixo custo operacional e governança.

---

## 🎯 Objetivos do Projeto

- Implementar uma aplicação **100% serverless**
- Executar a solução em **ambiente cloud**
- Automatizar:
    - Recebimento de feedbacks
    - Notificações de avaliações críticas
    - Geração de relatórios semanais
- Aplicar **Infraestrutura como Código (IaC)**
- Demonstrar monitoramento e segurança básicos

---

## 🏗️ Arquitetura da Solução

A solução foi projetada com base no princípio de **Responsabilidade Única**, utilizando múltiplas funções serverless.

### Componentes utilizados

- **AWS Lambda (Java 17)** – Processamento das regras de negócio
- **Amazon API Gateway** – Exposição do endpoint HTTP
- **Amazon DynamoDB** – Armazenamento dos feedbacks
- **Amazon SES** – Envio de notificações por e-mail
- **Amazon EventBridge** – Agendamento do relatório semanal
- **Amazon CloudWatch** – Logs e monitoramento
- **AWS SAM** – Deploy e infraestrutura como código

### Fluxo principal

1. O cliente envia um feedback via `POST /avaliacao`
2. A Lambda de ingestão:
    - Valida os dados
    - Calcula o nível de urgência
    - Persiste no DynamoDB
3. Caso o feedback seja crítico:
    - Uma Lambda de notificação é acionada
    - Um e-mail é enviado via SES
4. Semanalmente:
    - Uma Lambda de relatório é executada automaticamente
    - Métricas são consolidadas e registradas nos logs

---

## 🔗 Endpoint da Aplicação

### Envio de Avaliação

```http
POST /avaliacao
```

### Exemplo de payload

```json
{
  "descricao": "A aula foi boa, mas poderia ter mais exemplos",
  "nota": 3
}
```
### 3. Testando o Endpoint Principal

### Regra de urgência

- Nota ≤ 3 → **CRÍTICA**
- Nota > 3 → **NORMAL**

---

## 📊 Relatório Semanal

O relatório semanal é gerado automaticamente por meio de um agendamento via **Amazon EventBridge**.

### Métricas consolidadas

- Total de avaliações
- Média das notas
- Quantidade de avaliações por nível de urgência
- Quantidade de avaliações por dia

Os resultados são registrados nos **logs do CloudWatch**, servindo como base para análise administrativa.

---

## 🔔 Notificações por E-mail

Avaliações classificadas como **críticas** disparam automaticamente uma notificação por e-mail utilizando o **Amazon SES**.

### Observações sobre o SES

- Foram utilizados endereços de e-mail verificados
- O ambiente opera no modo **sandbox**, adequado para fins acadêmicos
- Em ambiente produtivo, a conta poderia ser promovida para produção mediante solicitação

---

## 🔐 Segurança e Governança

- Cada função Lambda possui **permissões mínimas necessárias** (IAM)
- Não há credenciais sensíveis no código-fonte
- Variáveis de ambiente são utilizadas para configurações dinâmicas
- O endpoint foi exposto sem autenticação para simplificação acadêmica

---

## 🚀 Deploy e Infraestrutura como Código

Toda a infraestrutura é provisionada automaticamente utilizando **AWS SAM**, garantindo reprodutibilidade e versionamento.

### Comandos principais

```bash
mvn clean package
sam build
sam deploy
```
* Nota: Feedbacks com nota <= 3 são automaticamente classificados como CRÍTICA e disparam um e-mail de alerta imediato contendo descrição, urgência e data de envio.

---

## 📈 Monitoramento

- Logs automáticos via **Amazon CloudWatch**
- Monitoramento de execuções das Lambdas
- Registro detalhado das execuções do relatório semanal

---

## 📁 Estrutura do Projeto

```text
src/main/java/org/postech/challange
 ├── handler
 │   ├── IngestaoHandler.java
 │   ├── NotificacaoHandler.java
 │   └── RelatorioHandler.java
 └── model
template.yaml
pom.xml
README.md
```

---

## 🧪 Demonstração

A solução é apresentada por meio de um **vídeo demonstrativo**, exibindo:

- Código-fonte
- Deploy via AWS SAM
- Execução do endpoint
- Persistência no DynamoDB
- Logs no CloudWatch
- Funcionamento das notificações e do relatório

---

## ✅ Conclusão

O projeto atende integralmente aos requisitos propostos no Tech Challenge, demonstrando a aplicação prática de conceitos de **Cloud Computing**, **Serverless**, **Infraestrutura como Código**, **monitoramento** e **boas práticas de arquitetura**.

---

**Autores**

* José Franklin Miranda Gomes Leite RA 361614
* Vitor Henrique dos Santos  RA 361617 
