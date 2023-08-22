import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
    ChatServer server = new ChatServer();
        launch(args);



    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        Button runServerButton = new Button("Ejecutar Servidor");

runServerButton.setOnAction(event -> {
    new Thread(() -> {
        ChatServer server = new ChatServer();
        server.runServer();
    }).start();
    runServerButton.setDisable(true);  // Desactivar el botón después de iniciar el servidor
});

Button createUserButton = new Button("Crear Usuario");

        createUserButton.setOnAction(event -> {
            ChatClient client = new ChatClient();
            client.showWindow();
        });

        root.getChildren().addAll(runServerButton, createUserButton);
        Scene scene = new Scene(root, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Crear Usuario Chat");
        primaryStage.show();
    }
}

