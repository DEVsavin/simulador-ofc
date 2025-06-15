package simulador.eventos;

import simulador.SimuladorGUI; // Importa a classe da UI
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configTempo.TempoDetalhado;
import simulador.configTempo.GerenciadorTempo;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;
import simulador.EstatisticasDia;

/**
 * Evento para deslocar um caminhão pequeno de uma zona à estação de transferência.
 */
public class EventoTransferenciaParaEstacao extends Evento {
    private final CaminhaoPequeno caminhaoPequeno;
    private final Zona zonaOrigem;
    private final GerenciadorZonas gerenciadorZonas;

    public EventoTransferenciaParaEstacao(int tempoInicio, CaminhaoPequeno caminhaoPequeno, Zona zonaOrigem, GerenciadorZonas gerenciador) {
        super(tempoInicio);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaOrigem = zonaOrigem;
        this.gerenciadorZonas = gerenciador;
    }

    @Override
    public String toString() {
        return String.format("Transferência | Caminhão: %s | Origem: %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaOrigem.getNome(),
                GerenciadorTempo.formatarHorarioSimulado(getTempo()));
    }

    @Override
    public void executar(EstatisticasDia estatisticas) {
        EstacaoDeTransferencia estacaoDestino = this.gerenciadorZonas.getEstacaoPara(zonaOrigem);

        // Atualiza a UI para mostrar que o caminhão está em trânsito
        SimuladorGUI.updateTruck(caminhaoPequeno.getId(), "Indo p/ Estação " + estacaoDestino.getNomeEstacao(), estacaoDestino.getNomeEstacao());

        // --- NOVA LINHA ---
        // Pausa a simulação por 1 segundo para que a animação seja visível
        SimuladorGUI.pause();

        // O resto do método continua normalmente
        int tempoAtual = getTempo();
        int cargaAtual = caminhaoPequeno.getCargaAtual();

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

        AgendaEventos.adicionarEvento(
                new EventoEstacaoTransferencia(
                        tempoAtual + temposCalculados.tempoTotal,
                        estacaoDestino,
                        caminhaoPequeno
                )
        );
    }
}