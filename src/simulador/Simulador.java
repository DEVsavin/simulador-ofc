package simulador;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoGrande; // IMPORT ADICIONADO (se não estiver presente)
import simulador.caminhoes.CaminhaoPequeno;
import simulador.configuracao.configuracao;
import simulador.estacoes.EstacaoDeTransferencia;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoDistribuidorDeRotas;
import simulador.configTempo.GerenciadorTempo;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;
import simulador.zona.Zonas;

/**
 * Coordena a simulação de coleta de lixo com zonas, caminhões e estações.
 */
public class Simulador {

    /**
     * Inicia a simulação para um número de dias, configurando zonas, estações, caminhões e eventos.
     *
     * @param dias Número de dias a simular
     */
    public void iniciarSimulacao(int dias) {
        Lista<Zona> zonas = inicializarZonas();
        EstatisticasDia estatisticas = new EstatisticasDia();

        // 1. Cria as estações
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("B");

        // Notifica a GUI para desenhar os caminhões grandes iniciais em suas estações
        SimuladorGUI.updateTruck("G" + estA.getCaminhaoGrandeAtual().getId(), "Aguardando", "Estacao A");
        SimuladorGUI.updateTruck("G" + estB.getCaminhaoGrandeAtual().getId(), "Aguardando", "Estacao B");

        // 2. Cria o gerenciador, passando as estações para ele
        GerenciadorZonas gerenciadorZonas = new GerenciadorZonas(estA, estB);

        // 3. Informa às estações sobre o gerenciador (para evitar dependência circular)
        estA.setGerenciadorZonas(gerenciadorZonas);
        estB.setGerenciadorZonas(gerenciadorZonas);

        // 4. Define as zonas no gerenciador
        gerenciadorZonas.setZonas(zonas);

        for (int dia = 1; dia <= dias; dia++) {
            System.out.println();
            System.out.println("---------------- COLETA DIA " + dia + " ------------");

            // Gera lixo nas zonas
            System.out.println("Gerando lixo nas zonas...");
            for (int i = 0; i < zonas.getTamanho(); i++) {
                zonas.getValor(i).gerarLixoDiario();
            }

            // Distribui caminhões passando a instância do gerenciador
            Lista<CaminhaoPequeno> caminhoes2t = EventoDistribuidorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_2T,
                    configuracao.VIAGENS_CAMINHOES_2T,
                    configuracao.CAPACIDADE_CAMINHOES_2T,
                    gerenciadorZonas);

            Lista<CaminhaoPequeno> caminhoes4t = EventoDistribuidorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_4T,
                    configuracao.VIAGENS_CAMINHOES_4T,
                    configuracao.CAPACIDADE_CAMINHOES_4T,
                    gerenciadorZonas);

            Lista<CaminhaoPequeno> caminhoes8t = EventoDistribuidorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_8T,
                    configuracao.VIAGENS_CAMINHOES_8T,
                    configuracao.CAPACIDADE_CAMINHOES_8T,
                    gerenciadorZonas);

            Lista<CaminhaoPequeno> caminhoes10t = EventoDistribuidorDeRotas.distribuir(zonas,
                    configuracao.QTD_CAMINHOES_10T,
                    configuracao.VIAGENS_CAMINHOES_10T,
                    configuracao.CAPACIDADE_CAMINHOES_10T,
                    gerenciadorZonas);


            // Define as listas de caminhões na instância do gerenciador
            gerenciadorZonas.setCaminhoes(caminhoes2t);
            gerenciadorZonas.setCaminhoes(caminhoes4t);
            gerenciadorZonas.setCaminhoes(caminhoes8t);
            gerenciadorZonas.setCaminhoes(caminhoes10t);

            System.out.println("Iniciando coleta...\n");

            // Processa eventos
            AgendaEventos.processarEventos(estatisticas);

            // Exibe resumo do dia
            int tempoFinal = AgendaEventos.getTempoUltimoEvento();
            System.out.println();
            System.out.println("+--------------------------------------------------+");
            System.out.println("|              RESUMO DO DIA " + dia + "                    |");
            System.out.println("+--------------------------------------------------+");
            System.out.printf("| %-18s | %-28s |%n", "Tempo Total", GerenciadorTempo.formatarDuracao(tempoFinal));
            System.out.printf("| %-18s | %-28s |%n", "Horário Encerramento", GerenciadorTempo.formatarHorarioSimulado(tempoFinal));
            for (int i = 0; i < zonas.getTamanho(); i++) {
                Zona zona = zonas.getValor(i);
                System.out.printf("| %-18s | %-28s |%n", "Zona " + zona.getNome(), zona.getLixoAcumulado() + " toneladas");
            }
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 2t", caminhoes2t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 4t", caminhoes4t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 8t", caminhoes8t.getTamanho());
            System.out.printf("| %-18s | %-28d |%n", "Caminhões de 10t", caminhoes10t.getTamanho());

            // --- LINHA ADICIONADA ---
            System.out.printf("| %-18s | %-28d |%n", "Caminhões Grandes", CaminhaoGrande.getNumeroTotalCriado());

            System.out.println("+--------------------------------------------------+");
            System.out.println();

            // Reseta eventos para o próximo dia
            AgendaEventos.resetar();
            estatisticas.resetar();
        }

        System.out.println();
        System.out.println("=============== FIM DA SIMULAÇÃO ===============");
    }

    /**
     * Cria a lista de zonas da simulação.
     *
     * @return Lista com zonas Sul, Sudeste, Centro, Leste e Norte
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