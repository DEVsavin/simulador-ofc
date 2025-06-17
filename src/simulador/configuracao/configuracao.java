package simulador.configuracao;

/**
 * Centraliza todas as constantes e parâmetros numéricos da simulação.
 * <p>
 * Esta classe utiliza apenas atributos estáticos e finais (constantes) para
 * garantir que os valores sejam consistentes e fáceis de modificar em um
 * único local. O construtor é privado para impedir a instanciação.
 */
public class configuracao {

    /* ==================== TEMPOS DE TRABALHO ==================== */

    /**
     * Minutos necessários para descarregar 1 tonelada de lixo.
     */
    public static final int TEMPO_DESCARGA_POR_TONELADA = 3;

    /**
     * Minutos necessários para coletar 1 tonelada de lixo.
     */
    public static final int TEMPO_COLETA_POR_TONELADA = 10;

    /* ==================== TEMPO DE VIAGENS DOS CAMINHÕES ==================== */

    /**
     * Tempo mínimo de viagem em horários de pico (em minutos).
     */
    public static final int TEMPO_MIN_PICO = 20;

    /**
     * Tempo máximo de viagem em horários de pico (em minutos).
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


    /* ==================== PARÂMETROS DE CRIAÇÃO DE CAMINHÕES ==================== */

    /** Quantidade de caminhões de 2 toneladas. */
    public static final int QTD_CAMINHOES_2T = 8;
    /** Número de viagens por dia para caminhões de 2 toneladas. */
    public static final int VIAGENS_CAMINHOES_2T = 5;
    /** Capacidade dos caminhões de 2 toneladas. */
    public static final int CAPACIDADE_CAMINHOES_2T = 2;

    /** Quantidade de caminhões de 4 toneladas. */
    public static final int QTD_CAMINHOES_4T = 5;
    /** Número de viagens por dia para caminhões de 4 toneladas. */
    public static final int VIAGENS_CAMINHOES_4T = 3;
    /** Capacidade dos caminhões de 4 toneladas. */
    public static final int CAPACIDADE_CAMINHOES_4T = 4;

    /** Quantidade de caminhões de 8 toneladas. */
    public static final int QTD_CAMINHOES_8T = 4;
    /** Número de viagens por dia para caminhões de 8 toneladas. */
    public static final int VIAGENS_CAMINHOES_8T = 5;
    /** Capacidade dos caminhões de 8 toneladas. */
    public static final int CAPACIDADE_CAMINHOES_8T = 8;

    /** Quantidade de caminhões de 10 toneladas. */
    public static final int QTD_CAMINHOES_10T = 3;
    /** Número de viagens por dia para caminhões de 10 toneladas. */
    public static final int VIAGENS_CAMINHOES_10T = 3;
    /** Capacidade dos caminhões de 10 toneladas. */
    public static final int CAPACIDADE_CAMINHOES_10T = 10;

    /* ==================== PARÂMETROS DE SIMULAÇÃO ==================== */
    /**
     * Tempo máximo de espera (em minutos) permitido para um caminhão pequeno na fila da estação.
     */
    public static final int TEMPO_MAX_ESPERA_ESTACAO = 50;
    /**
     * Tolerância de espera do caminhão grande (em minutos) para partir com carga parcial. Ex: 120min = 2 horas.
     */
    public static final int TOLERANCIA_ESPERA_CAMINHAO_GRANDE = 120;

    /** Menor quantidade de lixo gerada por dia na zona Sul (em toneladas). */
    public static final int LIXO_MIN_SUL = 20;
    /** Maior quantidade de lixo gerada por dia na zona Sul (em toneladas). */
    public static final int LIXO_MAX_SUL = 40;

    /** Menor quantidade de lixo gerada por dia na zona Norte (em toneladas). */
    public static final int LIXO_MIN_NORTE = 15;
    /** Maior quantidade de lixo gerada por dia na zona Norte (em toneladas). */
    public static final int LIXO_MAX_NORTE = 30;

    /** Menor quantidade de lixo gerada por dia na zona Centro (em toneladas). */
    public static final int LIXO_MIN_CENTRO = 10;
    /** Maior quantidade de lixo gerada por dia na zona Centro (em toneladas). */
    public static final int LIXO_MAX_CENTRO = 20;

    /** Menor quantidade de lixo gerada por dia na zona Leste (em toneladas). */
    public static final int LIXO_MIN_LESTE = 15;
    /** Maior quantidade de lixo gerada por dia na zona Leste (em toneladas). */
    public static final int LIXO_MAX_LESTE = 25;

    /** Menor quantidade de lixo gerada por dia na zona Sudeste (em toneladas). */
    public static final int LIXO_MIN_SUDESTE = 18;
    /** Maior quantidade de lixo gerada por dia na zona Sudeste (em toneladas). */
    public static final int LIXO_MAX_SUDESTE = 35;

    /**
     * Fator de multiplicação aplicado ao tempo de viagem em horários de pico.
     */
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.3;

    /**
     * Fator de multiplicação aplicado ao tempo de viagem em horários normais (fora de pico).
     */
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    /**
     * Construtor privado para impedir que esta classe de utilitários seja instanciada.
     */
    private configuracao() {
    }
}