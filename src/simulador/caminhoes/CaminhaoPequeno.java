package simulador.caminhoes;

import estruturas.lista.Lista;
import simulador.EstatisticasDia;
import simulador.SimuladorGUI;
import simulador.configTempo.GerenciadorTempo;
import simulador.configTempo.TempoDetalhado;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.eventos.EventoGerarCaminhaoGrande;
import simulador.eventos.EventoTransferenciaParaEstacao;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;

/**
 * Um caminhão pequeno que pega lixo nas zonas da cidade.
 * Agora, ele é responsável por sua própria lógica de coleta.
 */
public class CaminhaoPequeno {

    private String id;
    private int capacidadeMaxima;
    private int cargaAtual;
    private int numeroDeViagensDiarias;
    private Lista<Zona> rota;
    private int indiceRota = 0;
    private Zona zonaAlvo;
    private EventoGerarCaminhaoGrande eventoAgendado;

    public CaminhaoPequeno(String id, int capacidadeMaxima, int numeroDeViagensDiarias, Lista<Zona> rota) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
        this.cargaAtual = 0;
        this.rota = rota;
        this.indiceRota = 0;
        this.zonaAlvo = rota.getValor(0);
    }

    // NOVO MÉTODO - Contém toda a lógica de coleta
    public void realizarColeta(int tempoAtual, Zona zona, GerenciadorZonas gerenciadorZonas, EstatisticasDia estatisticas) {
        SimuladorGUI.updateTruck(this.id, "Coletando", zona.getNome());
        SimuladorGUI.pause();
        // Caso 1: Zona sem lixo
        if (zona.getLixoAcumulado() == 0) {
            System.out.println("  • Zona " + zona.getNome() + " está limpa. Nenhuma coleta realizada pelo caminhão " + this.id);
            this.registrarViagem();

            if (this.podeRealizarNovaViagem()) {
                boolean mudouZona = this.atualizarProximaZonaAlvo();
                if (mudouZona) {
                    AgendaEventos.adicionarEvento(new EventoColeta(tempoAtual + 30, this, this.getZonaAlvo(), gerenciadorZonas));
                } else {
                    System.out.println("  • Todas as zonas da rota do caminhão " + this.id + " estão limpas.");
                    AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempoAtual, this, zona, gerenciadorZonas));
                }
            } else {
                AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempoAtual, this, zona, gerenciadorZonas));
            }
            return;
        }

        // Caso 2: Realiza a coleta
        boolean coletou = false;
        int totalColetadoNaRodada = 0;
        int espacoDisponivel = this.capacidadeMaxima - this.cargaAtual;
        int lixoDisponivel = zona.getLixoAcumulado();
        int quantidadeReal = Math.min(espacoDisponivel, lixoDisponivel);

        if (quantidadeReal > 0) {
            String horarioAtual = GerenciadorTempo.formatarHorarioSimulado(tempoAtual);
            System.out.println("+--------------------------------------------------+");
            System.out.println("|                  COLETA DE LIXO                  |");
            System.out.println("+--------------------------------------------------+");
            System.out.printf("| %-18s | %-28s |%n", "Horário Inicial", horarioAtual);
            System.out.printf("| %-18s | %-28s |%n", "Caminhão", this.id);
            System.out.printf("| %-18s | %-28s |%n", "Zona", zona.getNome());
            System.out.printf("| %-18s | %-28d |%n", "Viagens Restantes", this.numeroDeViagensDiarias);

            coletou = this.coletar(quantidadeReal);
            if (coletou) {
                zona.coletarLixo(quantidadeReal);
                totalColetadoNaRodada += quantidadeReal;
                System.out.printf("| %-18s | %-28s |%n", "Quantidade Coletada", quantidadeReal + " toneladas (Carga: " + this.cargaAtual + "/" + this.capacidadeMaxima + ")");
            }
        }

        // Agendamento do próximo passo
        if (this.podeRealizarNovaViagem() && coletou) {
            TempoDetalhado tempoDetalhado = GerenciadorTempo.calcularTempoDetalhado(tempoAtual, totalColetadoNaRodada, false);
            estatisticas.registrarColeta(totalColetadoNaRodada, tempoDetalhado.tempoTotal);

            System.out.printf("| %-18s | %-28s |%n", "Tempo de Coleta", GerenciadorTempo.formatarDuracao(tempoDetalhado.tempoColeta));
            System.out.printf("| %-18s | %-28s |%n", "Tempo Total", GerenciadorTempo.formatarDuracao(tempoDetalhado.tempoTotal));
            System.out.printf("| %-18s | %-28s |%n", "Horário Final", GerenciadorTempo.formatarHorarioSimulado(tempoAtual + tempoDetalhado.tempoTotal));
            System.out.println("+--------------------------------------------------+");
            System.out.println();

            // Agenda o próximo evento, que pode ser na mesma zona ou em outra.
            AgendaEventos.adicionarEvento(new EventoColeta(tempoAtual + tempoDetalhado.tempoTotal, this, zona, gerenciadorZonas));
        } else if (this.cargaAtual > 0) {
            // Se não pode mais coletar (sem viagens ou cheio), mas tem carga, vai para a estação.
            this.registrarViagem();
            AgendaEventos.adicionarEvento(new EventoTransferenciaParaEstacao(tempoAtual, this, zona, gerenciadorZonas));
        }
    }

    // ========== MÉTODOS EXISTENTES (sem alterações) ==========

    public String getId() { return id; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public int getCargaAtual() { return cargaAtual; }
    public int getNumeroDeViagensDiarias() { return numeroDeViagensDiarias; }
    public Zona getZonaAlvo() { return zonaAlvo; }
    public Zona getZonaAtualDaRota() { return rota.getValor(indiceRota); }
    public void avancarParaProximaZona() {
        if (indiceRota < rota.getTamanho() - 1) {
            indiceRota++;
            this.zonaAlvo = rota.getValor(indiceRota);
        }
    }
    public boolean temMaisZonasNaRota() { return indiceRota < rota.getTamanho() - 1; }
    public Lista<Zona> getRota() { return rota; }
    public boolean atualizarProximaZonaAlvo() {
        int tentativas = rota.getTamanho();
        for (int i = 0; i < tentativas; i++) {
            indiceRota = (indiceRota + 1) % rota.getTamanho();
            Zona proximaZona = rota.getValor(indiceRota);
            if (!proximaZona.estaLimpa()) {
                zonaAlvo = proximaZona;
                System.out.println("[CAMINHÃO " + id + "] Redirecionado para zona " + zonaAlvo.getNome());
                return true;
            }
        }
        return false;
    }
    public void atualizarZonaAlvo() {
        if (rota == null || rota.getTamanho() == 0) return;
        indiceRota++;
        if (indiceRota >= rota.getTamanho()) {
            indiceRota = 0;
        }
        zonaAlvo = rota.getValor(indiceRota);
    }
    public void definirZonaAlvo(Zona novaZona) { this.zonaAlvo = novaZona; }
    public boolean coletar(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            return true;
        }
        System.out.println("[CAMINHÃO " + id + "] Carga máxima atingida.");
        return false;
    }
    public void descarregar() { this.cargaAtual = 0; }
    public boolean podeRealizarNovaViagem() { return numeroDeViagensDiarias > 0; }
    public void registrarViagem() {
        if (numeroDeViagensDiarias > 0) {
            numeroDeViagensDiarias--;
        }
    }
    public EventoGerarCaminhaoGrande getEventoAgendado() { return eventoAgendado; }
    public void setEventoAgendado(EventoGerarCaminhaoGrande eventoAgendado) { this.eventoAgendado = eventoAgendado; }
}