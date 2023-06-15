package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection {
    private final int playerID;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private final ArrayList<DTO> mapData;
    public enum AppType { CLIENT, SERVER }

    private final AtomicBoolean isConnected = new AtomicBoolean();

    public Connection(int playerID, Socket socket, AppType type) throws IOException {
        this.playerID = playerID;
        if (type == AppType.CLIENT) {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } else {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }

        mapData = new ArrayList<>();
        isConnected.set(true);

        Thread listener = new Thread(() -> {
            while(isConnected.get()) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                    Date date = new Date(System.currentTimeMillis());
                    System.out.println("RECEIVED:" + playerID + "("+ formatter.format(date) + ")");
                    DTO message = (DTO)ois.readObject();
                    mapData.add(message);
                } catch(Exception e) {
                    isConnected.set(false);
                    e.printStackTrace();
                    System.out.println(playerID + ": Connection lost!");
                }
            }
        });

        listener.setDaemon(true);
        listener.start();
    }

    public void post(DTO message) {
        if(!isConnected.get()) return;
        Thread post = new Thread(() -> {
            try {
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
                isConnected.set(false);
            }
        });
        post.setDaemon(true);
        post.start();
    }

    public boolean isUnreadMessage() {
        return mapData.size() > 0;
    }

    public boolean isConnected() { return isConnected.get(); }
    public DTO getUnreadMessage() {
        DTO message = mapData.get(0);
        mapData.remove(0);
        return message;
    }

    public int getID() { return this.playerID; }
}
