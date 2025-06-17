package simulador.eventos;

import simulador.caminhoes.CaminhaoPequeno;
import simulador.configTempo.GerenciadorTempo;
import simulador.zona.Zona;
import simulador.EstatisticasDia;
import simulador.zona.GerenciadorZonas;

/**
 * Evento para coleta de lixo. Agora atua como um simples gatilho
 * que aciona a lógica de coleta no objeto CaminhaoPequeno.
 */
public class EventoColeta extends Evento {
    private CaminhaoPequeno caminhao;
    private Zona zonaAtual;
    private GerenciadorZonas gerenciadorZonas;

    public EventoColeta(int tempo, CaminhaoPequeno caminhao, Zona zona, GerenciadorZonas gerenciador) {
        super(tempo);
        this.caminhao = caminhao;
        this.zonaAtual = zona;
        this.gerenciadorZonas = gerenciador;
    }

    @Override
    public String toString() {
        return String.format("EventoColeta | Caminhão %s | Zona %s | Horário: %s",
                caminhao.getId(),
                zonaAtual.getNome(),
                GerenciadorTempo.formatarHorarioSimulado(getTempo()));
    }

    /**
     * METODO SIMPLIFICADO: A única responsabilidade do evento é
     * "dizer" ao caminhão para executar sua lógica de coleta.
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        this.caminhao.realizarColeta(getTempo(), this.zonaAtual, this.gerenciadorZonas, estatisticas);
    }
}