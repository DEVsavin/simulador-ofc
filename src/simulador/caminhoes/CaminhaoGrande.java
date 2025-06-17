package simulador.caminhoes;

import simulador.eventos.EventoVerificarEsperaCaminhaoGrande;
import simulador.SimuladorGUI;

public class CaminhaoGrande {
    private static int proximoId = 1;
    private int id;
    private final int limiteCarga = 20;
    private int cargaAtual;
    private EventoVerificarEsperaCaminhaoGrande eventoDeVerificacao;
    private boolean carregando;
    private int tempoMaximoEspera;

    public CaminhaoGrande() {
        this.id = proximoId++;
        this.cargaAtual = 0;
        this.carregando = true;
    }

    public int getId() {
        return id;
    }

    public int getCargaAtual() {
        return cargaAtual;
    }

    public int getCapacidadeMaxima() {
        return limiteCarga;
    }

    public boolean estaCheio() {
        return cargaAtual >= limiteCarga;
    }

    public void receberCarga(int quantidade) {
        cargaAtual = Math.min(cargaAtual + quantidade, limiteCarga);
    }

    public EventoVerificarEsperaCaminhaoGrande getEventoDeVerificacao() {
        return eventoDeVerificacao;
    }

    public void setEventoDeVerificacao(EventoVerificarEsperaCaminhaoGrande evento) {
        this.eventoDeVerificacao = evento;
    }

    public void descarregar() {
        System.out.println("Caminhão grande #" + id + " foi para o aterro com " + cargaAtual + " unidades de lixo.");
        String truckId = "G" + id;
        SimuladorGUI.updateTruck(truckId, "Indo p/ Aterro", "Aterro");
        SimuladorGUI.pause();
        cargaAtual = 0;
        carregando = false;
    }

    public static int getNumeroTotalCriado() {
        return proximoId - 1;
    }

    /**
     * NOVO MÉTODO: Reseta o contador estático de IDs para o início.
     * Essencial para rodar novas simulações sem reiniciar o programa.
     */
    public static void resetarContadorDeId() {
        proximoId = 1;
    }
}