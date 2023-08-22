    import javax.swing.JFrame;
    import javax.swing.JTextArea;
    import javax.swing.JScrollPane;
    import java.awt.BorderLayout;
    import java.io.IOException;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.util.ArrayList;
    import java.util.List;
    /**
     * Clase que representa el servidor de chat para la aplicación de chat.
     * Gestiona las conexiones de los clientes y la comunicación entre ellos.
     */
    public class ChatServer {
        // código de la clase

        /**
         * Puerto en el que el servidor de chat escucha las conexiones entrantes de los clientes.
         */
        private static final int PORT = 6666;

        /**
         * Lista de manejadores de clientes conectados al servidor.
         */
        private static List<ClientHandler> clients = new ArrayList<>();

        /**
         * Método principal que inicia el servidor de chat y comienza a aceptar conexiones de clientes.
         */
        public static void main(String[] args) { new ChatServer().runServer();

        }

        /**
         * Inicia y ejecuta el servidor de chat. Escucha las conexiones entrantes de los clientes
         * y crea hilos separados para manejar a cada cliente conectado.
         */
        public void runServer() {
            // implementación del método runServer
        showHistoryWindow();
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

        /**
         * Reenvía un mensaje a todos los clientes conectados al servidor.
         *
         * @param message El mensaje a reenviar.
         */
        public void broadcastMessage(String message) {
            // implementación del método broadcastMessage
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }

        /**
         * Elimina un cliente de la lista de clientes conectados al servidor.
         *
         * @param client El manejador del cliente a eliminar.
         */
        public void removeClient(ClientHandler client) {
            clients.remove(client);
        }


    private JTextArea historyArea = new JTextArea();



        /**
         * Muestra una ventana de historial de mensajes en el servidor.
         */
    public void showHistoryWindow() {
        // implementación del método showHistoryWindow
        JFrame frame = new JFrame("Historial");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

        /**
         * Agrega un mensaje al historial de mensajes en el servidor.
         *
         * @param message El mensaje a agregar al historial.
         */
    public void addToHistory(String message) {
        // implementación del método addToHistory
        historyArea.append(message + "\n");
    }
    }
