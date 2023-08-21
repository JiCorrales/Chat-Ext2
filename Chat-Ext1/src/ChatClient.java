import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static int userCount = 0;
    private User currentUser;
    private Stage chatStage;
    private TextArea chatArea;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient() {
        userCount++;
        currentUser = new User("User-" + String.format("%03d", userCount));

        try {
            socket = new Socket("127.0.0.1", 4444); // Conexión al servidor.
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Inicia un hilo para escuchar mensajes del servidor.
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        chatArea.appendText(receivedMessage + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }

        chatStage = new Stage();
        VBox root = new VBox(10);
        chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField inputField = new TextField();
        Button sendButton = new Button("Enviar");
        sendButton.setOnAction(event -> {
            String message = inputField.getText();
            if ("exit".equalsIgnoreCase(message)) {
                out.println(currentUser.getUsername() + " ha salido del chat.");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chatStage.close();
            } else {
                out.println(currentUser.getUsername() + ": " + message);
                inputField.clear();
            }
        });

        Button exitButton = new Button("Salir");
        exitButton.setOnAction(event -> {
            out.println(currentUser.getUsername() + " ha salido del chat.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            chatStage.close();
        });

        root.getChildren().addAll(chatArea, inputField, sendButton, exitButton);

        Scene scene = new Scene(root, 400, 500);
        chatStage.setScene(scene);
        chatStage.setTitle(currentUser.getUsername());
    }

    public void showWindow() {
        chatStage.show();
    }

    public String getCurrentUsername() {
        return currentUser.getUsername();
    }

    private static class User {
        private String username;

        public User(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }
}
