package communication;

import engine.GameActivity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerService {
    private final int HANDLER_DELAY = 150;
    int port = 21000;
    private final ArrayList<Connection> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private GameActivity activity;
    private final AtomicBoolean isWorking = new AtomicBoolean();

    public void runServer(GameActivity activity) throws IOException {
        System.out.println("SERVER: I am running");
        this.activity = activity;
        isWorking.set(true);
        serverSocket = new ServerSocket(port);
        initConnectionHandler();
        initClientHandler();

        while(isWorking.get()) {
        }
    }

    public void initConnectionHandler() {
        Thread server = new Thread(() -> {
            while(isWorking.get()){
                try {
                    Socket socket = serverSocket.accept();
                    clients.add(new Connection(clients.size(), socket, Connection.AppType.SERVER));
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
        server.setDaemon(true);
        server.start();
    }

    public void initClientHandler() {
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
                        activity.serviceEvent(it.getID(), dto.getCommand());
                    }
                    it.post(new DTO(activity.mapToDTO()));
                });
            }
        });
        handler.setDaemon(true);
        handler.start();
    }
}
