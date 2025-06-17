package simulador.eventos;

import simulador.EstatisticasDia;
import simulador.caminhoes.CaminhaoGrande;
import simulador.estacoes.EstacaoDeTransferencia;

/**
 * Representa um evento de verificação agendado para o caminhão grande.
 * Este evento é acionado após um tempo de tolerância para decidir se um caminhão
 * grande, que já tem alguma carga mas não está cheio, deve partir para o aterro
 * para não ficar ocioso por muito tempo.
 */
public class EventoVerificarEsperaCaminhaoGrande extends Evento {

    /** A estação onde a verificação ocorrerá. */
    private EstacaoDeTransferencia estacao;

    /** O caminhão grande específico que está sob verificação. */
    private CaminhaoGrande caminhaoGrandeAlvo;

    /**
     * Cria um novo evento para verificar a tolerância de espera de um caminhão grande.
     *
     * @param tempo   O tempo de simulação (em minutos) em que a verificação deve ocorrer.
     * @param estacao A {@link EstacaoDeTransferencia} onde o caminhão está aguardando.
     * @param caminhao O {@link CaminhaoGrande} que é o alvo da verificação.
     */
    public EventoVerificarEsperaCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao, CaminhaoGrande caminhao) {
        super(tempo);
        this.estacao = estacao;
        this.caminhaoGrandeAlvo = caminhao;
    }

    /**
     * Executa a lógica de verificação da tolerância de espera.
     * Se o caminhão grande alvo ainda estiver na estação, com carga parcial,
     * ele é despachado para o aterro, e um novo caminhão grande é providenciado.
     *
     * @param estatisticas O objeto de estatísticas do dia (não utilizado diretamente neste evento).
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        CaminhaoGrande caminhaoAtualNaEstacao = estacao.getCaminhaoGrandeAtual();

        // Condição para despachar:
        // 1. O caminhão que acionou o evento ainda é o que está na estação.
        // 2. Ele já recebeu alguma carga.
        // 3. Ele ainda não está completamente cheio.
        if (caminhaoAtualNaEstacao == caminhaoGrandeAlvo && caminhaoAtualNaEstacao.getCargaAtual() > 0 && !caminhaoAtualNaEstacao.estaCheio()) {
            System.out.println();
            System.out.println("+--------------------------------------------------+");
            System.out.println("|           TOLERÂNCIA DE ESPERA ATINGIDA          |");
            System.out.println("+--------------------------------------------------+");
            System.out.printf("| %-18s | %-28s |%n", "Estação", estacao.getNomeEstacao());
            System.out.printf("| %-18s | %-28s |%n", "Caminhão Grande", caminhaoAtualNaEstacao.getId());
            System.out.printf("| %-18s | %-28s |%n", "Ação", "Partindo com carga parcial");
            System.out.printf("| %-18s | %-28s |%n", "Carga", caminhaoAtualNaEstacao.getCargaAtual() + " toneladas");
            System.out.println("+--------------------------------------------------+");
            System.out.println();

            // Manda o caminhão para o aterro e inicia o processo de colocar um novo no lugar.
            estacao.despacharCaminhaoGrande(getTempo());
        }
    }
}