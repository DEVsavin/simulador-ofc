package simulador.eventos;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.zona.GerenciadorZonas; // Import necessário
import simulador.zona.Zona;

/**
 * Distribui zonas entre caminhões pequenos e agenda seus primeiros eventos de coleta.
 */
public class EventoDistribuidorDeRotas {
    /**
     * Cria caminhões pequenos com rotas balanceadas e agenda coletas iniciais.
     *
     * @param zonas Lista de zonas disponíveis
     * @param quantidadeCaminhoes Número de caminhões a criar
     * @param viagensPorCaminhao Número de viagens por caminhão
     * @param capacidadeCaminhao Capacidade de cada caminhão
     * @param gerenciador A instância do gerenciador de zonas da simulação
     * @return Lista de caminhões criados
     */
    // MÉTODO MODIFICADO PARA RECEBER O GERENCIADOR
    public static Lista<CaminhaoPequeno> distribuir(Lista<Zona> zonas, int quantidadeCaminhoes, int viagensPorCaminhao, int capacidadeCaminhao, GerenciadorZonas gerenciador) {
        Lista<CaminhaoPequeno> caminhoes = new Lista<>();
        int quantidadeZonas = zonas.getTamanho();

        for (int i = 0; i < quantidadeCaminhoes; i++) {
            Lista<Zona> rotaCaminhao = new Lista<>();

            // Atribui zonas às viagens do caminhão
            for (int j = 0; j < viagensPorCaminhao; j++) {
                Zona zonaAlvo = zonas.getValor((i + j) % quantidadeZonas);
                rotaCaminhao.adicionar(j, zonaAlvo);
            }

            String id = "C" + (i + 1);

            CaminhaoPequeno caminhao = new CaminhaoPequeno(id, capacidadeCaminhao, viagensPorCaminhao, rotaCaminhao);
            caminhoes.adicionar(i, caminhao);

            // LÓGICA CORRIGIDA: Passa o 'gerenciador' para o construtor do EventoColeta
            AgendaEventos.adicionarEvento(new EventoColeta(0, caminhao, caminhao.getZonaAlvo(), gerenciador));
        }

        return caminhoes;
    }
}