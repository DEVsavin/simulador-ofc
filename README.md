# 🗑️ Simulador de Coleta de Lixo para Teresina

Projeto acadêmico desenvolvido para a disciplina de **Estruturas de Dados**, com foco em **Gestão de Resíduos Sólidos** e **Sustentabilidade Ambiental**. A simulação é aplicada ao contexto urbano da cidade de **Teresina (PI)**.

---

## 🎯 Visão Geral

Este projeto implementa uma **simulação orientada a eventos discretos** que modela o processo de coleta de lixo na cidade de Teresina. A cidade é dividida em zonas, cada uma com uma taxa de geração de lixo, e conta com uma frota de caminhões de diferentes capacidades que transportam os resíduos até estações de transferência, de onde seguem para o aterro sanitário.

> ⚙️ **Destaque técnico**: foram utilizadas **estruturas de dados personalizadas** (listas e filas), evitando o uso das estruturas do Java Collections Framework (como `ArrayList` ou `LinkedList`).

---

## ✨ Conceitos Principais

- **Simulação Orientada a Eventos:**  
  Utiliza uma agenda de eventos para controlar cronologicamente as ações do sistema (coleta, chegada, despacho, etc).

- **Zonas e Geração de Lixo:**  
  A cidade é dividida em 5 zonas: Norte, Sul, Leste, Sudeste e Centro. Cada zona gera lixo aleatoriamente dentro de uma faixa definida.

- **Frota de Caminhões:**
  - **Pequenos:** 2t, 4t, 8t e 10t – coletam nas zonas.
  - **Grandes:** 20t – atuam nas estações de transferência.

- **Estações de Transferência:**  
  Pontos onde caminhões pequenos descarregam. Se o caminhão grande estiver cheio, forma-se uma fila de espera.

---

## 🛠️ Tecnologias Utilizadas

- **Java (JDK 24)**
- **JavaFX** – interface gráfica e visualização da simulação
- **IntelliJ IDEA** – ambiente de desenvolvimento
- **Estruturas de Dados Customizadas** – lista e fila duplamente encadeadas

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos

- **JDK 17+**
- **JavaFX SDK** (compatível com sua JDK e SO)
- **IntelliJ IDEA** (Community ou Ultimate)

### 📦 Clonar o Repositório

```bash
git clone https://github.com/devsavin/simulador-ofc.git
cd simulador-ofc
🧩 Abrir no IntelliJ
Abra o IntelliJ IDEA.

Vá em File > Open... e selecione a pasta do projeto clonado.

⚙️ Configurar JavaFX
Vá em File > Project Structure > Libraries.

Clique em + > Java e selecione a pasta lib do seu JavaFX SDK.

Clique em OK.

🖥️ Configurar a Execução
Vá em Run > Edit Configurations.

Clique em + > Application.

Configure:

Main class: simulador.Main

VM options:

bash
Copiar
Editar
--module-path /caminho/para/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics
🔁 Substitua /caminho/para/... pelo caminho real no seu sistema.

Clique em OK e depois em ▶️ Run para iniciar a aplicação.

📁 Estrutura do Projeto
bash
Copiar
Editar
src/
├── simulador/
│   ├── Main.java
│   ├── Simulador.java
│   └── SimuladorGUI.java
├── simulador/caminhoes/
│   ├── CaminhaoPequeno.java
│   └── CaminhaoGrande.java
├── simulador/estacoes/
│   └── EstacaoDeTransferencia.java
├── simulador/eventos/
│   ├── Evento.java
│   ├── EventoColeta.java
│   └── AgendaEventos.java
├── simulador/zona/
│   ├── Zona.java
│   ├── Zonas.java
│   └── GerenciadorZonas.java
├── simulador/configuracao/
│   └── configuracao.java
├── simulador/configTempo/
│   └── GerenciadorTempo.java
└── estruturas/
    ├── Lista.java
    ├── Fila.java
    └── No.java
👨‍💻 Autores
Sávio Macêdo
Bruno Moraes
