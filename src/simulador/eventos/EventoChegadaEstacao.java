package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configTempo.GerenciadorTempo;
import simulador.EstatisticasDia;

/**
 * Representa o momento exato em que um caminhão pequeno chega a uma estação de transferência.
 * A principal responsabilidade deste evento é notificar a estação sobre a chegada
 * do caminhão, para que ela possa iniciar o processo de descarregamento ou enfileiramento.
 */
public class EventoChegadaEstacao extends Evento {

    /** A estação de destino do caminhão. */
    private EstacaoDeTransferencia estacao;

    /** O caminhão que está chegando na estação. */
    private CaminhaoPequeno caminhao;

    /**
     * Cria um evento de chegada de um caminhão a uma estação de transferência.
     *
     * @param tempo    O tempo de simulação (em minutos) em que o caminhão chega.
     * @param estacao  A estação de transferência de destino.
     * @param caminhao O caminhão pequeno que está chegando.
     */
    public EventoChegadaEstacao(int tempo, EstacaoDeTransferencia estacao, CaminhaoPequeno caminhao) {
        super(tempo);
        this.estacao = estacao;
        this.caminhao = caminhao;
    }

    /**
     * Fornece uma representação textual do evento, útil para logs e depuração.
     *
     * @return Uma string formatada com os detalhes do evento.
     */
    @Override
    public String toString() {

        return String.format("EventoChegadaEstacao | Caminhão %s | Estação %s | Horário: %s",
                caminhao.getId(),
                estacao.getNomeEstacao(),
                GerenciadorTempo.formatarHorarioSimulado(tempo));
    }

    /**
     * Executa a ação de chegada do caminhão.
     * A lógica de processamento é delegada à estação de transferência, que irá
     * receber o caminhão para descarregar ou colocar na fila de espera.
     *
     *
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        estacao.receberCaminhaoPequeno(caminhao, tempo);
    }
}