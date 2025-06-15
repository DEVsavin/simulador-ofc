package simulador.configTempo;

/**
 * Estrutura para armazenar tempos detalhados de uma operação (coleta, deslocamento e extra).
 */
public class TempoDetalhado {

    /** Tempo de coleta (em minutos) */
    public final int tempoColeta;

    /** Tempo de deslocamento (em minutos) */
    public final int tempoDeslocamento;

    /** Tempo extra por carga máxima (em minutos) */
    public final int tempoExtraCarregado;

    /** Tempo total da operação (em minutos) */
    public final int tempoTotal;

    /**
     * Cria uma estrutura com tempos detalhados.
     *
     * @param tempoColeta Tempo de coleta (em minutos)
     * @param tempoDeslocamento Tempo de deslocamento (em minutos)
     * @param tempoExtraCarregado Tempo extra por carga (em minutos)
     */
    public TempoDetalhado(int tempoColeta, int tempoDeslocamento, int tempoExtraCarregado) {
        this.tempoColeta = tempoColeta;
        this.tempoDeslocamento = tempoDeslocamento;
        this.tempoExtraCarregado = tempoExtraCarregado;
        this.tempoTotal = tempoColeta + tempoDeslocamento + tempoExtraCarregado;
    }
}