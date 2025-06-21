import java.io.*;
import java.net.*;
// Define necessary imports for server and client

public class InstantMessengerClient2 {
    // Define server address and port
    private static final String SERVER_ADDRESS = "localhost";
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
                // Check if user wants to send a text file
                if (userInput.equals("/txt")) {
                    System.out.println("Enter the filename:");
                    String filename = consoleReader.readLine(); // Read filename from console
                    System.out.println("Enter the text content (Type '/done' when finished):");
                    StringBuilder textContentBuilder = new StringBuilder();
                    String line;
                    // Read text content line by line until user types '/done'
                    while (!(line = consoleReader.readLine()).equals("/done")) {
                        textContentBuilder.append(line).append("\n"); // Append each line to text content
                    }
                    String textContent = textContentBuilder.toString().trim(); // Trim excess whitespace
                    // Call method to save and send text file
                    saveAndSendTextFile(filename, textContent, writer);
                } else {
                    writer.println(userInput); // Send regular user input to server
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any IOExceptions
        }
    }

    // Method to save text file and send it to the server
    private static void saveAndSendTextFile(String filename, String textContent, PrintWriter writer) {
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(filename))) {
            fileWriter.println(textContent); // Save text content to the file
            System.out.println("File saved successfully: " + filename); // Confirmation message

            // Send the filename to the server to initiate file transfer
            writer.println("/send " + filename);

            // Close the file writer before reopening the file for reading
            fileWriter.close();

            // Open the file reader to read the saved file content
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    writer.println(line); // Send each line of text content to the server
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print any IOExceptions while reading file
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any IOExceptions while saving file
        }
    }
}