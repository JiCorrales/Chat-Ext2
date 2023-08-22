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

    /**
     * Socket que representa la conexión con el cliente.
     */
    private Socket socket;

    /**
     * Referencia al servidor de chat al que pertenece este manejador de cliente.
     */
    private ChatServer server;

    /**
     * Flujo de salida utilizado para enviar mensajes al cliente.
     */
    private PrintWriter out;

    /**
     * Constructor de la clase ClientHandler. Inicializa la conexión con el cliente y configura el servidor asociado.
     *
     * @param socket El socket que representa la conexión con el cliente.
     * @param server La referencia al servidor de chat al que pertenece este manejador de cliente.
     */
    public ClientHandler(Socket socket, ChatServer server) {
        // implementación del constructor
        this.socket = socket;
        this.server = server;
    }

    /**
     * Método que ejecuta el hilo de manejo de cliente. Lee los mensajes del cliente y los reenvía a otros clientes.
     */
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Notifica al servidor que un cliente se ha conectado
            server.addToHistory("Cliente conectado: " + socket.getInetAddress());

            String message;
            while ((message = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message)) {
                    // Maneja la desconexión de un cliente
                    server.addToHistory(socket.getInetAddress() + " ha salido del chat.");
                    System.out.println("Cliente desconectado: " + socket.getInetAddress());
                    server.removeClient(this);
                    socket.close();
                    break;
                }

                // Reenvía el mensaje a todos los clientes conectados
                server.broadcastMessage(message);

                // Agrega el mensaje al historial del servidor
                server.addToHistory(message);
            }
        } catch (SocketException se) {
            // Maneja una desconexión inesperada del cliente
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

    /**
     * Envía un mensaje al cliente asociado a este manejador.
     *
     * @param message El mensaje a enviar al cliente.
     */
    public void sendMessage(String message) {
        if (out != null) {
            // Envía el mensaje al cliente a través del flujo de salida si no es null
            out.println(message);
        }
    }

}
