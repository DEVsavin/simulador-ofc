package simulador.eventos;

import estruturas.lista.Lista;
import simulador.EstatisticasDia;

/**
 * Gerencia a agenda global de eventos da simulação, ordenando-os por tempo.
 */
public class AgendaEventos {
    private static Lista<Evento> eventos = new Lista<>();
    private static int tempoUltimoEvento = 0;
    private static Evento ultimoEventoExecutado = null;

    /**
     * Adiciona um evento à agenda, ordenado por tempo.
     *
     * @param evento Evento a ser agendado
     * @throws IllegalArgumentException se o evento for nulo
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }

        eventos.adicionarOrdenado(evento, (e1, e2) -> {
            if (e1.getTempo() < e2.getTempo()) return -1;
            if (e1.getTempo() > e2.getTempo()) return 1;
            return 0;
        });
    }

    /**
     * Remove um evento específico da agenda.
     *
     * @param evento Evento a ser removido
     * @return true se removido, false caso contrário
     */
    public static boolean removerEvento(Evento evento) {
        return eventos.removerProcurado(evento);
    }

    /**
     * Processa todos os eventos em ordem cronológica, atualizando o tempo da simulação.
     */
    public static void processarEventos(EstatisticasDia estatisticas) {
        while (temEventos()) {
            Evento evento = eventos.removerHead();
            tempoUltimoEvento = evento.getTempo();
            ultimoEventoExecutado = evento;
            evento.executar(estatisticas);
        }
    }

    /**
     * Reseta a agenda, limpando eventos e estado.
     */
    public static void resetar() {
        eventos = new Lista<>();
        tempoUltimoEvento = 0;
        ultimoEventoExecutado = null;
    }

    /**
     * Retorna o tempo do último evento processado.
     *
     * @return Tempo em minutos
     */
    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    /**
     * Retorna o último evento executado.
     *
     * @return Último evento
     */
    public static Evento getUltimoEventoExecutado() {
        return ultimoEventoExecutado;
    }

    /**
     * Verifica se há eventos pendentes.
     *
     * @return true se há eventos, false caso contrário
     */
    public static boolean temEventos() {
        return !eventos.estaVazia();
    }
}