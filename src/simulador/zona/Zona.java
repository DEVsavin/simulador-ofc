package simulador.zona;

import java.util.Random;

/**
 * Representa uma zona geográfica com geração e acúmulo de lixo.
 */
public class Zona {

    private String nome;
    private int lixoMinimo;
    private int lixoMaximo;
    private int lixoAcomulado;

    /**
     * Cria uma zona com nome e limites de geração de lixo.
     *
     * @param nome Nome da zona
     * @param lixoMinimo Mínimo de lixo gerado por dia
     * @param lixoMaximo Máximo de lixo gerado por dia
     */
    public Zona(String nome, int lixoMinimo, int lixoMaximo) {
        this.nome = nome;
        this.lixoMinimo = lixoMinimo;
        this.lixoMaximo = lixoMaximo;
        this.lixoAcomulado = 0;
    }

    /**
     * Gera uma quantidade aleatória de lixo diário.
     */
    public void gerarLixoDiario() {
        this.lixoAcomulado = new Random().nextInt(lixoMaximo - lixoMinimo + 1) + lixoMinimo;
        System.out.println("[Zona] " + nome + " gerou " + lixoAcomulado + " toneladas de lixo.");
    }

    /**
     * Coleta lixo da zona, respeitando o acumulado.
     *
     * @param quantidade Quantidade desejada para coleta
     * @return Quantidade coletada
     */
    public int coletarLixo(int quantidade) {
        int coletado = Math.min(quantidade, lixoAcomulado);
        lixoAcomulado -= coletado;
        return coletado;
    }

    /**
     * Verifica se há lixo acumulado.
     *
     * @return True se houver lixo, false se vazia
     */
    public boolean temLixoRestante() {
        return lixoAcomulado > 0;
    }

    /**
     * Retorna o lixo acumulado.
     *
     * @return Lixo acumulado em toneladas
     */
    public int getLixoAcumulado() {
        return lixoAcomulado;
    }

    /**
     * Verifica se a zona está limpa.
     *
     * @return True se não houver lixo
     */
    public boolean estaLimpa() {
        return lixoAcomulado == 0;
    }

    /**
     * Verifica se a zona precisa de coleta.
     *
     * @param limiteMinimo Limite mínimo para coleta
     * @return True se o lixo acumulado atingir o limite
     */
    public boolean precisaDeColeta(int limiteMinimo) {
        return lixoAcomulado >= limiteMinimo;
    }

    /**
     * Retorna o nome da zona.
     *
     * @return Nome da zona
     */
    public String getNome() {
        return nome;
    }
}