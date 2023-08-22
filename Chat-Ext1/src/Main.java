import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase principal que contiene el método main para iniciar la aplicación de chat.
 * Permite ejecutar el servidor de chat y crear nuevos clientes de chat.
 */
public class Main extends Application {

    /**
     * Método principal para iniciar la aplicación.
     *
     * @param args Los argumentos de línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        // Crea una instancia del servidor de chat
        ChatServer server = new ChatServer();

        // Inicia la aplicación JavaFX
        launch(args);
    }

    /**
     * Método que inicia la interfaz de usuario JavaFX.
     *
     * @param primaryStage El escenario principal de la interfaz de usuario.
     */
    @Override
    public void start(Stage primaryStage) {
        // Configura la interfaz de usuario de inicio con botones para ejecutar el servidor y crear clientes
        VBox root = new VBox(10);
        Button runServerButton = new Button("Ejecutar Servidor");
        Button createUserButton = new Button("Crear Usuario");

        // Configura el botón para ejecutar el servidor en un hilo separado
        runServerButton.setOnAction(event -> {
            new Thread(() -> {
                ChatServer server = new ChatServer();
                server.runServer();
            }).start();
            runServerButton.setDisable(true);  // Desactiva el botón después de iniciar el servidor
        });

        // Configura el botón para crear un nuevo cliente de chat
        createUserButton.setOnAction(event -> {
            ChatClient client = new ChatClient();
            client.showWindow();
        });

        // Agrega botones a la interfaz de usuario
        root.getChildren().addAll(runServerButton, createUserButton);
        Scene scene = new Scene(root, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Crear Usuario Chat");
        primaryStage.show();
    }
}
