package simulador.caminhoes;

import estruturas.lista.Lista;
import simulador.EstatisticasDia;
import simulador.SimuladorGUI;
import simulador.configTempo.GerenciadorTempo;
import simulador.configTempo.TempoDetalhado;
import simulador.eventos.AgendaEventos;
import simulador.eventos.EventoColeta;
import simulador.eventos.EventoGerarCaminhaoGrande;
import simulador.eventos.EventoIniciarTransferencia;
import simulador.zona.GerenciadorZonas;
import simulador.zona.Zona;

/**
 * Representa um caminhão de coleta de pequeno porte, a principal entidade
 * responsável por coletar lixo nas zonas da cidade e transportá-lo para as
 * estações de transferência. A lógica central de sua operação está no
 * método {@code realizarColeta}.
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

    /**
     * Constrói um novo caminhão pequeno com seus parâmetros operacionais.
     *
     * @param id                     O identificador único do caminhão (ex: "C1").
     * @param capacidadeMaxima       A capacidade máxima de lixo (toneladas).
     * @param numeroDeViagensDiarias O número máximo de viagens de coleta por dia.
     * @param rota                   A lista de zonas que compõe a rota do caminhão.
     */
    public CaminhaoPequeno(String id, int capacidadeMaxima, int numeroDeViagensDiarias, Lista<Zona> rota) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.numeroDeViagensDiarias = numeroDeViagensDiarias;
        this.cargaAtual = 0;
        this.rota = rota;
        this.indiceRota = 0;
        this.zonaAlvo = rota.getValor(0);
    }

    /**
     * Executa a lógica completa de uma tentativa de coleta em uma zona.
     * <p>
     * Este método trata os diferentes cenários:
     * <ul>
     * <li><b>Zona Limpa:</b> Se a zona não tem lixo, a viagem é registrada e o caminhão busca uma nova zona ou vai para a estação.</li>
     * <li><b>Zona com Lixo:</b> O caminhão coleta o lixo até encher sua capacidade ou esvaziar a zona.</li>
     * </ul>
     * Ao final, agenda o próximo passo do caminhão, seja uma nova coleta ou uma ida à estação.
     *
     * @param tempoAtual       O tempo atual da simulação.
     * @param zona             A zona onde a coleta está sendo tentada.
     * @param gerenciadorZonas O gerenciador de zonas, para agendamento de eventos futuros.
     * @param estatisticas     O objeto de estatísticas do dia, para registrar a coleta.
     */
    public void realizarColeta(int tempoAtual, Zona zona, GerenciadorZonas gerenciadorZonas, EstatisticasDia estatisticas) {
        SimuladorGUI.atualizarCaminhao(this.id, "Coletando", zona.getNome());
        SimuladorGUI.pausar();

        if (zona.getLixoAcumulado() == 0) {
            System.out.println("  • Zona " + zona.getNome() + " está limpa. Nenhuma coleta realizada pelo caminhão " + this.id);
            this.registrarViagem();

            if (this.podeRealizarNovaViagem()) {
                boolean mudouZona = this.atualizarProximaZonaAlvo();
                if (mudouZona) {
                    AgendaEventos.adicionarEvento(new EventoColeta(tempoAtual + 30, this, this.getZonaAlvo(), gerenciadorZonas));
                } else {
                    System.out.println("  • Todas as zonas da rota do caminhão " + this.id + " estão limpas.");
                    AgendaEventos.adicionarEvento(new EventoIniciarTransferencia(tempoAtual, this, zona, gerenciadorZonas));
                }
            } else {
                AgendaEventos.adicionarEvento(new EventoIniciarTransferencia(tempoAtual, this, zona, gerenciadorZonas));
            }
            return;
        }

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

        if (this.podeRealizarNovaViagem() && coletou) {
            TempoDetalhado tempoDetalhado = GerenciadorTempo.calcularTempoDetalhado(tempoAtual, totalColetadoNaRodada, false);
            estatisticas.registrarColeta(totalColetadoNaRodada, tempoDetalhado.tempoTotal);

            System.out.printf("| %-18s | %-28s |%n", "Tempo de Coleta", GerenciadorTempo.formatarDuracao(tempoDetalhado.tempoColeta));
            System.out.printf("| %-18s | %-28s |%n", "Tempo Total", GerenciadorTempo.formatarDuracao(tempoDetalhado.tempoTotal));
            System.out.printf("| %-18s | %-28s |%n", "Horário Final", GerenciadorTempo.formatarHorarioSimulado(tempoAtual + tempoDetalhado.tempoTotal));
            System.out.println("+--------------------------------------------------+");
            System.out.println();

            AgendaEventos.adicionarEvento(new EventoColeta(tempoAtual + tempoDetalhado.tempoTotal, this, zona, gerenciadorZonas));
        } else if (this.cargaAtual > 0) {
            this.registrarViagem();
            AgendaEventos.adicionarEvento(new EventoIniciarTransferencia(tempoAtual, this, zona, gerenciadorZonas));
        }
    }

    /**
     * Retorna o ID do caminhão.
     * @return O ID do caminhão.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna a capacidade máxima de carga do caminhão.
     * @return A capacidade máxima em toneladas.
     */
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Retorna a carga atual do caminhão.
     * @return A carga atual em toneladas.
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * Retorna a zona alvo atual para coleta.
     * @return A {@link Zona} de destino.
     */
    public Zona getZonaAlvo() {
        return zonaAlvo;
    }

    /**
     * Procura a próxima zona não limpa na rota do caminhão, de forma cíclica.
     * @return {@code true} se uma nova zona alvo com lixo foi encontrada, {@code false} caso contrário.
     */
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

    /**
     * Avança para a próxima zona na rota, seguindo a ordem da lista.
     * Usado após descarregar na estação para definir o próximo destino de coleta.
     */
    public void atualizarZonaAlvo() {
        if (rota == null || rota.getTamanho() == 0) return;
        indiceRota++;
        if (indiceRota >= rota.getTamanho()) {
            indiceRota = 0;
        }
        zonaAlvo = rota.getValor(indiceRota);
    }

    /**
     * Adiciona uma quantidade de lixo à carga atual do caminhão.
     * @param quantidade A quantidade de lixo a ser adicionada.
     * @return {@code true} se o lixo coube, {@code false} se a capacidade máxima foi excedida.
     */
    public boolean coletar(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            return true;
        }
        System.out.println("[CAMINHÃO " + id + "] Carga máxima atingida.");
        return false;
    }

    /**
     * Zera a carga atual do caminhão, simulando o descarregamento na estação.
     */
    public void descarregar() {
        this.cargaAtual = 0;
    }

    /**
     * Verifica se o caminhão ainda tem viagens de coleta disponíveis para o dia.
     * @return {@code true} se o número de viagens diárias for maior que zero.
     */
    public boolean podeRealizarNovaViagem() {
        return numeroDeViagensDiarias > 0;
    }

    /**
     * Decrementa o contador de viagens diárias restantes do caminhão.
     */
    public void registrarViagem() {
        if (numeroDeViagensDiarias > 0) {
            numeroDeViagensDiarias--;
        }
    }

    /**
     * Retorna o evento de geração de caminhão grande que pode estar agendado para este caminhão.
     * @return O evento {@link EventoGerarCaminhaoGrande} agendado, ou null.
     */
    public EventoGerarCaminhaoGrande getEventoAgendado() {
        return eventoAgendado;
    }

    /**
     * Associa um evento de geração de caminhão grande a este caminhão.
     * @param eventoAgendado O evento a ser associado.
     */
    public void setEventoAgendado(EventoGerarCaminhaoGrande eventoAgendado) {
        this.eventoAgendado = eventoAgendado;
    }
}