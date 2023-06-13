package communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientService {
    private int serverPort = 21001;
    private final AtomicBoolean isRunning = new AtomicBoolean();
    private final AtomicBoolean dataExists = new AtomicBoolean();
    private Connection serverConnection;
    private DTO dto;

    public boolean runClient() throws IOException {
        InetAddress ipAddress = InetAddress.getLocalHost();
        isRunning.set(true);
        dataExists.set(false);
        Socket socket;
        try {
            socket = new Socket(ipAddress, serverPort);
        } catch (IOException e){
            System.out.println("CLIENT: Server is turn off");
            serverConnection = null;
            return false;
        }
        serverConnection = new Connection(0, socket, Connection.AppType.CLIENT);

        serverConnection.post(new DTO('c'));

        Thread receiver = new Thread(() -> {
            while(isRunning.get()){
                if(!serverConnection.isUnreadMessage()) continue;
                dto = serverConnection.getUnreadMessage();
                dataExists.set(true);
            }
        });

        receiver.setDaemon(true);
        receiver.start();
        return true;
    }

    public void post(char cmd) {
        this.serverConnection.post(new DTO(cmd));
    }
    public byte[] getMap() { return this.dto.map; }
    public boolean isGameOver() { return dto != null && dto.command == 'l'; }
    public boolean isGameWon() { return dto != null && dto.command == 'p'; }

    public boolean isDataExists() { return this.dataExists.get(); }
}
