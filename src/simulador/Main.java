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
        Application.launch(SimuladorGUI.class, args);
    }
}
