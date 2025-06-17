package simulador.zona;

import estruturas.lista.Lista;
import simulador.caminhoes.CaminhaoPequeno;
import simulador.estacoes.EstacaoDeTransferencia;

/**
 * Mapeia zonas da cidade às estações de transferência.
 * Esta classe agora funciona como um objeto que é instanciado no início da simulação.
 */
public class GerenciadorZonas {

    private EstacaoDeTransferencia estacaoA;
    private EstacaoDeTransferencia estacaoB;
    private Lista<Zona> zonas;
    private Lista<CaminhaoPequeno> caminhoes;

    /**
     * Construtor que configura o gerenciador com as estações de transferência.
     *
     * @param a Estação A (Leste, Norte, Centro)
     * @param b Estação B (Sul, Sudeste)
     */
    public GerenciadorZonas(EstacaoDeTransferencia a, EstacaoDeTransferencia b) {
        this.estacaoA = a;
        this.estacaoB = b;
    }

    /**
     * Retorna a estação correspondente a uma zona.
     *
     * @param zona Zona a ser verificada
     * @return Estação responsável
     * @throws IllegalArgumentException se a zona for desconhecida
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
     * Define a lista de zonas da simulação.
     *
     * @param listaZonas Lista de zonas
     */
    public void setZonas(Lista<Zona> listaZonas) {
        this.zonas = listaZonas;
    }

    /**
     * Define a lista de caminhões da simulação.
     *
     * @param listaCaminhoes Lista de caminhões
     */
    public void setCaminhoes(Lista<CaminhaoPequeno> listaCaminhoes) {
        this.caminhoes = listaCaminhoes;
    }

    /**
     * Retorna a lista de todas as zonas.
     *
     * @return Lista de zonas
     */
    public Lista<Zona> getTodasZonas() {
        return zonas;
    }

    /**
     * Retorna os caminhões com viagens disponíveis.
     *
     * @return Lista de caminhões ativos
     */
    public Lista<CaminhaoPequeno> getCaminhoesAtivos() {
        if (caminhoes == null) {
            return new Lista<>(); // Retorna uma lista vazia se não houver caminhões definidos
        }

        Lista<CaminhaoPequeno> ativos = new Lista<>();
        for (int i = 0; i < caminhoes.getTamanho(); i++) {
            CaminhaoPequeno caminhao = caminhoes.getValor(i);
            if (caminhao.podeRealizarNovaViagem()) {
                ativos.adicionar(ativos.getTamanho(), caminhao);
            }
        }
        return ativos;
    }
}