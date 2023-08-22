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
import java.net.SocketException;

/**
 * La clase ChatClient representa la implementación del lado del cliente de la aplicación de chat.
 * Se conecta al servidor y maneja la comunicación con otros usuarios a través del servidor.
 */
public class ChatClient {

    // Atributos privados para manejar la conexión y la interfaz de usuario
    private static int userCount = 0;
    private User currentUser;
    private Stage chatStage;
    private TextArea chatArea;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor de la clase ChatClient. Inicializa y configura la conexión con el servidor,
     * así como los componentes de la interfaz de usuario del cliente.
     */
    public ChatClient() {

        userCount++;//incrementa la cantidad de usuarios
        currentUser = new User("User-" + String.format("%03d", userCount));

        try {
            // Crea una conexión al servidor en la dirección y puerto especificados
            socket = new Socket("127.0.0.1", 6666);

            // Configura el flujo de salida para enviar mensajes al servidor
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Inicia un hilo para escuchar mensajes del servidor.
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        // Agrega el mensaje al área de chat en la interfaz de usuario
                        chatArea.appendText(receivedMessage + "\n");
                    }
                } catch (SocketException se) {
                    System.out.println("Conexión cerrada.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
        // Configura la interfaz de usuario del cliente
        chatStage = new Stage();
        VBox root = new VBox(10);
        chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField inputField = new TextField();
        Button sendButton = new Button("Enviar");
        sendButton.setOnAction(event -> {
            String message = inputField.getText();
            if ("exit".equalsIgnoreCase(message)) {
                // Maneja la salida del chat
                out.println(currentUser.getUsername() + " ha salido del chat.");
                try {
                    socket.close();
                } catch (SocketException se) {
                    System.out.println("Conexión cerrada.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chatStage.close();
            } else {
                // Envía el mensaje al servidor y agrega el mensaje al área de chat
                out.println(currentUser.getUsername() + ": " + message);
                inputField.clear();
            }
        });

        // Configura botón para salir del chat
        Button exitButton = new Button("Salir");
        exitButton.setOnAction(event -> {
            // Maneja la salida del chat
            out.println(currentUser.getUsername() + " ha salido del chat.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            chatStage.close();
        });
        // Agrega componentes a la interfaz de usuario
        root.getChildren().addAll(chatArea, inputField, sendButton, exitButton);

        Scene scene = new Scene(root, 400, 500);
        chatStage.setScene(scene);
        chatStage.setTitle(currentUser.getUsername());
    }

    /**
     * Muestra la ventana de la interfaz de usuario del cliente de chat.
     */
    public void showWindow() {
        // implementación del método showWindow
        chatStage.show();
    }

    /**
     * Obtiene el nombre de usuario actual del cliente.
     *
     * @return El nombre de usuario actual del cliente.
     */
    public String getCurrentUsername() {
        // retorna el nombre de usuario
        return currentUser.getUsername();
    }

    /**
     * Clase interna que representa un usuario en el chat.
     */
    private static class User {
        // implementación de la clase interna User
        private String username;

        public User(String username) {
            // Establece el nombre dado como parametro como username
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }
}
