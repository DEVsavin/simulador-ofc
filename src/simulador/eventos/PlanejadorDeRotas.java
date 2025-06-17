package simulador.eventos;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;

/**
 * Responsável por criar a frota inicial de caminhões pequenos para um dia de simulação.
 * Atua como uma "fábrica" que não apenas instancia os caminhões, mas também lhes
 * atribui rotas balanceadas e agenda sua primeira tarefa de coleta na {@link AgendaEventos}.
 */
public class PlanejadorDeRotas {
    /**
     * Cria caminhões pequenos, distribui as zonas de forma cíclica para formar suas rotas
     * e agenda o primeiro evento de coleta para cada um.
     *
     * @param zonas               A lista completa de zonas disponíveis na cidade.
     * @param quantidadeCaminhoes   O número total de caminhões a serem criados.
     * @param viagensPorCaminhao  O número de viagens que cada caminhão pode realizar por dia.
     * @param capacidadeCaminhao  A capacidade de carga (em toneladas) de cada caminhão.
     * @param gerenciador         A instância do {@link GerenciadorZonas}, necessária para agendar os eventos.
     * @return Uma {@code Lista<CaminhaoPequeno>} com todos os caminhões recém-criados.
     */
    public static Lista<CaminhaoPequeno> distribuir(Lista<Zona> zonas, int quantidadeCaminhoes, int viagensPorCaminhao, int capacidadeCaminhao, GerenciadorZonas gerenciador) {
        Lista<CaminhaoPequeno> caminhoes = new Lista<>();
        int quantidadeZonas = zonas.getTamanho();

        for (int i = 0; i < quantidadeCaminhoes; i++) {
            Lista<Zona> rotaCaminhao = new Lista<>();

            /** Atribui zonas de forma cíclica para balancear as rotas entre os caminhões.
             * A fórmula (i + j) % quantidadeZonas garante que cada caminhão comece
             * em uma zona diferente, distribuindo a frota pela cidade.
             */
            for (int j = 0; j < viagensPorCaminhao; j++) {
                Zona zonaAlvo = zonas.getValor((i + j) % quantidadeZonas);
                rotaCaminhao.adicionar(j, zonaAlvo);
            }

            String id = "C" + (i + 1);

            CaminhaoPequeno caminhao = new CaminhaoPequeno(id, capacidadeCaminhao, viagensPorCaminhao, rotaCaminhao);
            caminhoes.adicionar(i, caminhao);

            AgendaEventos.adicionarEvento(new EventoColeta(0, caminhao, caminhao.getZonaAlvo(), gerenciador));
        }

        return caminhoes;
    }
}