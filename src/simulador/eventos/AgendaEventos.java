package simulador.eventos;

import estruturas.lista.Lista;
import simulador.EstatisticasDia;

/**
 * Gerencia a agenda global de eventos da simulação, ordenando-os por tempo.
 * Atua como o coração cronológico do simulador, garantindo que as ações
 * ocorram na sequência correta. Esta classe é estática, pois existe uma
 * única linha do tempo para toda a simulação.
 */
public class AgendaEventos {

    /** A lista ordenada de eventos a serem processados. */
    private static Lista<Evento> eventos = new Lista<>();

    /** O tempo do último evento que foi executado, representando o "relógio" da simulação. */
    private static int tempoUltimoEvento = 0;

    /** Referência ao último evento executado, útil para depuração. */
    private static Evento ultimoEventoExecutado = null;

    /**
     * Adiciona um novo evento à agenda. O evento é inserido na posição correta
     * para manter a ordem cronológica (do menor para o maior tempo).
     *
     * @param evento O evento a ser agendado.
     * @throws IllegalArgumentException se o evento for nulo.
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }

        /** Adiciona o evento de forma ordenada usando uma expressão lambda para comparação.*/
        eventos.adicionarOrdenado(evento, (e1, e2) -> {
            if (e1.getTempo() < e2.getTempo()) return -1;
            if (e1.getTempo() > e2.getTempo()) return 1;
            return 0;
        });
    }

    /**
     * Remove um evento específico da agenda.
     * É útil para cancelar uma ação futura que não é mais necessária.
     *
     * @param evento O evento a ser removido.
     * @return {@code true} se o evento foi encontrado e removido, {@code false} caso contrário.
     */
    public static boolean removerEvento(Evento evento) {
        return eventos.removerProcurado(evento);
    }

    /**
     * Executa o loop principal da simulação, processando todos os eventos pendentes em ordem.
     * O tempo da simulação avança de acordo com o tempo de cada evento executado.
     *
     * @param estatisticas O objeto de estatísticas do dia, que pode ser modificado pelos eventos.
     */
    public static void processarEventos(EstatisticasDia estatisticas) {
        while (temEventos()) {
            Evento evento = eventos.removerHead(); // Pega o próximo evento na ordem cronológica
            tempoUltimoEvento = evento.getTempo();
            ultimoEventoExecutado = evento;
            evento.executar(estatisticas);
        }
    }

    /**
     * Limpa completamente a agenda de eventos e zera o tempo da simulação.
     * Essencial para preparar o simulador para um novo dia.
     */
    public static void resetar() {
        eventos = new Lista<>();
        tempoUltimoEvento = 0;
        ultimoEventoExecutado = null;
    }

    /**
     * Retorna o tempo de execução do último evento processado, que representa o tempo atual da simulação.
     *
     * @return O tempo do último evento em minutos.
     */
    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    /**
     * Retorna uma referência ao último evento que foi executado pela agenda.
     *
     * @return O último {@link Evento} executado.
     */
    public static Evento getUltimoEventoExecutado() {
        return ultimoEventoExecutado;
    }

    /**
     * Verifica se ainda existem eventos pendentes na agenda a serem processados.
     *
     * @return {@code true} se a lista de eventos não está vazia, {@code false} caso contrário.
     */
    public static boolean temEventos() {
        return !eventos.estaVazia();
    }
}