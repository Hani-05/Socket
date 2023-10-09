//------> For SERVER

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create input and output streams
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                // Read the string from the client
                CustomString clientString = (CustomString) in.readObject();

                // Count the characters and find the maximum
                Map<Character, Integer> charCount = new HashMap<>();
                char maxChar = '\0';
                int maxCount = 0;

                for (char c : clientString.toCharArray()) {
                    charCount.put(c, charCount.getOrDefault(c, 0) + 1);

                    if (charCount.get(c) > maxCount) {
                        maxChar = c;
                        maxCount = charCount.get(c);
                    }
                }

                // Send the result back to the client
                String result = maxChar + ":" + maxCount;
                out.writeObject(result);

                // Close the streams and socket
                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}



//------> For CLIENT

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try {
            Socket socket = new Socket(serverAddress, serverPort);

            // Create input and output streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Send the string to the server
            CustomString clientString = new CustomString("Hello , Ali! How are you ?");
            out.writeObject(clientString);

            // Receive and print the server's response
            String response = (String) in.readObject();
            System.out.println("Server Response: " + response);

            // Close the streams and socket
            out.close();
            in.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


//-------->  CustomString Class:

import java.io.Serializable;

public class CustomString implements Serializable {
    private static final long serialVersionUID = 1L;
    private String value;

    public CustomString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public char[] toCharArray() {
        return value.toCharArray();
    }
}
