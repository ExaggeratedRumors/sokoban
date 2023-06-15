package communication;

import engine.GameActivity;
import utils.Param;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerService {
    private final int HANDLER_DELAY = 150;
    int port = Param.socket;
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
                    Connection connection = new Connection(clients.size(), socket, Connection.AppType.SERVER);
                    if(!activity.addClient()) {
                        connection.post(new DTO('.'));
                        continue;
                    }
                    clients.add(connection);
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
                    if(activity.isGameOver())
                        it.post(new DTO('l'));
                    else if(activity.isGameWon())
                        it.post(new DTO('p'));
                    else it.post(new DTO(activity.mapToDTO()));
                });
            }
        });
        handler.setDaemon(true);
        handler.start();
    }
}
