import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.Socket;
import java.io.*;

public class ChatClient extends Application {
    private User currentUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        currentUser = new User();

        VBox root = new VBox(10);
        TextField ipField = new TextField("127.0.0.1");
        TextField portField = new TextField("8000"); // puerto por defecto
        TextArea chatArea = new TextArea();
        TextField inputField = new TextField();
        Button sendButton = new Button("Enviar");

        sendButton.setOnAction(event -> {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            String message = inputField.getText();

            if (!message.isEmpty()) {
                sendMessage(ip, port, currentUser.getUsername() + ": " + message);
                chatArea.appendText("Yo: " + message + "\n");
                inputField.clear();
            }
        });

        root.getChildren().addAll(ipField, portField, chatArea, inputField, sendButton);
        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat - " + currentUser.getUsername());
        primaryStage.show();
    }

    private void sendMessage(String ip, int port, String message) {
        try {
            Socket socket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class User {
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
