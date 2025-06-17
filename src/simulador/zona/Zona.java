package simulador.zona;

import java.util.Random;

/**
 * Representa uma zona geográfica da cidade na simulação.
 * Cada zona é responsável por gerar uma quantidade diária de lixo e manter
 * o controle do lixo acumulado que aguarda coleta.
 */
public class Zona {

    /** O nome identificador da zona (ex: "Sul", "Norte"). */
    private String nome;

    /** A quantidade mínima de lixo que pode ser gerada por dia. */
    private int lixoMinimo;

    /** A quantidade máxima de lixo que pode ser gerada por dia. */
    private int lixoMaximo;

    /** A quantidade atual de lixo acumulado na zona, em toneladas. */
    private int lixoAcomulado;

    /**
     * Cria uma zona com um nome e limites para a geração de lixo.
     *
     * @param nome          O nome da zona.
     * @param lixoMinimo    O mínimo de lixo (toneladas) gerado por dia.
     * @param lixoMaximo    O máximo de lixo (toneladas) gerado por dia.
     */
    public Zona(String nome, int lixoMinimo, int lixoMaximo) {
        this.nome = nome;
        this.lixoMinimo = lixoMinimo;
        this.lixoMaximo = lixoMaximo;
        this.lixoAcomulado = 0;
    }

    /**
     * Simula a geração diária de lixo na zona.
     * Uma quantidade aleatória de lixo, baseada nos limites mínimo e máximo,
     * é definida como o total acumulado para o dia.
     */
    public void gerarLixoDiario() {
        this.lixoAcomulado = new Random().nextInt(lixoMaximo - lixoMinimo + 1) + lixoMinimo;
        System.out.println("[Zona] " + nome + " gerou " + lixoAcomulado + " toneladas de lixo.");
    }

    /**
     * Simula a remoção de lixo da zona por um caminhão.
     * A quantidade de lixo coletado é limitada ao total atualmente acumulado,
     * garantindo que o lixo na zona não se torne negativo.
     *
     * @param quantidade A quantidade de lixo que o caminhão tenta coletar.
     * @return A quantidade de lixo que foi efetivamente coletada em toneladas.
     */
    public int coletarLixo(int quantidade) {
        int coletado = Math.min(quantidade, lixoAcomulado);
        lixoAcomulado -= coletado;
        return coletado;
    }

    /**
     * Retorna a quantidade atual de lixo acumulado na zona.
     *
     * @return A quantidade de lixo em toneladas.
     */
    public int getLixoAcumulado() {
        return lixoAcomulado;
    }

    /**
     * Verifica se a zona não possui mais lixo acumulado.
     *
     * @return {@code true} se a quantidade de lixo for zero, {@code false} caso contrário.
     */
    public boolean estaLimpa() {
        return lixoAcomulado == 0;
    }

    /**
     * Retorna o nome da zona.
     *
     * @return O nome identificador da zona.
     */
    public String getNome() {
        return nome;
    }
}