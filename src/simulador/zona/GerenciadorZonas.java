package simulador.zona;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;

/**
 * Mapeia zonas da cidade às suas respectivas estações de transferência.
 * Esta classe funciona como um objeto central de configuração que é instanciado
 * no início da simulação para gerenciar as relações geográficas e a frota.
 */
public class GerenciadorZonas {

    private EstacaoDeTransferencia estacaoA;
    private EstacaoDeTransferencia estacaoB;
    private Lista<Zona> zonas;
    private Lista<CaminhaoPequeno> caminhoes;

    /**
     * Construtor que configura o gerenciador com as duas estações de transferência.
     *
     * @param a Estação de Transferência A (atende Leste, Norte, Centro).
     * @param b Estação de Transferência B (atende Sul, Sudeste).
     */
    public GerenciadorZonas(EstacaoDeTransferencia a, EstacaoDeTransferencia b) {
        this.estacaoA = a;
        this.estacaoB = b;
    }

    /**
     * Retorna a estação de transferência responsável por uma determinada zona.
     *
     * @param zona A zona a ser verificada.
     * @return A instância da {@link EstacaoDeTransferencia} correspondente.
     * @throws IllegalArgumentException se a zona não for mapeada para nenhuma estação.
     */
    public EstacaoDeTransferencia getEstacaoPara(Zona zona) {
        String nome = zona.getNome().toLowerCase();

        if (nome.equals("leste") || nome.equals("norte") || nome.equals("centro")) {
            return estacaoA;
        }

        if (nome.equals("sul") || nome.equals("sudeste")) {
            return estacaoB;
        }

        throw new IllegalArgumentException("Zona desconhecida: " + zona.getNome());
    }

    /**
     * Define a lista de zonas geográficas que fazem parte da simulação.
     *
     * @param listaZonas A lista completa de zonas.
     */
    public void setZonas(Lista<Zona> listaZonas) {
        this.zonas = listaZonas;
    }

    /**
     * Define a frota de caminhões pequenos da simulação.
     *
     * @param listaCaminhoes A lista de caminhões pequenos a ser gerenciada.
     */
    public void setCaminhoes(Lista<CaminhaoPequeno> listaCaminhoes) {
        this.caminhoes = listaCaminhoes;
    }

}