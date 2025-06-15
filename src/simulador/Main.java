package simulador;

import javafx.application.Application;

/**
 * Inicia a interface gráfica do simulador de coleta de lixo.
 */
public class Main {

    /**
     * Ponto de entrada da aplicação, executa a simulação.
     *
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Esta é a forma padrão e correta de iniciar uma classe JavaFX
        // a partir de um método main separado.
        Application.launch(SimuladorGUI.class, args);
    }
}
