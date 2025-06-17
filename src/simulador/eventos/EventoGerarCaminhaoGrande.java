package simulador.eventos;

import simulador.estacoes.EstacaoDeTransferencia;
import simulador.EstatisticasDia;

/**
 * Evento para gerar um novo caminhão grande em uma estação de transferência.
 */
public class EventoGerarCaminhaoGrande extends Evento {
    private EstacaoDeTransferencia estacao;

    /**
     * Cria um evento de geração de caminhão grande.
     *
     * @param tempo Tempo de execução (em minutos)
     * @param estacao Estação de transferência alvo
     */
    public EventoGerarCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        this.estacao = estacao;
    }

    /**
     * Gera um novo caminhão grande se a estação não tiver um disponível.
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        // Só gera se não houver caminhão grande disponível
        if (estacao.temCaminhaoGrandeDisponivel()) return;

        System.out.println("[GERAÇÃO] Tempo máximo de espera atingido. Criando caminhão grande.");
        estacao.gerarNovoCaminhaoGrande(tempo);
    }
}