package engine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static utils.Param.mapPath;

public class GameActivity {
    private int level, score, pulls;
    int width, height;
    private MapObject[][] map;
    private final AtomicBoolean isPause;
    private ArrayList<Player> players;
    private ArrayList<Box> boxes;

    public GameActivity() {
        this.isPause = new AtomicBoolean(false);
        level = 1;
        loadMap();
    }

    private void loadMap() {
        players = new ArrayList<>();
        boxes = new ArrayList<>();
        pulls = 5;
        try {
            Path mapLevelPath = Paths.get(mapPath, "map" + level + ".txt");
            byte[] tempMapArray = Files.readAllBytes(mapLevelPath);

            StringBuilder tempRows = new StringBuilder();
            StringBuilder tempCols = new StringBuilder();
            StringBuilder tempPool = new StringBuilder();
            int temp = 0;

            while((char)tempMapArray[temp] != ',')
            {
                tempRows.append((char) tempMapArray[temp]);
                temp++;
            }
            temp++;
            while((char)tempMapArray[temp] != ',')
            {
                tempCols.append((char) tempMapArray[temp]);
                temp++;
            }
            temp++;
            while((char)tempMapArray[temp] != '\r')
            {
                tempPool.append((char) tempMapArray[temp]);
                temp++;
            }
            height = Integer.parseInt(tempRows.toString());
            width = Integer.parseInt(tempCols.toString());
            score = Integer.parseInt(tempPool.toString());

            map = new MapObject[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if ((char) tempMapArray[temp] == '_') {
                        map[i][j] = new MapObject(false, false);
                    } else if ((char) tempMapArray[temp] == 'X') {
                        map[i][j] = new MapObject(true, false);
                    } else if ((char) tempMapArray[temp] == '*') {
                        boxes.add(new Box(i, j));
                    } else if ((char) tempMapArray[temp] == '.') {
                        map[i][j] = new MapObject(false, true);
                    } else if ((char) tempMapArray[temp] == '@') {
                        players.add(new Player(i, j));
                    } else { j--; }
                    temp++;
                }
            }
        } catch (Exception e) {
            System.out.println("Nie wczytano pliku");
        }
    }

    public byte[] createDTO() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(",").append(height).append(",").append(score).append("\n");
        byte[][] symbolMap = new byte[height][width];
        for(int i = 0 ; i < height ; i++) {
            for(int j = 0 ; j < width ; j++) {
                if(map[i][j].isCollision()) symbolMap[i][j] = 'X';
                else if(map[i][j].isDestination()) symbolMap[i][j] = '.';
                else symbolMap[i][j] = '_';
            }
        }

        players.forEach((it) -> symbolMap[it.getY()][it.getX()] = '@');
        boxes.forEach((it) -> symbolMap[it.getY()][it.getX()] = '*');

        for(int i = 0 ; i < height ; i++) {
            for(int j = 0 ; i < width ; j ++) {
                sb.append(symbolMap[i][j]);
            }
            sb.append("\n");
        }
        return String.valueOf(sb).getBytes();
    }

    private boolean isMoveLegal(int id, int dx, int dy) {
        Box tempBox = null;
        Player player = players.get(id);
        if(player.getX() + dx < 0 || player.getY() + dy < 0 ||
            player.getX() + dx >= width || player.getY() + dy >= height)
            return false;
        if(map[player.getY() + dy][player.getX() + dx].isCollision())
            return false;
        for(Box b: boxes) {
            if (b.matchPosition(player.getX() + dx, player.getY() + dy)) {
                tempBox = b;
                break;
            }
        }
        if(tempBox != null) {
            if(map[tempBox.getY() + 2 * dy][tempBox.getX() + 2 * dx].isCollision())
                return false;
            for(Box b: boxes) {
                if (b.matchPosition(tempBox.getX() + 2 * dx, tempBox.getY() + 2 * dy)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void movePlayer(int id, int dx, int dy) {
        Player player = players.get(id);
        player.move(dx, dy);
        for(Box b: boxes) {
            if(b.matchPosition(player.getX(), player.getY())) {
                b.move(dx, dy);
                b.setChecked(map[b.getY()][b.getX()].isDestination());
                break;
            }
        }
    }

    private boolean isPullLegal(int id) {
        Box tempBox = null;
        Player player = players.get(id);
        for(Box b: boxes) {
            if (Math.abs(b.getX() - player.getX()) <= 1 && Math.abs(b.getY() - player.getY()) <= 1) {
                tempBox = b;
                break;
            }
        }
        if(tempBox == null) return false;

        int diffY = tempBox.getY() - player.getY();
        int diffX = tempBox.getX() - player.getX();
        if(map[player.getY() - diffY][player.getX() - diffX].isCollision())
            return false;
        for(Box b: boxes) {
            if (b.matchPosition(player.getY() - diffY, player.getX() - diffX)) {
                return false;
            }
        }
        return true;
    }

    private void pullBox(int id) {
        Player player = players.get(id);
        Box tempBox = null;
        for(Box b: boxes) {
            if (Math.abs(b.getX() - player.getX()) <= 1 && Math.abs(b.getY() - player.getY()) <= 1) {
                tempBox = b;
                break;
            }
        }
        if(tempBox == null) return;
        int diffY = tempBox.getY() - player.getY();
        int diffX = tempBox.getX() - player.getX();
        player.move(diffX, diffY);
        tempBox.move(diffX, diffY);
    }

    public boolean isGameOver() {
        return score <= 0;
    }

    public boolean isGameWon() {
        for(Box b: boxes) {
            if(!b.isChecked()) return false;
        }
        level = Math.min(3, level + 1);
        return true;
    }

    public void serviceEvent(int id, char cmd) {
        switch (cmd){
            case 'w':
                if(!isPause.get() && score > 0 && isMoveLegal(id, 0, -1)) {
                    movePlayer(id, 0, -1);
                    score -= 1;
                }
                break;
            case 's':
                if(!isPause.get() && score > 0 && isMoveLegal(id, 0, 1)) {
                    movePlayer(id, 0, 1);
                    score -= 1;
                }
                break;
            case 'a':
                if(!isPause.get() &&  score > 0 && isMoveLegal(id, -1, 0)) {
                    movePlayer(id, -1, 0);
                    score -= 1;
                }
                break;
            case 'd':
                if(!isPause.get() && score > 0 && isMoveLegal(id, 1, 0)) {
                    movePlayer(id, 1, 0);
                    score -= 1;
                }
                break;
            case 'p':
                isPause.set(!isPause.get());
                break;
            case 'x':
                loadMap();
                break;
            case 'z':
                if(!isPause.get() && score > 0 && pulls > 0 && isPullLegal(id)) {
                    pullBox(id);
                    score -= 1;
                    pulls -= 1;
                }
                break;
            default:
                break;
        }
    }
}
