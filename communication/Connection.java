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
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
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
                    DTO message = (DTO)ois.readObject();
                    mapData.add(message);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                    Date date = new Date(System.currentTimeMillis());
                    System.out.println(playerID + "("+ formatter.format(date) + "): ");
                }
                catch(Exception e) {
                    isConnected.set(false);
                    System.out.println(playerID + ": Connection lost!");
                }
            }
        });

        listener.setDaemon(true);
        listener.start();
    }

    public void post(DTO message) {
        Thread post = new Thread(() -> {
            try {
                Thread.sleep(1);
                oos.writeObject(message);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        post.setDaemon(true);
        post.start();
    }

    public void post(char cmd) {
        Thread post = new Thread(() -> {
            try {
                Thread.sleep(1);
                oos.writeObject(cmd);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
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
