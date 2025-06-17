package simulador.eventos;

import simulador.EstatisticasDia;

/**
 * Classe abstrata que serve como base para todos os eventos da simulação.
 * Define a estrutura fundamental de um evento, que inclui um tempo para execução
 * e uma ação específica. A implementação da interface {@code Comparable} é
 * essencial para que a {@link AgendaEventos} possa ordenar os eventos cronologicamente.
 */
public abstract class Evento implements Comparable<Evento> {

    /**
     * O tempo (em minutos desde o início do dia) em que o evento está agendado para ocorrer.
     */
    protected int tempo;

    /**
     * Cria um evento com um tempo de execução especificado.
     *
     * @param tempo O tempo de execução do evento (em minutos).
     * @throws IllegalArgumentException se o tempo fornecido for negativo.
     */
    public Evento(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("Tempo não pode ser negativo");
        }
        this.tempo = tempo;
    }

    /**
     * Retorna o tempo em que o evento está agendado para ser executado.
     *
     * @return O tempo de execução em minutos.
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Método abstrato que define a ação a ser realizada quando o evento ocorrer.
     * Cada subclasse (ex: {@link EventoColeta}) deve implementar sua própria lógica.
     *
     * @param estatisticas O objeto de estatísticas do dia, que pode ser modificado pelo evento.
     */
    public abstract void executar(EstatisticasDia estatisticas);

    /**
     * Compara este evento com outro com base no tempo de execução.
     * Este método permite que a {@link AgendaEventos} ordene a lista de eventos.
     *
     * @param outro O {@link Evento} a ser comparado.
     * @return Um valor negativo se este evento ocorrer antes do outro,
     * zero se ocorrerem ao mesmo tempo, ou um valor positivo se ocorrer depois.
     * @throws NullPointerException se o outro evento for nulo.
     */
    @Override
    public int compareTo(Evento outro) {
        if (outro == null) {
            throw new NullPointerException("Evento para comparação não pode ser null");
        }
        return Integer.compare(this.tempo, outro.tempo);
    }
}