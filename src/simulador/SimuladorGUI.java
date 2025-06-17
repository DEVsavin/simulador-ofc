package simulador;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import simulador.caminhoes.CaminhaoGrande;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerencia a interface gráfica do usuário (GUI) para o simulador de coleta de lixo.
 * Esta classe utiliza JavaFX para desenhar o mapa da cidade, zonas, estações e
 * a movimentação dos caminhões em tempo real.
 */
public class SimuladorGUI extends Application {

    /** Largura da janela da aplicação em pixels. */
    private static final int LARGURA = 800;
    /** Altura da janela da aplicação em pixels. */
    private static final int ALTURA = 850;

    /** Botão para iniciar a simulação. */
    private Button startButton;
    /** Contexto gráfico do canvas, usado para desenhar formas e imagens. */
    private GraphicsContext gc;
    /** Imagem de fundo do mapa da cidade. */
    private Image mapaBackground;
    /** Campo de texto para o usuário inserir o número de dias a simular. */
    private TextField daysTextField;

    /**
     * Mapa que armazena as representações visuais dos caminhões, usando o ID do caminhão como chave.
     * É um ConcurrentHashMap para permitir atualizações seguras a partir da thread da simulação.
     */
    private static Map<String, TruckRepresentation> truckRepresentations = new ConcurrentHashMap<>();

    /**
     * Controla a velocidade da animação. É o tempo (em milissegundos) que a thread da simulação pausa.
     * É volátil para garantir que as alterações feitas pela thread da UI sejam visíveis para a thread da simulação.
     */
    private static volatile int velocidadePausaMs = 250;

