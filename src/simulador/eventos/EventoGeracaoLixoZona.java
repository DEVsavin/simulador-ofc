package simulador.eventos;

import simulador.zona.Zona;
import simulador.EstatisticasDia;

/**
 * Evento para simular a geração diária de lixo em uma zona.
 */
public class EventoGeracaoLixoZona extends Evento {
    private Zona zona;

    /**
     * Cria um evento de geração de lixo.
     *
     * @param tempo Tempo de execução (em minutos)
     * @param zona Zona onde o lixo será gerado
     */
    public EventoGeracaoLixoZona(int tempo, Zona zona) {
        super(tempo);
        this.zona = zona;
    }

    /**
     * Executa a geração de lixo na zona, conforme seus limites.
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        zona.gerarLixoDiario();
        System.out.println("[Geração] Zona " + zona.getNome() + " com lixo: " + zona.getLixoAcumulado() + "t");
    }
}