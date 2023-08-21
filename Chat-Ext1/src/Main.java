import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        Button createUserButton = new Button("Crear Usuario");

        createUserButton.setOnAction(event -> {
            ChatClient client = new ChatClient();
            client.showWindow();
        });

        root.getChildren().addAll(createUserButton);
        Scene scene = new Scene(root, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Crear Usuario Chat");
        primaryStage.show();
    }
}

