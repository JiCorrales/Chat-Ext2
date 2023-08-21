import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 4444;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        new ChatServer().runServer();
    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("El servidor está en marcha...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Se utiliza para reenviar un mensaje a todos los clientes.
    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Elimina un cliente de la lista.
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}
