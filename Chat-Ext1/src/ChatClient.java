import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatClient {
    private User currentUser;
    private Stage chatStage;

    public ChatClient() {
        this.currentUser = new User();
        this.chatStage = new Stage();

        VBox root = new VBox(10);
        TextArea chatArea = new TextArea();
        TextField inputField = new TextField();
        Button sendButton = new Button("Enviar");

        sendButton.setOnAction(event -> {
            // Aquí puedes agregar la lógica para enviar el mensaje a otros usuarios.
            String message = inputField.getText();
            chatArea.appendText(currentUser.getUsername() + ": " + message + "\n");
            inputField.clear();
        });

        root.getChildren().addAll(chatArea, inputField, sendButton);
        Scene scene = new Scene(root, 300, 400);
        chatStage.setScene(scene);
        chatStage.setTitle("Chat - " + currentUser.getUsername());
    }

    public void showWindow() {
        chatStage.show();
    }

    private static class User {
        private static int userCount = 0;
        private String username;

        public User() {
            userCount++;
            this.username = String.format("User-%03d", userCount);
        }

        public String getUsername() {
            return username;
        }
    }
}
