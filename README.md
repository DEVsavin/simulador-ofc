# 🗑️ simulador.Simulador de Coleta de Lixo para Teresina

Projeto acadêmico desenvolvido para a disciplina de **Estruturas de Dados**, com foco em **Gestão de Resíduos Sólidos** e **Sustentabilidade Ambiental**.

Este simulador modela o processo de coleta de lixo na cidade de **Teresina**, considerando zonas urbanas, caminhões com diferentes capacidades, estações de transferência e transporte ao aterro sanitário.

---

## 📌 Objetivos

- Modelar o processo de coleta de lixo em Teresina com base em zonas urbanas.
- Simular a dinâmica de caminhões pequenos e grandes, filas e eventos.
- Aplicar estruturas de dados próprias (listas, filas, etc.) implementadas em Java.
- Coletar estatísticas como tempo médio de espera e quantidade total de lixo coletado.
- Responder: **Quantos caminhões de 20 toneladas são necessários para atender à demanda?**

---

## 🧩 Requisitos Funcionais

- Divisão da cidade em 5 zonas: Sul, Norte, Centro, Leste e Sudeste.
- Caminhões pequenos com capacidades de **2, 4, 8 e 10 toneladas** realizam coletas por zona.
- Tempos de viagem variam conforme horário de pico e fora de pico.
- Caminhões pequenos descarregam em **2 estações de transferência**.
- Caminhões grandes de **20 toneladas** transportam o lixo ao aterro.
- Se o tempo de espera nas estações for excedido, um novo caminhão grande é adicionado.

---

## ⚙️ Parâmetros de Configuração

- Capacidades dos caminhões pequenos.
- Capacidade dos caminhões grandes.
- Intervalos de geração de lixo por zona.
- Tempo de viagem dos caminhões (pico e fora de pico).
- Número máximo de viagens por caminhão pequeno.
- Tempo máximo de espera nas estações.
- Tolerância de espera dos caminhões grandes.

---

## 🧱 Estrutura de Dados

O projeto **não utiliza estruturas de dados prontas do Java** como `ArrayList` ou `Queue`. Foram implementadas **estruturas próprias** para:

- Listas
- Filas
- Pilhas (se necessário)
- Gerenciador de eventos

---

## 📊 Saída Esperada

- Logs detalhados em tempo real.
- Estatísticas da simulação:
  - Tempo médio de espera
  - Total de lixo coletado
  - Caminhões acionados
- Visualização de filas e dinâmicas operacionais

---

## 📚 Conclusão

Este projeto promove a integração entre **teoria e prática** em Estruturas de Dados, ao mesmo tempo em que oferece um olhar aplicado sobre desafios reais de sustentabilidade urbana. O simulador permite avaliar estratégias para a coleta de lixo e dimensionamento de recursos de forma eficiente.

---

> Projeto desenvolvido como trabalho final da disciplina **Estruturas de Dados**, Sistemas de Informação, 2025.
