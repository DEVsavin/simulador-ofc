package simulador.eventos;

import simulador.SimuladorGUI;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configTempo.TempoDetalhado;
import simulador.configTempo.GerenciadorTempo;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;
import simulador.EstatisticasDia;

/**
 * Representa o início da viagem de um caminhão pequeno desde uma zona de coleta
 * até a sua estação de transferência correspondente.
 * A principal função deste evento é calcular o tempo de trajeto e agendar o
 * evento de chegada do caminhão ({@link EventoChegadaEstacao}).
 */
public class EventoIniciarTransferencia extends Evento {
    /** O caminhão que está se deslocando. */
    private final CaminhaoPequeno caminhaoPequeno;
    /** A zona de onde o caminhão está partindo. */
    private final Zona zonaOrigem;
    /** O gerenciador de zonas, usado para encontrar a estação de destino. */
    private final GerenciadorZonas gerenciadorZonas;

    /**
     * Constrói um novo evento para iniciar a transferência de um caminhão.
     *
     * @param tempoInicio     O tempo de simulação (em minutos) em que a viagem se inicia.
     * @param caminhaoPequeno O {@link CaminhaoPequeno} que está se deslocando.
     * @param zonaOrigem      A {@link Zona} de onde o caminhão está partindo.
     * @param gerenciador     O {@link GerenciadorZonas} para determinar a estação de destino.
     */
    public EventoIniciarTransferencia(int tempoInicio, CaminhaoPequeno caminhaoPequeno, Zona zonaOrigem, GerenciadorZonas gerenciador) {
        super(tempoInicio);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaOrigem = zonaOrigem;
        this.gerenciadorZonas = gerenciador;
    }

    /**
     * Retorna uma representação textual do evento de transferência para logs.
     *
     * @return Uma string formatada com os detalhes da transferência.
     */
    @Override
    public String toString() {
        return String.format("Transferência | Caminhão: %s | Origem: %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaOrigem.getNome(),
                GerenciadorTempo.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o início da transferência.
     * O método determina a estação de destino, calcula todos os tempos de viagem
     * (considerando trânsito e carga), atualiza a interface gráfica, e o mais importante,
     * agenda um {@link EventoChegadaEstacao} para o horário de chegada calculado.
     *
     * @param estatisticas O objeto de estatísticas do dia (não utilizado diretamente aqui).
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        EstacaoDeTransferencia estacaoDestino = this.gerenciadorZonas.getEstacaoPara(zonaOrigem);

        /** Atualiza a UI para mostrar que o caminhão está em trânsito**/
        SimuladorGUI.updateTruck(caminhaoPequeno.getId(), "Indo p/ Estação " + estacaoDestino.getNomeEstacao(), estacaoDestino.getNomeEstacao());

        /** Pausa a simulação para que a animação seja visível**/
        SimuladorGUI.pause();

        int tempoAtual = getTempo();
        int cargaAtual = caminhaoPequeno.getCargaAtual();

        /** Calcula os tempos de viagem **/
        TempoDetalhado temposCalculados = GerenciadorTempo.calcularTempoDetalhado(tempoAtual, cargaAtual, true);


        System.out.println("+--------------------------------------------------+");
        System.out.println("|          TRANSFERÊNCIA PARA ESTAÇÃO              |");
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| %-18s | %-28s |%n", "Horário Inicial", GerenciadorTempo.formatarHorarioSimulado(tempoAtual));
        System.out.printf("| %-18s | %-28s |%n", "Caminhão", caminhaoPequeno.getId());
        System.out.printf("| %-18s | %-28s |%n", "Estação Destino", estacaoDestino.getNomeEstacao());
        System.out.printf("| %-18s | %-28s |%n", "Tempo de Trajeto", GerenciadorTempo.formatarDuracao(temposCalculados.tempoDeslocamento));
        if (temposCalculados.tempoExtraCarregado > 0) {
            System.out.printf("| %-18s | %-28s |%n", "Tempo Extra Carga", GerenciadorTempo.formatarDuracao(temposCalculados.tempoExtraCarregado));
        }
        System.out.printf("| %-18s | %-28s |%n", "Tempo Total", GerenciadorTempo.formatarDuracao(temposCalculados.tempoTotal));
        System.out.printf("| %-18s | %-28s |%n", "Horário de Chegada", GerenciadorTempo.formatarHorarioSimulado(tempoAtual + temposCalculados.tempoTotal));
        System.out.println("+--------------------------------------------------+");
        System.out.println();

        // Agenda o evento de chegada na estação
        AgendaEventos.adicionarEvento(
                new EventoChegadaEstacao(
                        tempoAtual + temposCalculados.tempoTotal,
                        estacaoDestino,
                        caminhaoPequeno
                )
        );
    }
}