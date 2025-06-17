package simulador.eventos;
import simulador.EstatisticasDia;
/**
 * Classe abstrata para eventos genéricos no simulador, com tempo de execução e lógica específica.
 */
public abstract class Evento implements Comparable<Evento> {

    /**
     * Tempo (em minutos) em que o evento está agendado.
     */
    protected int tempo;

    /**
     * Cria um evento com o tempo especificado.
     *
     * @param tempo Tempo de execução (em minutos)
     * @throws IllegalArgumentException se o tempo for negativo
     */
    public Evento(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("Tempo não pode ser negativo");
        }
        this.tempo = tempo;
    }

    /**
     * Retorna o tempo de execução do evento.
     *
     * @return Tempo em minutos
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Executa a lógica do evento.
     */
    public abstract void executar(EstatisticasDia estatisticas);

    /**
     * Compara eventos por tempo de execução.
     *
     * @param outro Evento a ser comparado
     * @return Negativo se este evento for antes, zero se simultâneo, positivo se depois
     * @throws NullPointerException se outro for nulo
     */
    @Override
    public int compareTo(Evento outro) {
        if (outro == null) {
            throw new NullPointerException("Evento para comparação não pode ser null");
        }
        return Integer.compare(this.tempo, outro.tempo);
    }
}