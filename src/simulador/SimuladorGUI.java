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
import javafx.scene.control.TextField; // Import para o campo de texto
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import simulador.caminhoes.CaminhaoGrande; // Import para poder resetar o contador

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SimuladorGUI extends Application {

    private static final int LARGURA = 800;
    private static final int ALTURA = 850;

    private Button startButton;
    private GraphicsContext gc;
    private Image mapaBackground;
    private TextField daysTextField; // Campo para inserir o número de dias

    private static Map<String, TruckRepresentation> truckRepresentations = new ConcurrentHashMap<>();

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

    private void startSimulation() {
        startButton.setDisable(true);
        truckRepresentations.clear();

        // Reseta o contador de IDs dos caminhões grandes para uma nova simulação
        CaminhaoGrande.resetarContadorDeId();

        int diasParaSimular;
        try {
            // Tenta converter o texto para um número inteiro
            diasParaSimular = Integer.parseInt(daysTextField.getText());
            if (diasParaSimular <= 0) {
                diasParaSimular = 1; // Define um valor mínimo se for negativo ou zero
                daysTextField.setText("1");
            }
        } catch (NumberFormatException e) {
            // Se o usuário digitar algo que não é um número, usa o valor padrão 1
            diasParaSimular = 1;
            daysTextField.setText("1");
        }

        final int finalDias = diasParaSimular; // Variável final para usar na thread

        Thread simulationThread = new Thread(() -> {
            try {
                Simulador simulador = new Simulador();
                // Usa o valor da interface em vez de um número fixo
                simulador.iniciarSimulacao(finalDias);
            } finally {
                Platform.runLater(() -> startButton.setDisable(false));
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    private void updateAndDraw() {
        gc.setFill(Color.web("#2c3e50"));
        gc.fillRect(0, 0, LARGURA, ALTURA);
        if (mapaBackground != null) {
            gc.drawImage(mapaBackground, 0, 0, LARGURA, ALTURA);
        }

        drawLocationMarker("NORTE", 250, 200, Color.web("#e67e22"));
        drawLocationMarker("LESTE", 450, 300, Color.web("#2ecc71"));
        drawLocationMarker("SUDESTE", 550, 450, Color.web("#e91e63"));
        drawLocationMarker("SUL", 300, 550, Color.web("#1abc9c"));
        drawLocationMarker("CENTRO", 250, 375, Color.web("#f1c40f"));
        drawLocationMarker("ESTAÇÃO A", 350, 280, Color.web("#9b59b6"));
        drawLocationMarker("ESTAÇÃO B", 450, 500, Color.web("#9b59b6"));
        drawLocationMarker("ATERRO", 200, 50, Color.web("#7f8c8d"));

        for (TruckRepresentation truck : truckRepresentations.values()) {
            truck.updatePosition();
            truck.draw(gc);
        }
    }

    private void drawLocationMarker(String name, double x, double y, Color color) {
        gc.setFill(color.deriveColor(0, 1, 1, 0.4));
        gc.fillRoundRect(x - 50, y - 25, 100, 50, 10, 10);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("System", FontWeight.BOLD, 14));
        gc.fillText(name, x - (name.length() * 4), y + 5);
    }

    public static void updateTruck(String id, String status, String location) {
        Platform.runLater(() -> {
            TruckRepresentation rep = truckRepresentations.computeIfAbsent(id, TruckRepresentation::new);
            rep.setTarget(status, location);
        });
    }

    public static void pause() {
        try {
            if (velocidadePausaMs > 0) {
                Thread.sleep(velocidadePausaMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private static class TruckRepresentation {
        String id;
        double currentX, currentY;
        double targetX, targetY;
        Color color;
        String status;

        public TruckRepresentation(String id) {
            this.id = id;
            this.currentX = 20;
            this.currentY = 450;
            this.targetX = this.currentX;
            this.targetY = this.currentY;
            this.color = id.toUpperCase().startsWith("C") ? Color.DODGERBLUE : Color.ORCHID;
            this.status = "Aguardando";
        }

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

        public void updatePosition() {
            double dx = targetX - currentX;
            double dy = targetY - currentY;
            if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
                return;
            }
            currentX += dx * 0.05;
            currentY += dy * 0.05;
        }

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