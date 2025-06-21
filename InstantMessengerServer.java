import java.io.*;
import java.net.*;
import java.util.*;
// Define necessary imports for server and client to ensure client and server are connected to each other
public class InstantMessengerServer {
    private static final int PORT = 12345; // Port number for the server to listen on
    private static final Map<String, PrintWriter> clientWriters = new HashMap<>(); // Map to store client usernames and corresponding PrintWriter objects

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT); // Create a ServerSocket to listen for incoming connections
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept(); // Accept a new client connection
                System.out.println("New client connected: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket); // Create a new ClientHandler for the connected client
                new Thread(clientHandler).start(); // Start a new thread to handle communication with the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket; // Socket for communication with the client
        private BufferedReader reader; // BufferedReader for reading messages from the client
        private PrintWriter writer; // PrintWriter for sending messages to the client
        private String username; // Username of the client associated with this ClientHandler

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Create BufferedReader for reading messages from the client
                writer = new PrintWriter(socket.getOutputStream(), true); // Create PrintWriter for sending messages to the client

                writer.println("Welcome to the Instant Messenger!");

                // Loop to prompt the client for their username
                while (true) {
                    writer.println("Enter your username:");
                    username = reader.readLine(); // Read the username input from the client
                    if (username == null) {
                        return; // If username is null, terminate the connection
                    }
                    synchronized (clientWriters) {
                        if (!clientWriters.containsKey(username)) {
                            writer.println("Username accepted!"); // Notify the client that their username is accepted
                            break; // If username is accepted, break out of the loop
                        } else {
                            writer.println("Username already taken. Please choose another one."); // Notify the client that the username is already taken
                        }
                    }
                }

                synchronized (clientWriters) {
                    clientWriters.put(username, writer); // Add the username and its corresponding PrintWriter to the map
                }

                broadcastMessage("[Server]: " + username + " has joined the chat."); // Broadcast a message indicating the user has joined the chat

                String clientMessage;
                while ((clientMessage = reader.readLine()) != null) {
                    System.out.println(username + ": " + clientMessage); // Print out user's messages
                    broadcastMessage(username + ": " + clientMessage); // Broadcast user's message to all clients
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (username != null) {
                    synchronized (clientWriters) {
                        clientWriters.remove(username); // Remove the client from the map when they disconnect
                    }
                    broadcastMessage("[Server]: " + username + " has left the chat."); // Broadcast a message indicating the user has left
                }
                try {
                    socket.close(); // Close the socket
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcastMessage(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters.values()) {
                    writer.println(message); // Send the message to all connected clients
                }
            }
        }
    }
}



