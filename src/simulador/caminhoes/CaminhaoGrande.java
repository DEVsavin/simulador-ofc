package simulador.caminhoes;

import simulador.eventos.EventoVerificarEsperaCaminhaoGrande;
import simulador.SimuladorGUI;

/**
 * Representa um caminhão de grande porte (20 toneladas).
 * Sua função é transportar o lixo consolidado das estações de transferência
 * até o aterro sanitário. Ele não coleta lixo diretamente das zonas.
 */
public class CaminhaoGrande {
    /** Contador estático para gerar IDs únicos para cada caminhão grande criado. */
    private static int proximoId = 1;

    /** O identificador único deste caminhão. */
    private int id;

    /** A capacidade máxima de carga do caminhão, fixada em 20 toneladas. */
    private final int limiteCarga = 20;

    /** A quantidade de lixo que o caminhão está carregando no momento. */
    private int cargaAtual;

    /** Referência ao evento agendado para verificar sua tolerância de espera. */
    private EventoVerificarEsperaCaminhaoGrande eventoDeVerificacao;

    /** Indica se o caminhão está em processo de carregamento na estação. */
    private boolean carregando;

    /** Atributo não utilizado na implementação atual. */
    private int tempoMaximoEspera;

    /**
     * Cria um novo caminhão grande, atribuindo-lhe um ID único e sequencial.
     * O caminhão inicia vazio e em estado de carregamento.
     */
    public CaminhaoGrande() {
        this.id = proximoId++;
        this.cargaAtual = 0;
        this.carregando = true;
    }

    /**
     * Retorna o ID único do caminhão.
     * @return O ID do caminhão.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna a quantidade de lixo que o caminhão está carregando atualmente.
     * @return A carga atual em toneladas.
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * Verifica se o caminhão atingiu sua capacidade máxima de carga.
     * @return {@code true} se a carga atual for maior ou igual ao limite, {@code false} caso contrário.
     */
    public boolean estaCheio() {
        return cargaAtual >= limiteCarga;
    }

    /**
     * Adiciona uma quantidade de lixo à carga atual do caminhão.
     * A carga é limitada pela capacidade máxima do caminhão.
     * @param quantidade A quantidade de lixo a ser adicionada.
     */
    public void receberCarga(int quantidade) {
        cargaAtual = Math.min(cargaAtual + quantidade, limiteCarga);
    }

    /**
     * Retorna o evento de verificação de espera associado a este caminhão.
     * @return O evento de verificação agendado, ou null se não houver.
     */
    public EventoVerificarEsperaCaminhaoGrande getEventoDeVerificacao() {
        return eventoDeVerificacao;
    }

    /**
     * Define o evento de verificação de espera para este caminhão.
     * @param evento O evento a ser associado.
     */
    public void setEventoDeVerificacao(EventoVerificarEsperaCaminhaoGrande evento) {
        this.eventoDeVerificacao = evento;
    }

    /**
     * Simula a viagem do caminhão ao aterro para descarregar todo o lixo.
     * Zera a carga atual, atualiza seu status e notifica a interface gráfica.
     */
    public void descarregar() {
        System.out.println("Caminhão grande #" + id + " foi para o aterro com " + cargaAtual + " unidades de lixo.");
        String truckId = "G" + id;
        SimuladorGUI.updateTruck(truckId, "Indo p/ Aterro", "Aterro");
        SimuladorGUI.pause();
        cargaAtual = 0;
        carregando = false;
    }

    /**
     * Retorna o número total de caminhões grandes criados durante a simulação.
     * @return O total de caminhões grandes instanciados.
     */
    public static int getNumeroTotalCriado() {
        return proximoId - 1;
    }

    /**
     * Reseta o contador estático de IDs para o início.
     * Essencial para rodar novas simulações sem reiniciar o programa.
     */
    public static void resetarContadorDeId() {
        proximoId = 1;
    }
}