package no.kristiania.http;

import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.HttpController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private final ServerSocket serverSocket;
    private final Map<String, HttpController> controllers = new HashMap<>();
    private FileController fileController = new FileController();

    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        new Thread(this::handleClients).start();
    }

    private void handleClients() {
        try {
            while (true) {
                handleClient();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleClient() throws IOException, SQLException {
        Socket clientSocket = serverSocket.accept();
        HttpMessage httpMessage = new HttpMessage(clientSocket);

        HttpMessage response;
        if (controllers.containsKey(httpMessage.fileTarget)) {
            response = controllers.get(httpMessage.fileTarget).handle(httpMessage);
        }else{
            response = fileController.handle(httpMessage);
        }

        response.write(clientSocket);

    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void addController(String path, HttpController controller) {
        this.controllers.put(path, controller);
    }

    public void addFileController(FileController fileController) {
        this.fileController = fileController;
    }
}
