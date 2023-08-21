import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
