import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


/**
 * Clase que representa un manejador de cliente en el servidor de chat.
 * Administra la comunicación con un cliente específico y reenvía mensajes a otros clientes.
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private PrintWriter out;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Cliente conectado: " + socket.getInetAddress());
        server.addToHistory("Cliente conectado: " + socket.getInetAddress());

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + message);
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Cliente desconectado: " + socket.getInetAddress());
                    server.removeClient(this);
                    socket.close();
                    break;
                }
                server.broadcastMessage(message);
    server.addToHistory(message);
            }
        } catch (SocketException se) {
    server.addToHistory(socket.getInetAddress() + " ha salido del chat.");
    System.out.println("Conexión cerrada.");
    server.removeClient(this);
    try {
        socket.close();
    } catch (IOException ioException) {
        ioException.printStackTrace();
    }
} catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
