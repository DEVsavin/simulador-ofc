package simulador.eventos;

import simulador.EstatisticasDia;
import simulador.caminhoes.CaminhaoGrande;
import simulador.estacoes.EstacaoDeTransferencia;

/**
 * Evento que verifica se a tolerância de espera de um caminhão grande foi atingida.
 */
public class EventoVerificarEsperaCaminhaoGrande extends Evento {
    private EstacaoDeTransferencia estacao;
    private CaminhaoGrande caminhaoGrandeAlvo;

    public EventoVerificarEsperaCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao, CaminhaoGrande caminhao) {
        super(tempo);
        this.estacao = estacao;
        this.caminhaoGrandeAlvo = caminhao;
    }

    @Override
    public void executar(EstatisticasDia estatisticas) {
        // Pega o caminhão grande que está atualmente na estação
        CaminhaoGrande caminhaoAtualNaEstacao = estacao.getCaminhaoGrandeAtual();

        // Verifica se o caminhão grande alvo ainda é o mesmo que está na estação
        // e se ele tem alguma carga, mas não está cheio.
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

            // Manda o caminhão para o aterro e processa a fila
            estacao.despacharCaminhaoGrande(getTempo());
        }
    }
}