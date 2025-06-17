package simulador;

/**
 * Armazena e gerencia as estatísticas acumuladas para um único dia de simulação.
 * Esta classe rastreia o total de lixo coletado, o número de viagens e o tempo
 * gasto nas operações de coleta.
 */
public class EstatisticasDia {

    /** Total de lixo coletado no dia, em toneladas. */
    private int totalLixoColetado = 0;

    /** Número total de viagens de coleta realizadas no dia. */
    private int totalViagensColeta = 0;

    /** Tempo total gasto em todas as coletas do dia, em minutos. */
    private int tempoTotalColeta = 0;

    /**
     * Registra os dados de uma única operação de coleta, atualizando as estatísticas diárias.
     *
     * @param quantidade A quantidade de lixo (em toneladas) coletada nesta viagem.
     * @param tempoGasto O tempo (em minutos) que a operação de coleta levou.
     */
    public void registrarColeta(int quantidade, int tempoGasto) {
        this.totalLixoColetado += quantidade;
        this.totalViagensColeta++;
        this.tempoTotalColeta += tempoGasto;
    }

    /**
     * Zera todos os contadores de estatísticas.
     * Este método deve ser chamado no final de cada dia para preparar
     * a instância para a simulação do dia seguinte.
     */
    public void resetar() {
        totalLixoColetado = 0;
        totalViagensColeta = 0;
        tempoTotalColeta = 0;
    }
}