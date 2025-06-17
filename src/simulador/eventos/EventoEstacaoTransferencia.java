package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.configTempo.GerenciadorTempo;
import simulador.EstatisticasDia;

/**
 * Evento para enviar um caminhão pequeno à estação de transferência.
 */
public class EventoEstacaoTransferencia extends Evento {
    private EstacaoDeTransferencia estacao;
    private CaminhaoPequeno caminhao;

    /**
     * Cria um evento de transferência para a estação.
     *
     * @param tempo Tempo de execução (em minutos)
     * @param estacao Estação de transferência de destino
     * @param caminhao Caminhão pequeno a ser enviado
     */
    public EventoEstacaoTransferencia(int tempo, EstacaoDeTransferencia estacao, CaminhaoPequeno caminhao) {
        super(tempo);
        this.estacao = estacao;
        this.caminhao = caminhao;
    }

    /**
     * Retorna uma representação textual do evento.
     *
     * @return String com dados do caminhão, estação e horário
     */
    @Override
    public String toString() {
        return String.format("EventoEstacaoTransferencia | Caminhão %s | Estação %s | Horário: %s",
                caminhao.getId(),
                estacao.getNomeEstacao(),
                GerenciadorTempo.formatarHorarioSimulado(tempo));
    }

    /**
     * Executa o evento, enviando o caminhão à estação para descarregamento.
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        estacao.receberCaminhaoPequeno(caminhao, tempo);
    }
}