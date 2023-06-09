import communication.Connection;
import communication.DTO;
import engine.GameActivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    static final int HANDLER_DELAY = 1000;
    static final ArrayList<Connection> clients = new ArrayList<>();
    static ServerSocket serverSocket;
    static AtomicBoolean isWorking = new AtomicBoolean();
    static int port = 9876;

    public static void main(String[] args) throws IOException {
        GameActivity gameActivity = new GameActivity();

        System.out.println("SERVER: I am running");
        isWorking.set(true);
        serverSocket = new ServerSocket(port);
        Thread server = new Thread(() -> {
            while(isWorking.get()){
                try{
                    Socket socket = serverSocket.accept();
                    String clientName = "Client " + (clients.size() + 1);
                    clients.add(new Connection(clients.size(), socket, Connection.AppType.SERVER));
                    System.out.println("SERVER: Start connection with " + clientName);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        server.setDaemon(true);
        server.start();

        Thread handler = new Thread(() -> {
            while(isWorking.get()){
                try {
                    Thread.sleep(HANDLER_DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for(int j = 0 ; j < clients.size() ; j++) {
                    if(!clients.get(j).isConnected()) {
                        clients.remove(j);
                        break;
                    }
                }

                clients.forEach((it) -> {
                    if(it.isUnreadMessage()) {
                        DTO dto = it.getUnreadMessage();
                        gameActivity.serviceEvent(it.getID(), dto.getCommand());
                    }
                    it.post(new DTO(gameActivity.createDTO()));
                });
            }
        });
        handler.setDaemon(true);
        handler.start();

        while(isWorking.get()) {
        }
    }
}