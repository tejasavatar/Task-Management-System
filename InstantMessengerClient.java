import java.io.*;
import java.net.*;

public class InstantMessengerClient {
    // Define server address and port
    private static final String SERVER_ADDRESS = "64.187.246.6";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            // Connection established message
            System.out.println("Connected to server.");

            // Thread to listen for server messages
            Thread serverListener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println(serverMessage); // Print server messages to console
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverListener.start(); // Start the server message listener thread

            String userInput;
            // Read user input from console
            while ((userInput = consoleReader.readLine()) != null) {
                if (userInput.startsWith("/file")) { // Check if user wants to send a file
                    String filePath = userInput.substring(6).trim(); // Extract file path from user input
                    sendFile(filePath, writer); // Send the file to the server
                } else {
                    writer.println(userInput); // Send regular user input to server
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any IOExceptions
        }
    }

    // Method to send a file to the server
    private static void sendFile(String filePath, PrintWriter writer) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                writer.println(line); // Send each line of the file content to the server
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any IOExceptions while reading file
        }
    }
}







