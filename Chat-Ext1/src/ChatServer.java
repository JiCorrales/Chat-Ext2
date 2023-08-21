import java.net.*;
import java.io.*;

public class ChatServer {
    private ServerSocket serverSocket;
    private int port;

    public ChatServer() {
        this.port = 8000; // puedes empezar desde este puerto y verificar hacia arriba
        while (true) {
            try {
                this.serverSocket = new ServerSocket(port);
                System.out.println("Servidor iniciado en el puerto: " + port);
                break;
            } catch (IOException e) {
                port++;
            }
        }
    }

    public int getPort() {
        return this.port;
    }

    public void startServer() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message = in.readLine();
                    System.out.println("Mensaje recibido: " + message);
                    // Aquí puedes agregar código para manejar los mensajes recibidos.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.startServer();
    }
}
