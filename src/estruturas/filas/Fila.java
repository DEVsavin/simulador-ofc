package estruturas.filas;

/**
 * Fila genérica (FIFO) baseada em nós encadeados.
 *
 * @param <T> Tipo dos elementos da fila.
 */
public class Fila<T> {

    /**
     * Nó da fila, contém o valor e a referência para o próximo nó.
     */
    private static class No<T> {
        T valor;
        No<T> prox;

        No(T valor) {
            this.valor = valor;
            this.prox = null;
        }
    }

    private No<T> head, tail;
    private int tamanho;

    /**
     * Construtor. Inicializa a fila vazia.
     */
    public Fila() {
        head = null;
        tail = null;
        tamanho = 0;
    }

    /**
     * Adiciona um elemento no final da fila.
     *
     * @param valor o valor a ser adicionado
     * @return {@code true} se a inserção for bem-sucedida
     */
    public boolean enqueue(T valor) {
        No<T> novo = new No<>(valor);
        if (tail == null) {
            head = novo;
            tail = novo;
        } else {
            tail.prox = novo;
            tail = novo;
        }
        tamanho++;
        return true;
    }

    /**
     * Remove e retorna o elemento no início da fila.
     *
     * @return o valor removido, ou {@code null} se a fila estiver vazia
     */
    public T dequeue() {
        if (head == null) {
            System.out.println("Fila vazia");
            return null;
        }
        T valor = head.valor;
        head = head.prox;
        if (head == null) {
            tail = null;
        }
        tamanho--;
        return valor;
    }

    /**
     * Remove e retorna o elemento no início da fila sem mensagens.
     *
     * @return o valor removido, ou {@code null} se a fila estiver vazia
     */
    public T poll() {
        if (head == null) {
            return null;
        }
        T valor = head.valor;
        head = head.prox;
        if (head == null) {
            tail = null;
        }
        tamanho--;
        return valor;
    }

    /**
     * Verifica se a fila está vazia.
     *
     * @return {@code true} se estiver vazia, {@code false} caso contrário
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Exibe todos os elementos da fila.
     */
    public void printQueue() {
        if (head == null) {
            System.out.println("Fila vazia");
            return;
        }
        No<T> atual = head;
        while (atual != null) {
            System.out.print(atual.valor + " -> ");
            atual = atual.prox;
        }
        System.out.println("[EXIT]");
    }

    /**
     * Retorna o número de elementos na fila.
     *
     * @return o tamanho da fila
     */
    public int size() {
        return tamanho;
    }
}
