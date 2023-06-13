import communication.ServerService;
import engine.GameActivity;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        GameActivity gameActivity = new GameActivity();
        ServerService serverService = new ServerService();
        serverService.runServer(gameActivity);
    }
}