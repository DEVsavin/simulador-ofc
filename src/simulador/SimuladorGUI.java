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

    private static final int LARGURA = 800;
    private static final int ALTURA = 850;

    private Button startButton;
    private GraphicsContext gc;
    private Image mapaBackground;
    private TextField daysTextField;

    /**
     * Mapa que armazena as representações visuais dos caminhões, usando o ID do caminhão como chave.
     */
    private static Map<String, RepresentacaoCaminhao> representacoesCaminhoes = new ConcurrentHashMap<>();
    private static volatile int velocidadePausaMs = 250;

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
        startButton.setOnAction(e -> iniciarSimulacao());

        Label daysLabel = new Label("Número de Dias para Simular:");
        daysTextField = new TextField("3");
        daysTextField.setMaxWidth(60);

        Label speedLabel = new Label("Velocidade da Simulação (Lento <-> Rápido)");
        Slider speedSlider = new Slider(0, 1000, velocidadePausaMs);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(250);

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            velocidadePausaMs = 1000 - newVal.intValue();
        });

        VBox controls = new VBox(10, daysLabel, daysTextField, startButton, speedLabel, speedSlider);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);
        root.setBottom(controls);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                atualizarEDesenhar();
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
     */
    private void iniciarSimulacao() {
        startButton.setDisable(true);
        representacoesCaminhoes.clear();

        CaminhaoGrande.resetarContadorDeId();

        int diasParaSimular;
        try {
            diasParaSimular = Integer.parseInt(daysTextField.getText());
            if (diasParaSimular <= 0) {
                diasParaSimular = 1;
                daysTextField.setText("1");
            }
        } catch (NumberFormatException e) {
            diasParaSimular = 1;
            daysTextField.setText("1");
        }

        final int finalDias = diasParaSimular;

        Thread simulationThread = new Thread(() -> {
            try {
                Simulador simulador = new Simulador();
                simulador.iniciarSimulacao(finalDias);
            } finally {
                Platform.runLater(() -> startButton.setDisable(false));
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    /**
     * O loop principal de renderização, chamado a cada frame pelo AnimationTimer.
     */
    private void atualizarEDesenhar() {
        gc.setFill(Color.web("#2c3e50"));
        gc.fillRect(0, 0, LARGURA, ALTURA);
        if (mapaBackground != null) {
            gc.drawImage(mapaBackground, 0, 0, LARGURA, ALTURA);
        }

        desenharMarcadorLocal("NORTE", 250, 200, Color.web("#e67e22"));
        desenharMarcadorLocal("LESTE", 450, 300, Color.web("#2ecc71"));
        desenharMarcadorLocal("SUDESTE", 550, 450, Color.web("#e91e63"));
        desenharMarcadorLocal("SUL", 300, 550, Color.web("#1abc9c"));
        desenharMarcadorLocal("CENTRO", 250, 375, Color.web("#f1c40f"));
        desenharMarcadorLocal("ESTAÇÃO A", 350, 280, Color.web("#9b59b6"));
        desenharMarcadorLocal("ESTAÇÃO B", 450, 500, Color.web("#9b59b6"));
        desenharMarcadorLocal("ATERRO", 200, 50, Color.web("#7f8c8d"));

        for (RepresentacaoCaminhao caminhao : representacoesCaminhoes.values()) {
            caminhao.atualizarPosicao();
            caminhao.desenhar(gc);
        }
    }

    /**
     * Desenha um marcador de localidade (zona, estação, etc.) no mapa.
     */
    private void desenharMarcadorLocal(String nome, double x, double y, Color cor) {
        gc.setFill(cor.deriveColor(0, 1, 1, 0.4));
        gc.fillRoundRect(x - 50, y - 25, 100, 50, 10, 10);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("System", FontWeight.BOLD, 14));
        gc.fillText(nome, x - (nome.length() * 4), y + 5);
    }

    /**
     * Método estático para atualizar o estado de um caminhão na GUI.
     * ATENÇÃO: Se renomear este método, atualize as chamadas em outras classes.
     */
    public static void atualizarCaminhao(String id, String status, String localizacao) {
        Platform.runLater(() -> {
            RepresentacaoCaminhao rep = representacoesCaminhoes.computeIfAbsent(id, RepresentacaoCaminhao::new);
            rep.definirAlvo(status, localizacao);
        });
    }

    /**
     * Pausa a execução da thread da simulação.
     * ATENÇÃO: Se renomear este método, atualize as chamadas em outras classes.
     */
    public static void pausar() {
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
     */
    private static class RepresentacaoCaminhao {
        String id;
        double xAtual, yAtual;
        double xAlvo, yAlvo;
        Color cor;
        String estado;

        /**
         * Constrói a representação visual de um caminhão.
         *
         * @param id O ID único do caminhão.
         */
        public RepresentacaoCaminhao(String id) {
            this.id = id;
            this.xAtual = 20;
            this.yAtual = 450;
            this.xAlvo = this.xAtual;
            this.yAlvo = this.yAtual;
            this.cor = id.toUpperCase().startsWith("C") ? Color.DODGERBLUE : Color.ORCHID;
            this.estado = "Aguardando";
        }

        /**
         * Define o novo destino e estado do caminhão.
         *
         * @param estado O novo estado a ser exibido.
         * @param localizacao O nome do local de destino para calcular as coordenadas.
         */
        public void definirAlvo(String estado, String localizacao) {
            this.estado = estado;
            switch (localizacao.toLowerCase()) {
                case "norte": this.xAlvo = 250; this.yAlvo = 200; break;
                case "leste": this.xAlvo = 450; this.yAlvo = 300; break;
                case "sudeste": this.xAlvo = 550; this.yAlvo = 450; break;
                case "sul": this.xAlvo = 300; this.yAlvo = 550; break;
                case "estacao a": this.xAlvo = 350; this.yAlvo = 280; break;
                case "estacao b": this.xAlvo = 450; this.yAlvo = 500; break;
                case "centro": this.xAlvo = 250; this.yAlvo = 375; break;
                case "aterro": this.xAlvo = 200; this.yAlvo = 50; break;
                default: break;
            }
        }

        /**
         * Atualiza a posição atual do caminhão, movendo-o em direção ao alvo.
         */
        public void atualizarPosicao() {
            double dx = xAlvo - xAtual;
            double dy = yAlvo - yAtual;
            if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
                return;
            }
            xAtual += dx * 0.05;
            yAtual += dy * 0.05;
        }

        /**
         * Desenha o caminhão no canvas.
         *
         * @param gc O GraphicsContext no qual desenhar.
         */
        public void desenhar(GraphicsContext gc) {
            gc.setFill(this.cor);
            gc.fillOval(xAtual - 10, yAtual - 10, 20, 20);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(xAtual - 10, yAtual - 10, 20, 20);

            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("System", FontWeight.BOLD, 12));
            gc.fillText(id, xAtual - 8, yAtual + 5);
            gc.setFont(Font.font("System", FontWeight.NORMAL, 11));
            gc.setFill(Color.WHITE);
            gc.fillText(estado, xAtual - 25, yAtual + 25);
        }
    }
}