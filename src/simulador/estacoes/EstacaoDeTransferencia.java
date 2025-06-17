package simulador.estacoes;

import simulador.SimuladorGUI;
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
 * Modela uma estação de transbordo de lixo, um ponto central onde caminhões
 * pequenos descarregam seu lixo para ser consolidado em um caminhão grande.
 * Esta classe gerencia a fila de espera de caminhões pequenos e o ciclo de vida
 * (carregamento, despacho) dos caminhões grandes.
 */
public class EstacaoDeTransferencia {
    private String nomeEstacao;
    private CaminhaoGrande caminhaoGrandeAtual;
    private Fila<CaminhaoPequeno> filaCaminhoes = new Fila<>();
    private GerenciadorZonas gerenciadorZonas;

    /**
     * Cria uma nova estação de transferência com um nome e um caminhão grande inicial.
     * @param nomeEstacao O nome da estação (ex: "A", "B").
     */
    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.caminhaoGrandeAtual = new CaminhaoGrande();
    }

    /**
     * Define a referência ao {@link GerenciadorZonas}.
     * Essencial para que a estação possa agendar os próximos eventos dos caminhões pequenos,
     * como o retorno para uma nova coleta.
     * @param gerenciador A instância do gerenciador de zonas da simulação.
     */
    public void setGerenciadorZonas(GerenciadorZonas gerenciador) {
        this.gerenciadorZonas = gerenciador;
    }

    /**
     * Substitui o caminhão grande atual por um novo.
     * Após a chegada do novo caminhão, tenta imediatamente descarregar os caminhões
     * pequenos que porventura estejam na fila de espera.
     * @param tempoAtual O tempo da simulação em que o novo caminhão é gerado.
     */
    public void gerarNovoCaminhaoGrande(int tempoAtual) {
        this.caminhaoGrandeAtual = new CaminhaoGrande();
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande criado.");
        SimuladorGUI.updateTruck("G" + caminhaoGrandeAtual.getId(), "Aguardando", "Estacao " + this.nomeEstacao);
        SimuladorGUI.pause();
        descarregarFilaEspera(tempoAtual);
    }

    /**
     * Retorna o nome da estação.
     * @return O nome da estação.
     */
    public String getNomeEstacao() {
        return nomeEstacao;
    }

    /**
     * Retorna o caminhão grande que está atualmente na estação.
     * @return A instância do {@link CaminhaoGrande} atual.
     */
    public CaminhaoGrande getCaminhaoGrandeAtual() {
        return caminhaoGrandeAtual;
    }

    /**
     * Retorna a fila de caminhões pequenos que estão aguardando para descarregar.
     * @return A instância da {@link Fila} de caminhões.
     */
    public Fila<CaminhaoPequeno> getFilaCaminhoes() {
        return filaCaminhoes;
    }

    /**
     * Verifica se o caminhão grande atual pode receber mais lixo.
     * @return {@code true} se o caminhão não estiver cheio, {@code false} caso contrário.
     */
    public boolean temCaminhaoGrandeDisponivel() {
        return caminhaoGrandeAtual != null && !caminhaoGrandeAtual.estaCheio();
    }

    /**
     * Libera o caminhão grande atual para ir ao aterro sanitário.
     * Este método cancela qualquer evento de verificação de espera pendente para o caminhão
     * que partiu e aciona a geração de um novo caminhão para substituí-lo.
     * @param tempoAtual O tempo da simulação em que o despacho ocorre.
     */
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

    /**
     * Ponto de entrada para um {@link CaminhaoPequeno} que chega à estação.
     * O método avalia se o caminhão grande atual pode receber a carga. Se sim,
     * o descarregamento é imediato. Se não (porque o caminhão grande está cheio ou ausente),
     * o caminhão pequeno é colocado em uma fila de espera.
     * @param caminhao O caminhão pequeno que chegou.
     * @param tempoAtual O tempo da simulação em que a chegada ocorre.
     */
    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println("+--------------------------------------------------+");
        System.out.println("|          DESCARREGAMENTO NA ESTAÇÃO              |");
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| %-18s | %-28s |%n", "Horário de Chegada", GerenciadorTempo.formatarHorarioSimulado(tempoAtual));
        System.out.printf("| %-18s | %-28s |%n", "Estação", nomeEstacao);
        System.out.printf("| %-18s | %-28s |%n", "Caminhão", caminhao.getId());
        System.out.printf("| %-18s | %-28s |%n", "Status", "Chegada confirmada");

        if (caminhaoGrandeAtual == null || caminhaoGrandeAtual.estaCheio()) {
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

    /**
     * Processa a fila de caminhões pequenos que estão em espera.
     * Os caminhões são retirados da fila e descarregam sua carga no caminhão grande
     * até que a fila de espera esvazie ou o caminhão grande atinja sua capacidade máxima.
     * @param tempoAtual O tempo atual da simulação, para referência de log.
     */
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