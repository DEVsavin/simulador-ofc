package estruturas.lista;

import java.util.Comparator;

/**
 * Implementação de uma lista duplamente encadeada genérica.
 *
 * @param <T> o tipo dos elementos armazenados na lista
 */
public class Lista<T> {
    private No<T> head;
    private No<T> tail;
    private int tamanho;

    /**
     * Constrói uma lista vazia.
     */
    public Lista() {
        head = null;
        tail = null;
        tamanho = 0;
    }

    // ========== OPERAÇÕES BÁSICAS ==========

    /**
     * Adiciona um elemento na posição especificada.
     *
     * @param pos a posição onde o elemento será inserido (0-based)
     * @param valor o valor a ser inserido
     * @return true se a inserção foi bem-sucedida, false caso contrário
     * @throws IndexOutOfBoundsException se a posição for inválida (negativa ou maior que o tamanho)
     */
    public boolean adicionar(int pos, T valor) {
        if (pos < 0 || pos > tamanho) {
            throw new IndexOutOfBoundsException("Posição inválida: " + pos);
        }

        No<T> novo = new No<>(valor);

        if (pos == 0) {
            if (head == null) {
                head = novo;
                tail = novo;
            } else {
                novo.setProx(head);
                head.setPrev(novo);
                head = novo;
            }
        } else if (pos == tamanho) {
            tail.setProx(novo);
            novo.setPrev(tail);
            tail = novo;
        } else {
            No<T> atual = head;
            for (int i = 0; i < pos - 1; i++) {
                atual = atual.getProx();
            }
            novo.setProx(atual.getProx());
            atual.setProx(novo);
            novo.getProx().setPrev(novo);
            novo.setPrev(atual);
        }
        tamanho++;
        return true;
    }

    /**
     * Adiciona um elemento na lista mantendo a ordem definida pelo comparador.
     *
     * @param elemento o elemento a ser adicionado
     * @param comparador o comparador para definir a ordem
     * @throws IllegalArgumentException se elemento ou comparador forem nulos
     */
    public void adicionarOrdenado(T elemento, Comparator<T> comparador) {
        if (elemento == null || comparador == null) {
            throw new IllegalArgumentException("Elemento e comparador não podem ser nulos");
        }

        // Caso especial: lista vazia ou inserção no início
        if (head == null || comparador.compare(elemento, head.getValor()) <= 0) {
            adicionar(0, elemento);
            return;
        }

        // Caso especial: inserção no final
        if (comparador.compare(elemento, tail.getValor()) >= 0) {
            adicionar(tamanho, elemento);
            return;
        }

        // Procura a posição correta no meio da lista
        No<T> atual = head;
        int pos = 0;
        while (atual != null && comparador.compare(elemento, atual.getValor()) > 0) {
            atual = atual.getProx();
            pos++;
        }

        adicionar(pos, elemento);
    }

    /**
     * Remove e retorna o elemento no início da lista.
     *
     * @return o elemento removido, ou null se a lista estiver vazia
     */
    public T removerHead() {
        if (head == null) {
            return null;
        }
        T valor = head.getValor();
        head = head.getProx();
        if (head != null) {
            head.setPrev(null);
        } else {
            tail = null; // Lista ficou vazia
        }
        tamanho--;
        return valor;
    }

    public boolean removerProcurado(T elemento) {
    if (elemento == null || head == null) return false;

    No<T> atual = head;

    while (atual != null) {
        if (atual.getValor().equals(elemento)) {
            // Caso 1: elemento é o head
            if (atual == head) {
                head = atual.getProx();
                if (head != null) head.setPrev(null);
            }
            // Caso 2: elemento é o tail
            else if (atual == tail) {
                tail = atual.getPrev();
                if (tail != null) tail.setProx(null);
            }
            // Caso 3: meio da lista
            else {
                atual.getPrev().setProx(atual.getProx());
                atual.getProx().setPrev(atual.getPrev());
            }

            tamanho--;
            return true;
        }

        atual = atual.getProx();
    }

    return false; // elemento não encontrado
}


    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Retorna o nó na posição especificada.
     *
     * @param pos a posição do nó (0-based)
     * @return o nó na posição especificada, ou null se a posição for inválida
     */
    private No<T> getNo(int pos) {
        if (pos < 0 || pos >= tamanho) {
            return null;
        }

        No<T> atual;
        // Otimização: decide começar do início ou do fim para melhor performance
        if (pos < tamanho / 2) {
            atual = head;
            for (int i = 0; i < pos; i++) {
                atual = atual.getProx();
            }
        } else {
            atual = tail;
            for (int i = tamanho - 1; i > pos; i--) {
                atual = atual.getPrev();
            }
        }
        return atual;
    }

    public int getTamanho() {
        return tamanho;
    }

    /**
     * Verifica se a lista está vazia.
     *
     * @return true se a lista estiver vazia, false caso contrário
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Retorna o valor armazenado na posição indicada.
     *
     * @param pos posição desejada (0-based)
     * @return valor armazenado, ou null se a posição for inválida
     */
    public T getValor(int pos) {
        No<T> no = getNo(pos); // getNo já existe e percorre a lista corretamente
        return no != null ? no.getValor() : null;
    }
}