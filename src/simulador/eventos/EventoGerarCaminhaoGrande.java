package simulador.eventos;

import simulador.estacoes.EstacaoDeTransferencia;
import simulador.EstatisticasDia;

/**
 * Representa o evento que cria um novo caminhão grande em uma estação de transferência.
 * Este evento é agendado quando um caminhão pequeno chega a uma estação que está
 * sem capacidade de recebê-lo e o tempo máximo de espera é atingido.
 */
public class EventoGerarCaminhaoGrande extends Evento {

    /** A estação de transferência onde o novo caminhão grande será gerado. */
    private EstacaoDeTransferencia estacao;

    /**
     * Cria um evento para a geração de um novo caminhão grande.
     *
     * @param tempo   O tempo de simulação (em minutos) em que o caminhão deve ser criado.
     * @param estacao A estação de transferência alvo.
     */
    public EventoGerarCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        this.estacao = estacao;
    }

    /**
     * Executa a ação de gerar um novo caminhão grande.
     * A ação só ocorre se a estação realmente não tiver um caminhão grande disponível
     * no momento da execução, evitando a criação desnecessária.
     *
     * @param estatisticas O objeto de estatísticas do dia (não utilizado diretamente neste evento).
     */
    @Override
    public void executar(EstatisticasDia estatisticas) {
        // Só gera um novo caminhão se a estação ainda precisar de um.
        if (estacao.temCaminhaoGrandeDisponivel()) return;

        System.out.println("[GERAÇÃO] Tempo máximo de espera atingido. Criando caminhão grande.");
        estacao.gerarNovoCaminhaoGrande(tempo);
    }
}