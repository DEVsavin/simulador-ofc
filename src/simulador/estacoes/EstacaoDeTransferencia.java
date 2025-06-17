package simulador.estacoes;

import simulador.SimuladorGUI; // Importa a classe da interface gráfica
import estruturas.filas.Fila;
import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.configuracao;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.eventos.EventoGerarCaminhaoGrande;
import simulador.configTempo.GerenciadorTempo;
import simulador.eventos.EventoVerificarEsperaCaminhaoGrande;
import simulador.zona.GerenciadorZonas;

/**
 * Gerencia uma estação de transferência de lixo.
 */
public class EstacaoDeTransferencia {
    private String nomeEstacao;
    private CaminhaoGrande caminhaoGrandeAtual;
    private Fila<CaminhaoPequeno> filaCaminhoes = new Fila<>();
    private GerenciadorZonas gerenciadorZonas;

    /**
     * Cria uma estação de transferência.
     * @param nomeEstacao Nome da estação
     */
    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.caminhaoGrandeAtual = new CaminhaoGrande();
    }

    public void setGerenciadorZonas(GerenciadorZonas gerenciador) {
        this.gerenciadorZonas = gerenciador;
    }

    public void gerarNovoCaminhaoGrande(int tempoAtual) {
        this.caminhaoGrandeAtual = new CaminhaoGrande();
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande criado.");
        // Para a UI, podemos representar a chegada de um novo caminhão grande
        SimuladorGUI.updateTruck("G" + caminhaoGrandeAtual.getId(), "Aguardando", "Estacao " + this.nomeEstacao);
        SimuladorGUI.pause();
        descarregarFilaEspera(tempoAtual);
    }

    public String getNomeEstacao() {
        return nomeEstacao;
    }

    public CaminhaoGrande getCaminhaoGrandeAtual() {
        return caminhaoGrandeAtual;
    }

    public Fila<CaminhaoPequeno> getFilaCaminhoes() {
        return filaCaminhoes;
    }

    public boolean temCaminhaoGrandeDisponivel() {
        return caminhaoGrandeAtual != null && !caminhaoGrandeAtual.estaCheio();
    }

    public void despacharCaminhaoGrande(int tempoAtual) {
        if (caminhaoGrandeAtual != null) {
            if (caminhaoGrandeAtual.getEventoDeVerificacao() != null) {
                AgendaEventos.removerEvento(caminhaoGrandeAtual.getEventoDeVerificacao());
                caminhaoGrandeAtual.setEventoDeVerificacao(null);
            }
            caminhaoGrandeAtual.descarregar();
            gerarNovoCaminhaoGrande(tempoAtual);
        }
    }

    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println("+--------------------------------------------------+");
        System.out.println("|          DESCARREGAMENTO NA ESTAÇÃO              |");
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| %-18s | %-28s |%n", "Horário de Chegada", GerenciadorTempo.formatarHorarioSimulado(tempoAtual));
        System.out.printf("| %-18s | %-28s |%n", "Estação", nomeEstacao);
        System.out.printf("| %-18s | %-28s |%n", "Caminhão", caminhao.getId());
        System.out.printf("| %-18s | %-28s |%n", "Status", "Chegada confirmada");

        if (caminhaoGrandeAtual == null || caminhaoGrandeAtual.estaCheio()) {
            // --- LINHA ALTERADA ---
            // Atualiza a UI para mostrar o caminhão na fila da estação
            SimuladorGUI.updateTruck(caminhao.getId(), "Na Fila", "Estacao " + this.nomeEstacao);

            filaCaminhoes.enqueue(caminhao);
            System.out.printf("| %-18s | %-28d |%n", "Tamanho da Fila", filaCaminhoes.size());

            if (caminhao.getEventoAgendado() == null) {
                int tempoLimite = tempoAtual + configuracao.TEMPO_MAX_ESPERA_ESTACAO;
                EventoGerarCaminhaoGrande evento = new EventoGerarCaminhaoGrande(tempoLimite, this);
                AgendaEventos.adicionarEvento(evento);
                caminhao.setEventoAgendado(evento);
                System.out.printf("| %-18s | %-28s |%n", "Evento Agendado", "Caminhão grande às " + GerenciadorTempo.formatarHorarioSimulado(tempoLimite));
            }
        } else {
            // --- LINHA ALTERADA ---
            // Atualiza a UI para mostrar o caminhão descarregando na estação
            SimuladorGUI.updateTruck(caminhao.getId(), "Descarregando", "Estacao " + this.nomeEstacao);

            if (caminhao.getEventoAgendado() != null) {
                AgendaEventos.removerEvento(caminhao.getEventoAgendado());
                caminhao.setEventoAgendado(null);
                System.out.printf("| %-18s | %-28s |%n", "Evento Cancelado", "Geração de caminhão grande");
            }

            int carga = caminhao.getCargaAtual();
            int tempoDescarga = carga * configuracao.TEMPO_DESCARGA_POR_TONELADA;
            boolean eraVazio = caminhaoGrandeAtual.getCargaAtual() == 0;

            caminhaoGrandeAtual.receberCarga(carga);
            caminhao.descarregar();

            System.out.printf("| %-18s | %-28s |%n", "Carga Descarregada", carga + " toneladas (Carga: " + caminhao.getCargaAtual() + "/" + caminhao.getCapacidadeMaxima() + ")");
            System.out.printf("| %-18s | %-28s |%n", "Tempo de Descarga", GerenciadorTempo.formatarDuracao(tempoDescarga));
            System.out.printf("| %-18s | %-28s |%n", "Horário de Conclusão", GerenciadorTempo.formatarHorarioSimulado(tempoAtual + tempoDescarga));

            if (eraVazio && caminhaoGrandeAtual.getCargaAtual() > 0) {
                int tempoVerificacao = tempoAtual + configuracao.TOLERANCIA_ESPERA_CAMINHAO_GRANDE;
                EventoVerificarEsperaCaminhaoGrande evento = new EventoVerificarEsperaCaminhaoGrande(tempoVerificacao, this, this.caminhaoGrandeAtual);
                this.caminhaoGrandeAtual.setEventoDeVerificacao(evento);
                AgendaEventos.adicionarEvento(evento);
                System.out.printf("| %-18s | %-28s |%n", "Tolerância Acionada", "Verificação às " + GerenciadorTempo.formatarHorarioSimulado(tempoVerificacao));
            }

            caminhao.registrarViagem();

            if (caminhao.podeRealizarNovaViagem()) {
                int proximoHorario = tempoAtual + tempoDescarga;
                caminhao.atualizarZonaAlvo();
                AgendaEventos.adicionarEvento(new EventoColeta(proximoHorario, caminhao, caminhao.getZonaAlvo(), this.gerenciadorZonas));
                System.out.printf("| %-18s | %-28s |%n", "Próxima Ação", "Volta para coleta");
            } else {
                System.out.printf("| %-18s | %-28s |%n", "Status do Caminhão", "Finalizou atividades do dia");
            }

            if (caminhaoGrandeAtual.estaCheio()) {
                System.out.printf("| %-18s | %-28s |%n", "Caminhão Grande", "Cheio");
                System.out.printf("| %-18s | %-28s |%n", "Ação", "Caminhão Grande " + caminhaoGrandeAtual.getId() + " partiu para aterro");
                despacharCaminhaoGrande(tempoAtual + tempoDescarga);
            }
        }
        System.out.println("+--------------------------------------------------+");
        System.out.println();
    }

    private void descarregarFilaEspera(int tempoAtual) {
        while (!filaCaminhoes.isEmpty() && !caminhaoGrandeAtual.estaCheio()) {
            CaminhaoPequeno caminhaoFila = filaCaminhoes.poll();
            if (caminhaoFila.getEventoAgendado() != null) {
                AgendaEventos.removerEvento(caminhaoFila.getEventoAgendado());
                caminhaoFila.setEventoAgendado(null);
            }
            int carga = caminhaoFila.getCargaAtual();
            caminhaoGrandeAtual.receberCarga(carga);
            System.out.println("[ESTAÇÃO " + nomeEstacao + "] Caminhão pequeno " + caminhaoFila.getId() + " da fila descarregou " + carga + " toneladas.");
        }
    }
}