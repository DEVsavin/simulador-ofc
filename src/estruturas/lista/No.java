package estruturas.lista;


/**
 * Classe que representa um nó genérico em uma estrutura encadeada dupla.
 *
 * @param <T> o tipo de valor armazenado no nó
 */
public class No<T> {
    private T valor;
    private No<T> prox;
    private No<T> prev;

    /**
     * Construtor que inicializa o nó com um valor.
     *
     * @param valor o valor a ser armazenado no nó
     */
    public No(T valor) {
        this.valor = valor;
        this.prox = null;
        this.prev = null;
    }

    /**
     * Retorna o valor armazenado neste nó.
     *
     * @return o valor contido no nó
     */
    public T getValor() {
        return valor;
    }

    /**
     * Retorna a referência para o próximo nó.
     *
     * @return o próximo nó ou {@code null} se não houver
     */
    public No<T> getProx() {
        return prox;
    }

    /**
     * Define a referência para o próximo nó.
     *
     * @param prox o próximo nó
     */
    public void setProx(No<T> prox) {
        this.prox = prox;
    }

    /**
     * Retorna a referência para o nó anterior.
     *
     * @return o nó anterior ou {@code null} se não houver
     */
    public No<T> getPrev() {
        return prev;
    }

    /**
     * Define a referência para o nó anterior.
     *
     * @param prev o nó anterior
     */
    public void setPrev(No<T> prev) {
        this.prev = prev;
    }
}