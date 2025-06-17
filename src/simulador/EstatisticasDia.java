package simulador;

public class EstatisticasDia {
    private int totalLixoColetado = 0;
    private int totalViagensColeta = 0;
    private int tempoTotalColeta = 0;
    private int totalViagensDescarga = 0;
    private int tempoTotalDescarga = 0;

    public void registrarColeta(int quantidade, int tempoGasto) {
        this.totalLixoColetado += quantidade;
        this.totalViagensColeta++;
        this.tempoTotalColeta += tempoGasto;
    }

    public void registrarDescarga(int tempoGasto) {
        this.totalViagensDescarga++;
        this.tempoTotalDescarga += tempoGasto;
    }

    public int getTotalLixoColetado() {
        return totalLixoColetado;
    }

    public double getTempoMedioColeta() {
        return (totalViagensColeta == 0) ? 0 : (double) tempoTotalColeta / totalViagensColeta;
    }

    public double getTempoMedioDescarga() {
        return (totalViagensDescarga == 0) ? 0 : (double) tempoTotalDescarga / totalViagensDescarga;
    }

    public void resetar() {
        totalLixoColetado = 0;
        totalViagensColeta = 0;
    }
}