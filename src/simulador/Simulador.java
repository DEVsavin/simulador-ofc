package simulador;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoGrande;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.configuracao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.PlanejadorDeRotas;
import simulador.configTempo.GerenciadorTempo;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;
import simulador.zona.Zonas;

/**
 * Classe principal que orquestra a simulação de coleta de lixo.
 * Responsável por inicializar o ambiente da simulação (zonas, estações de transferência),
 * controlar o ciclo diário de eventos (geração de lixo, distribuição de caminhões, coleta, etc.)
 * e apresentar os resultados estatísticos ao final de cada dia simulado.
 */
public class Simulador {

    /**
     * Inicia a simulação de coleta de lixo por um número específico de dias.
     * <p>
     * Para cada dia, a simulação realiza:
     * <ul>
     *   Geração de lixo nas zonas
     *   Distribuição dos caminhões de coleta
     *   Processamento dos eventos agendados
     *   Apresentação do resumo estatístico diário
     *
     *
     * @param dias O número total de dias a serem simulados.
     */
    public void iniciarSimulacao(int dias) {
        Lista<Zona> zonas = inicializarZonas();
        EstatisticasDia estatisticas = new EstatisticasDia();

        // Criação das estações de transferência
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("B");

        // Atualiza interface gráfica com os caminhões grandes nas estações
        SimuladorGUI.atualizarCaminhao("G" + estA.getCaminhaoGrandeAtual().getId(), "Aguardando", "Estacao A");
        SimuladorGUI.atualizarCaminhao("G" + estB.getCaminhaoGrandeAtual().getId(), "Aguardando", "Estacao B");

        // Inicializa o gerenciador de zonas e vincula às estações
        GerenciadorZonas gerenciadorZonas = new GerenciadorZonas(estA, estB);
        estA.setGerenciadorZonas(gerenciadorZonas);
        estB.setGerenciadorZonas(gerenciadorZonas);
        gerenciadorZonas.setZonas(zonas);

        // Loop principal da simulação diária
        for (int dia = 1; dia <= dias; dia++) {
            System.out.println();
            System.out.println("---------------- COLETA DIA " + dia + " ------------");

            // Geração de lixo nas zonas
            System.out.println("Gerando lixo nas zonas...");
            for (int i = 0; i < zonas.getTamanho(); i++) {
                zonas.getValor(i).gerarLixoDiario();
            }

            // Distribuição dos caminhões por capacidade
            Lista<CaminhaoPequeno> caminhoes2t = PlanejadorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_2T, configuracao.VIAGENS_CAMINHOES_2T,
                    configuracao.CAPACIDADE_CAMINHOES_2T, gerenciadorZonas);

            Lista<CaminhaoPequeno> caminhoes4t = PlanejadorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_4T, configuracao.VIAGENS_CAMINHOES_4T,
                    configuracao.CAPACIDADE_CAMINHOES_4T, gerenciadorZonas);

            Lista<CaminhaoPequeno> caminhoes8t = PlanejadorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_8T, configuracao.VIAGENS_CAMINHOES_8T,
                    configuracao.CAPACIDADE_CAMINHOES_8T, gerenciadorZonas);

            Lista<CaminhaoPequeno> caminhoes10t = PlanejadorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_10T, configuracao.VIAGENS_CAMINHOES_10T,
                    configuracao.CAPACIDADE_CAMINHOES_10T, gerenciadorZonas);

            // Consolida todos os caminhões pequenos em uma lista única
            Lista<CaminhaoPequeno> todosCaminhoes = new Lista<>();
            for (int i = 0; i < caminhoes2t.getTamanho(); i++) {
                todosCaminhoes.adicionar(todosCaminhoes.getTamanho(), caminhoes2t.getValor(i));
            }
            for (int i = 0; i < caminhoes4t.getTamanho(); i++) {
                todosCaminhoes.adicionar(todosCaminhoes.getTamanho(), caminhoes4t.getValor(i));
            }
            for (int i = 0; i < caminhoes8t.getTamanho(); i++) {
                todosCaminhoes.adicionar(todosCaminhoes.getTamanho(), caminhoes8t.getValor(i));
            }
            for (int i = 0; i < caminhoes10t.getTamanho(); i++) {
                todosCaminhoes.adicionar(todosCaminhoes.getTamanho(), caminhoes10t.getValor(i));
            }

            // Informa os caminhões disponíveis ao gerenciador
            gerenciadorZonas.setCaminhoes(todosCaminhoes);

            System.out.println("Iniciando coleta...\n");

            // Processamento dos eventos agendados para o dia
            AgendaEventos.processarEventos(estatisticas);

            // Exibição do resumo estatístico diário
            int tempoFinal = AgendaEventos.getTempoUltimoEvento();
            System.out.println();
            System.out.println("+--------------------------------------------------+");
            System.out.println("|              RESUMO DO DIA " + dia + "                    |");
            System.out.println("+--------------------------------------------------+");
            System.out.printf("| %-18s | %-28s |%n", "Tempo Total", GerenciadorTempo.formatarDuracao(tempoFinal));
            System.out.printf("| %-18s | %-28s |%n", "Horário Encerramento", GerenciadorTempo.formatarHorarioSimulado(tempoFinal));
            for (int i = 0; i < zonas.getTamanho(); i++) {
                Zona zona = zonas.getValor(i);
                System.out.printf("| %-18s | %-28s |%n", "Lixo em " + zona.getNome(), zona.getLixoAcumulado() + " toneladas");
            }
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 2t", caminhoes2t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 4t", caminhoes4t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 8t", caminhoes8t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 10t", caminhoes10t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões Grandes", CaminhaoGrande.getNumeroTotalCriado());
            System.out.println("+--------------------------------------------------+");
            System.out.println();

            // Prepara o sistema para o próximo dia
            AgendaEventos.resetar();
            estatisticas.resetar();
        }

        System.out.println();
        System.out.println("=============== FIM DA SIMULAÇÃO ===============");
    }

    /**
     * Inicializa e retorna a lista de zonas geográficas utilizadas na simulação.
     * <p>
     * As zonas são geradas a partir da classe {@link Zonas}, que oferece
     * instâncias pré-configuradas das principais regiões da cidade:
     * Sul, Sudeste, Centro, Leste e Norte.
     *
     * @return Uma {@code Lista<Zona>} contendo todas as zonas da cidade.
     */
    public Lista<Zona> inicializarZonas() {
        Lista<Zona> zonas = new Lista<>();
        zonas.adicionar(0, Zonas.zonaSul());
        zonas.adicionar(1, Zonas.zonaSudeste());
        zonas.adicionar(2, Zonas.zonaCentro());
        zonas.adicionar(3, Zonas.zonaLeste());
        zonas.adicionar(4, Zonas.zonaNorte());
        return zonas;
    }
}