    /**
     * Ponto de entrada principal para a aplicação JavaFX.
     * Inicializa o palco (Stage), a cena (Scene) e todos os componentes da UI.
     *
     * @param primaryStage O palco principal desta aplicação.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fluxo de Coleta de Lixo - Teresina (Dinâmico)");

        try {
            mapaBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/mapa-teresina.png")));
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem do mapa! Verifique se está em src/resources/");
            mapaBackground = null;
        }

        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(LARGURA, ALTURA);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        startButton = new Button("Iniciar Simulação");
        startButton.setOnAction(e -> startSimulation());

        // Controles da UI
        Label daysLabel = new Label("Número de Dias para Simular:");
        daysTextField = new TextField("3"); // Valor padrão de 3 dias
        daysTextField.setMaxWidth(60);

        Label speedLabel = new Label("Velocidade da Simulação (Lento <-> Rápido)");
        Slider speedSlider = new Slider(0, 1000, velocidadePausaMs);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(250);

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            velocidadePausaMs = 1000 - newVal.intValue();
        });

        // Adiciona todos os controles ao VBox
        VBox controls = new VBox(10, daysLabel, daysTextField, startButton, speedLabel, speedSlider);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);
        root.setBottom(controls);

        // Inicia o loop de animação que chama updateAndDraw() a cada frame.
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateAndDraw();
            }
        }.start();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Inicia a lógica da simulação quando o botão "Iniciar Simulação" é clicado.
     * Desabilita o botão, lê os parâmetros da UI e inicia o simulador em uma nova thread
     * para não bloquear a interface gráfica.
     */
    private void startSimulation() {
        startButton.setDisable(true);
        truckRepresentations.clear();

        // Reseta o contador de IDs dos caminhões grandes para uma nova simulação
        CaminhaoGrande.resetarContadorDeId();

        int diasParaSimular;
        try {
            diasParaSimular = Integer.parseInt(daysTextField.getText());
            if (diasParaSimular <= 0) {
                diasParaSimular = 1; // Garante um valor mínimo
                daysTextField.setText("1");
            }
        } catch (NumberFormatException e) {
            diasParaSimular = 1; // Valor padrão em caso de entrada inválida
            daysTextField.setText("1");
        }

        final int finalDias = diasParaSimular;

        // A simulação roda em uma thread separada para não travar a UI.
        Thread simulationThread = new Thread(() -> {
            try {
                Simulador simulador = new Simulador();
                simulador.iniciarSimulacao(finalDias);
            } finally {
                // Reabilita o botão na thread da UI quando a simulação termina.
                Platform.runLater(() -> startButton.setDisable(false));
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    /**
     * O loop principal de renderização, chamado a cada frame pelo AnimationTimer.
     * Limpa o canvas e redesenha o mapa, os marcadores de localização e todos os caminhões.
     */
    private void updateAndDraw() {
        gc.setFill(Color.web("#2c3e50"));
        gc.fillRect(0, 0, LARGURA, ALTURA);
        if (mapaBackground != null) {
            gc.drawImage(mapaBackground, 0, 0, LARGURA, ALTURA);
        }

        // Desenha os marcadores fixos das zonas e estações
        drawLocationMarker("NORTE", 250, 200, Color.web("#e67e22"));
        drawLocationMarker("LESTE", 450, 300, Color.web("#2ecc71"));
        drawLocationMarker("SUDESTE", 550, 450, Color.web("#e91e63"));
        drawLocationMarker("SUL", 300, 550, Color.web("#1abc9c"));
        drawLocationMarker("CENTRO", 250, 375, Color.web("#f1c40f"));
        drawLocationMarker("ESTAÇÃO A", 350, 280, Color.web("#9b59b6"));
        drawLocationMarker("ESTAÇÃO B", 450, 500, Color.web("#9b59b6"));
        drawLocationMarker("ATERRO", 200, 50, Color.web("#7f8c8d"));

        // Atualiza a posição e desenha cada caminhão
        for (TruckRepresentation truck : truckRepresentations.values()) {
            truck.updatePosition();
            truck.draw(gc);
        }
    }

    /**
     * Desenha um marcador de localidade (zona, estação, etc.) no mapa.
     *
     * @param name  O nome do local a ser exibido.
     * @param x     A coordenada X central do marcador.
     * @param y     A coordenada Y central do marcador.
     * @param color A cor do marcador.
     */
    private void drawLocationMarker(String name, double x, double y, Color color) {
        gc.setFill(color.deriveColor(0, 1, 1, 0.4));
        gc.fillRoundRect(x - 50, y - 25, 100, 50, 10, 10);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("System", FontWeight.BOLD, 14));
        gc.fillText(name, x - (name.length() * 4), y + 5);
    }

    /**
     * Metodo estático e thread-safe para atualizar o estado de um caminhão na GUI.
     * Outras partes do código (rodando em outra thread) chamam este metodo para
     * atualizar a posição e o status de um caminhão.
     *
     * @param id       O ID do caminhão (ex: "C1", "G2").
     * @param status   O novo status a ser exibido (ex: "Coletando", "Na Fila").
     * @param location A nova localização de destino do caminhão.
     */
    public static void updateTruck(String id, String status, String location) {
        Platform.runLater(() -> {
            TruckRepresentation rep = truckRepresentations.computeIfAbsent(id, TruckRepresentation::new);
            rep.setTarget(status, location);
        });
    }

    /**
     * Pausa a execução da thread da simulação para controlar a velocidade da animação.
     * A duração da pausa é definida pelo slider na UI.
     */
    public static void pause() {
        try {
            if (velocidadePausaMs > 0) {
                Thread.sleep(velocidadePausaMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Classe interna que representa um único caminhão na interface gráfica.
     * Contém sua posição, destino, cor e status para ser desenhado no canvas.
     */
    private static class TruckRepresentation {
        String id;
        double currentX, currentY;
        double targetX, targetY;
        Color color;
        String status;

        /**
         * Constrói a representação visual de um caminhão.
         *
         * @param id O ID único do caminhão.
         */
        public TruckRepresentation(String id) {
            this.id = id;
            this.currentX = 20; // Posição inicial
            this.currentY = 450;
            this.targetX = this.currentX;
            this.targetY = this.currentY;
            // Caminhões pequenos (C) são azuis, caminhões grandes (G) são roxos.
            this.color = id.toUpperCase().startsWith("C") ? Color.DODGERBLUE : Color.ORCHID;
            this.status = "Aguardando";
        }

        /**
         * Define o novo destino e status do caminhão.
         *
         * @param status   O novo status a ser exibido.
         * @param location O nome do local de destino, que é convertido para coordenadas X e Y.
         */
        public void setTarget(String status, String location) {
            this.status = status;
            switch (location.toLowerCase()) {
                case "norte": this.targetX = 250; this.targetY = 200; break;
                case "leste": this.targetX = 450; this.targetY = 300; break;
                case "sudeste": this.targetX = 550; this.targetY = 450; break;
                case "sul": this.targetX = 300; this.targetY = 550; break;
                case "estacao a": this.targetX = 350; this.targetY = 280; break;
                case "estacao b": this.targetX = 450; this.targetY = 500; break;
                case "centro": this.targetX = 250; this.targetY = 375; break;
                case "aterro": this.targetX = 200; this.targetY = 50; break;
                default: break;
            }
        }

        /**
         * Atualiza a posição atual do caminhão, movendo-o um pouco em direção ao seu destino.
         * Isso cria o efeito de animação suave.
         */
        public void updatePosition() {
            double dx = targetX - currentX;
            double dy = targetY - currentY;
            // Se já está perto do alvo, para de mover.
            if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
                return;
            }
            // Move uma fração da distância a cada frame.
            currentX += dx * 0.05;
            currentY += dy * 0.05;
        }

        /**
         * Desenha o caminhão (círculo, ID e status) no canvas.
         *
         * @param gc O GraphicsContext no qual desenhar.
         */
        public void draw(GraphicsContext gc) {
            gc.setFill(this.color);
            gc.fillOval(currentX - 10, currentY - 10, 20, 20);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(currentX - 10, currentY - 10, 20, 20);

            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("System", FontWeight.BOLD, 12));
            gc.fillText(id, currentX - 8, currentY + 5);
            gc.setFont(Font.font("System", FontWeight.NORMAL, 11));
            gc.setFill(Color.WHITE);
            gc.fillText(status, currentX - 25, currentY + 25);
        }
    }
}