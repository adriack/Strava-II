package es.deusto.MetaAuthServer;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MetaAuthServer {
    private static final int PORT = 8081;
    private static Map<String, String> users = new HashMap<>();

    // usuarios prueba
    static {
        users.put("ivan@meta.com", "ivan");
        users.put("javier@meta.com", "javier");
        users.put("alex@meta.com", "alex");
    }
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("MetaAuthServer is running...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = in.readLine();
            String[] parts = request.split(" ");
            String command = parts[0];

            switch (command) {
                case "REGISTER":
                    String email = parts[1];
                    String password = parts[2];
                    if (users.containsKey(email)) {
                        out.println("REGISTER_FAIL");
                    } else {
                        users.put(email, password);
                        out.println("REGISTER_SUCCESS");
                    }
                    break;

                case "VALIDATE_EMAIL":
                    email = parts[1];
                    out.println(users.containsKey(email) ? "EMAIL_VALID" : "EMAIL_INVALID");
                    break;

                case "VALIDATE_PASSWORD":
                    email = parts[1];
                    password = parts[2];
                    if (users.containsKey(email) && users.get(email).equals(password)) {
                        out.println("PASSWORD_VALID");
                    } else {
                        out.println("PASSWORD_INVALID");
                    }
                    break;

                default:
                    out.println("UNKNOWN_COMMAND");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
