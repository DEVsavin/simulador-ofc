package simulador.configuracao;

/**
 * Guarda os números importantes para o simulador de coleta de lixo funcionar.
 * <p>
 * Esta classe tem valores fixos, como tempos, quantidades de lixo e horários, usados em todo o sistema.
 */
public class configuracao {

    // ==================== TEMPOS DE TRABALHO ====================

    /**
     * Minutos para descarregar 1 tonelada de lixo.
     */
    public static final int TEMPO_DESCARGA_POR_TONELADA = 3;

    /**
     * Minutos para coletar 1 tonelada de lixo.
     */
    public static final int TEMPO_COLETA_POR_TONELADA = 10;

    // = TEMPO DE VIAGENS DOS CAMINHÕES

    /**
     * Tempo mínimo de viagem em horários de muito trânsito (em minutos).
     */
    public static final int TEMPO_MIN_PICO = 20;

    /**
     * Tempo máximo de viagem em horários de muito trânsito (em minutos).
     */
    public static final int TEMPO_MAX_PICO = 60;

    /**
     * Tempo mínimo de viagem em horários tranquilos (em minutos).
     */
    public static final int TEMPO_MIN_FORA_PICO = 20;

    /**
     * Tempo máximo de viagem em horários tranquilos (em minutos).
     */
    public static final int TEMPO_MAX_FORA_PICO = 35;

    /**
     * Quantidade máxima de viagens por dia para caminhões pequenos.
     */


    // ==================== PARÂMETROS DE CRIAÇÃO DE CAMINHÕES ====================
    public static final int QTD_CAMINHOES_2T = 8;
    public static final int VIAGENS_CAMINHOES_2T = 5;
    public static final int CAPACIDADE_CAMINHOES_2T = 2;

    public static final int QTD_CAMINHOES_4T = 5;
    public static final int VIAGENS_CAMINHOES_4T = 3;
    public static final int CAPACIDADE_CAMINHOES_4T = 4;

    public static final int QTD_CAMINHOES_8T = 4;
    public static final int VIAGENS_CAMINHOES_8T = 5;
    public static final int CAPACIDADE_CAMINHOES_8T = 8;

    public static final int QTD_CAMINHOES_10T = 3;
    public static final int VIAGENS_CAMINHOES_10T = 3;
    public static final int CAPACIDADE_CAMINHOES_10T = 10;

    // ==================== PARÂMETROS DE SIMULAÇÃO ====================
    /**
     * Tempo máximo de espera permitido nas estações de transferência para os caminhões pequenos.
     */
    public static final int TEMPO_MAX_ESPERA_ESTACAO = 50; // em minutos

    /**
     * Tempo de deslocamento padrão quando um caminhão se move para outra zona sem coletar.
     */
    public static final int TEMPO_VIAGEM_ENTRE_ZONAS = 30; // em minutos


    /**
     * Tolerância de espera do caminhão grande para partir com carga parcial (em minutos).
     */
    public static final int TOLERANCIA_ESPERA_CAMINHAO_GRANDE = 120; // Ex: 2 horas

    /**
     * Menor quantidade de lixo gerada por dia na zona Sul (em toneladas).
     */
    public static final int LIXO_MIN_SUL = 20;

    /**
     * Maior quantidade de lixo gerada por dia na zona Sul (em toneladas).
     */
    public static final int LIXO_MAX_SUL = 40;

    /**
     * Menor quantidade de lixo gerada por dia na zona Norte (em toneladas).
     */
    public static final int LIXO_MIN_NORTE = 15;

    /**
     * Maior quantidade de lixo gerada por dia na zona Norte (em toneladas).
     */
    public static final int LIXO_MAX_NORTE = 30;

    /**
     * Menor quantidade de lixo gerada por dia na zona Centro (em toneladas).
     */
    public static final int LIXO_MIN_CENTRO = 10;

    /**
     * Maior quantidade de lixo gerada por dia na zona Centro (em toneladas).
     */
    public static final int LIXO_MAX_CENTRO = 20;

    /**
     * Menor quantidade de lixo gerada por dia na zona Leste (em toneladas).
     */
    public static final int LIXO_MIN_LESTE = 15;

    /**
     * Maior quantidade de lixo gerada por dia na zona Leste (em toneladas).
     */
    public static final int LIXO_MAX_LESTE = 25;

    /**
     * Menor quantidade de lixo gerada por dia na zona Sudeste (em toneladas).
     */
    public static final int LIXO_MIN_SUDESTE = 18;

    /**
     * Maior quantidade de lixo gerada por dia na zona Sudeste (em toneladas).
     */
    public static final int LIXO_MAX_SUDESTE = 35;



    /**
     * Aumento do tempo de viagem em horários de muito trânsito.
     */
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.3;

    /**
     * Tempo normal de viagem em horários tranquilos.
     */
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    // ==================== HORÁRIOS DE TRÂNSITO ====================

    /**
     * Hora de início do trânsito intenso pela manhã.
     */
    public static final int HORA_INICIO_PICO_MANHA = 6;

    /**
     * Hora de fim do trânsito intenso pela manhã.
     */
    public static final int HORA_FIM_PICO_MANHA = 9;

    /**
     * Hora de início do trânsito intenso à tarde.
     */
    public static final int HORA_INICIO_PICO_TARDE = 17;

    /**
     * Hora de fim do trânsito intenso à tarde.
     */
    public static final int HORA_FIM_PICO_TARDE = 19;

    /**
     * Verifica se uma hora está em período de muito trânsito.
     *
     * @param hora Hora do dia (0 a 23)
     * @return Verdadeiro se for horário de pico, falso se não for
     */
    public static boolean isHorarioDePico(int hora) {
        return (hora >= HORA_INICIO_PICO_MANHA && hora < HORA_FIM_PICO_MANHA)
                || (hora >= HORA_INICIO_PICO_TARDE && hora < HORA_FIM_PICO_TARDE);
    }

    /**
     * Não permite criar objetos desta classe, pois ela só guarda valores fixos.
     */
    private configuracao() {
        // Impede a criação de objetos
    }
}