package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configTempo.GerenciadorTempo;
import simulador.zona.Zona;
import simulador.EstatisticasDia;
import simulador.zona.GerenciadorZonas;

/**
 * Representa o evento de um caminhão que tenta realizar a coleta de lixo em uma zona específica.
 * Este evento atua como um "gatilho" que aciona a lógica de coleta, a qual está encapsulada
 * na própria classe {@link CaminhaoPequeno}.
 */
public class EventoColeta extends Evento {

    /** O caminhão que realizará a coleta. */
    private CaminhaoPequeno caminhao;

    /** A zona onde a coleta será realizada. */
    private Zona zonaAtual;

    /** O gerenciador de zonas, necessário para a lógica de navegação do caminhão. */
    private GerenciadorZonas gerenciadorZonas;

    /**
     * Constrói um novo evento de coleta.
     *
     * @param tempo       O tempo de simulação (em minutos) em que a coleta deve ocorrer.
     * @param caminhao    O {@link CaminhaoPequeno} que realizará a coleta.
     * @param zona        A {@link Zona} onde a coleta será tentada.
     * @param gerenciador O {@link GerenciadorZonas} para consulta de rotas e estações.
     */
    public EventoColeta(int tempo, CaminhaoPequeno caminhao, Zona zona, GerenciadorZonas gerenciador) {
        super(tempo);
        this.caminhao = caminhao;
        this.zonaAtual = zona;
        this.gerenciadorZonas = gerenciador;
    }

    /**
     * Retorna uma representação textual do evento para fins de log e depuração.
     *
     * @return Uma string formatada com o ID do caminhão, o nome da zona e o horário do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoColeta | Caminhão %s | Zona %s | Horário: %s",
                caminhao.getId(),
                zonaAtual.getNome(),
                GerenciadorTempo.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa a ação do evento de coleta.
     * A responsabilidade da lógica é delegada ao método {@code realizarColeta} do caminhão,
     * passando todo o contexto necessário para que ele execute a operação.
     *
     * @param estatisticas O objeto de estatísticas do dia, que será atualizado pelo caminhão.
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        this.caminhao.realizarColeta(getTempo(), this.zonaAtual, this.gerenciadorZonas, estatisticas);
    }
}