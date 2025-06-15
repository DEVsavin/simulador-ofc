package simulador.configTempo;

import simulador.configuracao.configuracao;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilitário para manipulação de tempos no simulador de coleta de lixo.
 */
public class GerenciadorTempo {

    // Horários de pico (em minutos desde 00:00)
    private static final int PICO_MANHA_INICIO = 360; // 06:00
    private static final int PICO_MANHA_FIM = 540; // 09:00
    private static final int PICO_TARDE_INICIO = 1020; // 17:00
    private static final int PICO_TARDE_FIM = 1200; // 20:00
    private static final int HORA_INICIAL_SIMULACAO = 420; // 07:00

    /**
     * Formata minutos desde 07:00 como horário HH:mm.
     *
     * @param minutosDecorridos Minutos desde o início da simulação
     * @return String no formato "HH:mm"
     * @throws IllegalArgumentException se minutos forem negativos
     */
    public static String formatarHorarioSimulado(int minutosDecorridos) {
        if (minutosDecorridos < 0) {
            throw new IllegalArgumentException("Minutos decorridos não podem ser negativos");
        }
        int minutosTotais = HORA_INICIAL_SIMULACAO + minutosDecorridos;
        int horas = minutosTotais / 60;
        int minutos = minutosTotais % 60;
        return String.format("%02d:%02d", horas, minutos);
    }

    /**
     * Formata uma duração em minutos como texto legível.
     *
     * @param duracaoMinutos Duração em minutos
     * @return String no formato "Xh Ym" ou "Zm"
     */
    public static String formatarDuracao(int duracaoMinutos) {
        int horas = duracaoMinutos / 60;
        int minutosRestantes = duracaoMinutos % 60;
        return horas > 0
                ? String.format("%dh %dm", horas, minutosRestantes)
                : String.format("%dm", minutosRestantes);
    }

    /**
     * Estima o tempo de viagem ajustado por horários de pico.
     *
     * @param tempoSimulacao Tempo atual da simulação (em minutos)
     * @param duracaoPadrao Duração base da viagem (em minutos)
     * @return Tempo ajustado
     * @throws IllegalArgumentException se parâmetros forem negativos
     */
    public static int estimarTempoViagem(int tempoSimulacao, int duracaoPadrao) {
        if (tempoSimulacao < 0 || duracaoPadrao < 0) {
            throw new IllegalArgumentException("Parâmetros de tempo devem ser não negativos");
        }

        int minutosRestantes = duracaoPadrao;
        int tempoAcumulado = 0;
        int tempoAtual = HORA_INICIAL_SIMULACAO + tempoSimulacao;

        while (minutosRestantes > 0) {
            double fatorTráfego = isPeriodoCongestionado(tempoAtual)
                    ? configuracao.MULTIPLICADOR_TEMPO_PICO
                    : configuracao.MULTIPLICADOR_TEMPO_FORA_PICO;
            tempoAcumulado += (int) Math.ceil(fatorTráfego);
            tempoAtual++;
            minutosRestantes--;
        }

        return tempoAcumulado;
    }

    /**
     * Calcula tempos detalhados para coleta ou transferência.
     *
     * @param tempoSimulacao Tempo atual da simulação (em minutos)
     * @param cargaToneladas Carga em toneladas
     * @param isDescarregamento True para transferência, false para coleta
     * @return Objeto com tempos calculados
     * @throws IllegalArgumentException se parâmetros forem inválidos
     */
    public static TempoDetalhado calcularTempoDetalhado(int tempoSimulacao, int cargaToneladas, boolean isDescarregamento) {
        if (tempoSimulacao < 0 || cargaToneladas < 0) {
            throw new IllegalArgumentException("Parâmetros devem ser não negativos");
        }

        // Verifica horário de pico
        int minutosTotais = HORA_INICIAL_SIMULACAO + tempoSimulacao;
        boolean emPico = isPeriodoCongestionado(minutosTotais);

        // Seleciona tempos mínimo e máximo
        int tempoMinimo = emPico ? configuracao.TEMPO_MIN_PICO : configuracao.TEMPO_MIN_FORA_PICO;
        int tempoMaximo = emPico ? configuracao.TEMPO_MAX_PICO : configuracao.TEMPO_MAX_FORA_PICO;

        // Gera tempo base
        int tempoBaseViagem = ThreadLocalRandom.current().nextInt(tempoMinimo, tempoMaximo + 1);

        // Calcula tempos
        int tempoViagem = estimarTempoViagem(tempoSimulacao, tempoBaseViagem);
        int tempoOperacao = isDescarregamento ? 0 : cargaToneladas * configuracao.TEMPO_COLETA_POR_TONELADA;
        int tempoAdicionalCarga = isDescarregamento ? (int) (tempoViagem * 0.3) : 0;
        int tempoTotalOperacao = tempoOperacao + tempoViagem + tempoAdicionalCarga;

        return new TempoDetalhado(tempoOperacao, tempoViagem, tempoTotalOperacao);
    }

    /**
     * Verifica se o tempo está em período de pico.
     *
     * @param minutosTotais Minutos desde 00:00
     * @return True se for período de pico
     */
    private static boolean isPeriodoCongestionado(int minutosTotais) {
        return (minutosTotais >= PICO_MANHA_INICIO && minutosTotais <= PICO_MANHA_FIM) ||
                (minutosTotais >= PICO_TARDE_INICIO && minutosTotais <= PICO_TARDE_FIM);
    }
}