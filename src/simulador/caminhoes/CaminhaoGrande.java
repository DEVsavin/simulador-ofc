package simulador.caminhoes;

import simulador.eventos.EventoVerificarEsperaCaminhaoGrande;
import simulador.SimuladorGUI;

/**
 * Um caminhão grande que leva lixo das estações de transferência até o aterro sanitário.
 * <p>
 * Cada caminhão tem um número único, pode carregar até 20 unidades de lixo e acompanha
 * quanto lixo está carregando no momento.
 */
public class CaminhaoGrande {
    private static int proximoId = 1; // Gera números únicos para cada caminhão
    private int id;
    private final int limiteCarga = 20; // Quantidade máxima de lixo que pode carregar
    private int cargaAtual;
    private EventoVerificarEsperaCaminhaoGrande eventoDeVerificacao;
    private boolean carregando;
    private int tempoMaximoEspera;

    /**
     * Cria um novo caminhão grande com um número único e sem lixo carregado.
     */
    public CaminhaoGrande() {
        this.id = proximoId++;
        this.cargaAtual = 0;
        this.carregando = true;
    }

    /**
     * Mostra o número único do caminhão.
     *
     * @return O número do caminhão
     */
    public int getId() {
        return id;
    }

    /**
     * (Não usado no momento)
     * <p>
     * Mostra quantas unidades de lixo o caminhão está carregando.
     *
     * @return Quantidade de lixo atual
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * (Não usado no momento)
     * <p>
     * Mostra o limite máximo de lixo que o caminhão pode carregar.
     *
     * @return Limite de carga (20 unidades)
     */
    public int getCapacidadeMaxima() {
        return limiteCarga;
    }

    /**
     * Verifica se o caminhão está cheio de lixo.
     *
     * @return Verdadeiro se o caminhão estiver no limite, falso se ainda couber mais
     */
    public boolean estaCheio() {
        return cargaAtual >= limiteCarga;
    }

    /**
     * Adiciona lixo ao caminhão. Se for muito, só carrega até o limite.
     *
     * @param quantidade Quantidade de lixo para adicionar
     */
    public void receberCarga(int quantidade) {
        // Adiciona lixo, mas respeita o limite máximo
        cargaAtual = Math.min(cargaAtual + quantidade, limiteCarga);
    }

    public EventoVerificarEsperaCaminhaoGrande getEventoDeVerificacao() {
        return eventoDeVerificacao;
    }

    public void setEventoDeVerificacao(EventoVerificarEsperaCaminhaoGrande evento) {
        this.eventoDeVerificacao = evento;
    }

    /**
     * Esvazia o caminhão no aterro sanitário.
     * <p>
     * Zera o lixo carregado e marca o caminhão como não carregando mais.
     */
    public void descarregar() {
        System.out.println("Caminhão grande #" + id + " foi para o aterro com " + cargaAtual + " unidades de lixo.");
        String truckId = "G" + id; // Exemplo de formatação
        SimuladorGUI.updateTruck(truckId, "Indo p/ Aterro", "Aterro");
        SimuladorGUI.pause();
        cargaAtual = 0; // Zera a carga
        carregando = false; // Finaliza o carregamento
    }
}