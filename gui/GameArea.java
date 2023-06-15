package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

import communication.ClientService;
import event.*;
import interfaces.ISideBlock;
import model.*;
import interfaces.IGameArea;
import static utils.Param.*;


public class GameArea extends JPanel implements IGameArea, KeyListener, Runnable{

    private int width, height;
    private int panelHeight, panelWidth;
    private int colsNumber, rowsNumber;
    private Field[][] fields;
    private Field[][] backgroundFields;
    private final Color[] colors = {new Color(2293760), new Color(2303302), new Color(2303252)};
    private String mapTagName;
    private Move playerSetUp;
    private int currentMap;
    private ArrayList<ISideBlock> listeners;
    private int pool;
    private int score;
    private int boxPull;
    private int reset;
    private boolean resetProcess;
    private boolean paused;
    private boolean reverseMove;
    private boolean animation;
    private boolean move;
    private boolean gameOver;
    private MoveEvent moveEvent;
    private int fps;
    ClientService clientServices;

    boolean online;

    public GameArea(int width, int height) {
        this.width = width;
        this.height = height;
        setFrame(width,height);
        this.getSize().getHeight();
        clientServices = new ClientService();
        addKeyListener(this);
        try {
            online = clientServices.runClient();
            //online = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(online) System.out.println("Online");
        runGame();
    }

    public void runGame() {
        boxPull=numberOfBoxPull;
        reset=numberOfResets;
        paused=false;
        reverseMove=false;
        resetProcess=false;
        gameOver=false;
        fps = 32;
        move=false;
        animation = false;
        listeners = new ArrayList<>();
        loadMap();
    }

    public void setFrame(int width, int height){
        panelHeight = height;
        panelWidth = (int)Math.round(width*0.6);
        Dimension gameScreenSize = new Dimension(panelWidth, panelHeight);
        setPreferredSize(gameScreenSize);
    }

    @Override
    synchronized public void keyTyped(KeyEvent keyEvent) { }

    @Override
    synchronized public void keyPressed(KeyEvent keyEvent) {
        if(!online) playerSetUp.keyPressed(keyEvent);
        else clientEvent(new MoveEvent("None", keyEvent.getKeyChar(), null,  false));
    }

    @Override
    synchronized public void keyReleased(KeyEvent keyEvent) {
        if(!online) playerSetUp.keyReleased(keyEvent);
    }

    @Override
    public void run() {
        try {
            while (!gameOver) {
                if(clientServices.isDisconnected()) {
                    online = false;
                    runGame();
                }
                setFocusable(true);
                setEnabled(true);
                if (!resetProcess) {
                    if (online) {
                        loadMap();
                        if(clientServices.isGameOver()) gameOver();
                        continue;
                    }
                    if(fields == null || playerSetUp == null) continue;
                    if (!checkPosition()) {
                        if (animation && !reverseMove) {
                            move = true;
                            animation = false;
                            for (int i = 0; i < fps; i++) {
                                if (!paused) {
                                    playerSetUp.animation(moveEvent.direction(), moveEvent.collision(), false);
                                    Thread.sleep(9);
                                } else i--;
                            }
                            playerSetUp.setMove(moveEvent.direction(), moveEvent.collision(), false);
                            move = false;
                        } else if (animation) {
                            move = true;
                            animation = false;
                            reverseMove = false;
                            for (int i = 0; i < fps; i++) {
                                if (!paused) {
                                    playerSetUp.animation(moveEvent.direction(), moveEvent.collision(), true);
                                    Thread.sleep(9);
                                } else i--;
                            }
                            playerSetUp.setMove(moveEvent.direction(), moveEvent.collision(), true);
                            move = false;
                        }
                        fields = playerSetUp.getFields();
                        playerSetUp.refreshPosition(panelWidth, panelHeight);
                    } else {
                        if (currentMap != numberOfMaps) {
                            notify(new GameAreaEvent("EndMap", score, 0));
                            currentMap++;
                            loadMap();
                            notify(new GameAreaEvent("NewMap", score, 0));
                        } else {
                            currentMap++;
                            System.out.println(currentMap);
                            notify(new GameAreaEvent("LastMap", score, currentMap));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadMap() {
        if(online && !clientServices.isDataExists()) return;
        try {
            byte[] tempMapArray;

            if (online) {
                tempMapArray = clientServices.getMap();
                if(tempMapArray == null) return;
            }
            else {
                currentMap = 1;
                mapTagName = mapName + currentMap;
                Path mapLevelPath = Paths.get(mapPath, mapTagName + ".txt");
                tempMapArray = Files.readAllBytes(mapLevelPath);
            }

            StringBuilder tempRows = new StringBuilder();
            StringBuilder tempCols = new StringBuilder();
            StringBuilder tempPool = new StringBuilder();
            int temp = 0;

            while ((char) tempMapArray[temp] != ',') {
                tempRows.append((char) tempMapArray[temp]);
                temp++;
            }
            temp++;
            while ((char) tempMapArray[temp] != ',') {
                tempCols.append((char) tempMapArray[temp]);
                temp++;
            }
            temp++;
            while ((char) tempMapArray[temp] != '\r' && (char) tempMapArray[temp] != '\n') {
                tempPool.append((char) tempMapArray[temp]);
                temp++;
            }
            rowsNumber = Integer.parseInt(tempRows.toString());
            colsNumber = Integer.parseInt(tempCols.toString());
            pool = Integer.parseInt(tempPool.toString());
            score = pool;

            int numberOfPlayers = 0;

            double blockSize = 0.8 * 1 / Math.max(colsNumber, rowsNumber);
            if(fields == null) fields = new Field[rowsNumber][colsNumber];
            if(backgroundFields == null) backgroundFields = new Field[rowsNumber][colsNumber];
            for (int temp_i = 0; temp_i < rowsNumber; temp_i++) {
                for (int temp_j = 0; temp_j < colsNumber; temp_j++) {
                    if ((char) tempMapArray[temp] == '_') {
                        fields[temp_i][temp_j] = new Floor(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                        backgroundFields[temp_i][temp_j] = new Floor(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                    } else if ((char) tempMapArray[temp] == 'X') {
                        fields[temp_i][temp_j] = new Wall(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                        backgroundFields[temp_i][temp_j] = new Wall(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                    } else if ((char) tempMapArray[temp] == '*') {
                        fields[temp_i][temp_j] = new BoxChest(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                        backgroundFields[temp_i][temp_j] = new Floor(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                    } else if ((char) tempMapArray[temp] == '.') {
                        fields[temp_i][temp_j] = new DestinationPoint(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                        backgroundFields[temp_i][temp_j] = new DestinationPoint(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                    } else if ((char) tempMapArray[temp] == '@') {
                        Player player = new Player(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                        fields[temp_i][temp_j] = player;
                        backgroundFields[temp_i][temp_j] = new Floor(panelHeight, panelWidth, temp_j * blockSize, temp_i * blockSize, blockSize, blockSize);
                        numberOfPlayers++;
                        if (numberOfPlayers == 1) playerSetUp = new Move(temp_j, temp_i, player);
                    } else {
                        temp_j--;
                    }
                    temp++;
                }
            }
            playerSetUp.setFields(fields);
            playerSetUp.add(this);
            this.setFocusable(true);
        } catch (FileSystemException e) {
            System.out.println("Plik nie został pobrany");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPosition(){
        if(!resetProcess && fields != null) {
            int temp = 0;
            for (Field[] fieldArray : fields) {
                for (Field field : fieldArray) {
                    temp += field.isCorrectPosition();
                }
            }
            return temp == 0;
        }
        return false;
    }

    public void pause(){
        paused = !paused;
    }

    public void setGameOver(){
        if(!gameOver) gameOver=true;
    }

    public int getPool(){ return pool; }

    public void defaultDimensions(int width,int height) {
        if(online && !clientServices.isDataExists()) return;
        panelHeight = height;
        panelWidth = (int)Math.round(width*0.6);
        if(playerSetUp != null) playerSetUp.refreshPosition(panelWidth,panelHeight);
        if(backgroundFields == null) return;
        for(Field[] fieldArray : backgroundFields)
        {
            for(Field field: fieldArray){
                field.defaultDimensions(panelWidth,panelHeight);
            }
        }
        for(Field[] fieldArray : fields)
        {
            for(Field field: fieldArray){
                field.defaultDimensions(panelWidth,panelHeight);
            }
        }
        repaint();
    }

    public void paint(Graphics g) {
        drawBackground(g);
        if(online && !clientServices.isDataExists()) return;
        for(int n = 0; n < rowsNumber; n++) {
            for (int m = 0; m < colsNumber; m++) {
                backgroundFields[n][m].draw(g);
            }
        }
        for(int n = 0; n < rowsNumber; n++) {
            for (int m = 0; m < colsNumber; m++) {
                fields[n][m].draw(g);
            }
        }
    }

    public void drawBackground(Graphics g) {
        for (int i = 0; i  <= getHeight(); i += 1) {
            g.setColor(colors[i % colors.length]);
            g.fillRect(0, i, getWidth(), getHeight());
        }
    }

    @Override
    public void add(ISideBlock listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(ISideBlock listener) {
        listeners.remove(listener);
    }

    @Override
    public void notify(GameAreaEvent event) {
        for(ISideBlock listener : listeners){
            listener.gameAreaEvent(event);
        }
    }

    public void gameOver() {
        gameOver = true;
        notify(new GameAreaEvent("OutOfPoints",score,currentMap));
    }
    public void clientEvent(MoveEvent event) {
        clientServices.post(event.code());
        switch (event.code()) {
            case 'w', 's', 'a', 'd' -> notify(new GameAreaEvent("Move", score, 0));
            case 'z' -> notify(new GameAreaEvent("Pull", score, Math.max(0,--boxPull)));
            case 'x' -> notify(new GameAreaEvent("Reset", score, Math.max(0,--reset)));
            case 'p' -> notify(new GameAreaEvent("Pause", 0, 0));
            default -> {
            }
        }
    }

    @Override
    public void moveEvent(MoveEvent event) {
        String cmd = event.command();
        switch (cmd){
            case "Move":
                if(!move && score>0) {
                    score--;
                    notify(new GameAreaEvent("Move", score,0));
                    animation = true;
                    moveEvent = event;
                }
                else if(score <= 0){
                    notify(new GameAreaEvent("OutOfPoints",score,currentMap));
                }
                break;
            case "Pull":
                if(!move && boxPull>0 && score>0) {
                    boxPull--;
                    score--;
                    notify(new GameAreaEvent("Pull", score, boxPull));
                    animation = true;
                    reverseMove=true;
                    moveEvent = event;
                }
                else if(score <= 0){
                    notify(new GameAreaEvent("OutOfPoints",score,currentMap));
                }
                break;
            case "Reset":
                if(!move && !paused && reset>0) {
                    resetProcess = true;
                    reset--;
                    loadMap();
                    notify(new GameAreaEvent("Reset", score, reset));
                    moveEvent = event;
                    resetProcess = false;
                    move = false;
                    animation = false;
                }
                break;
            case "Pause":
                pause();
                notify(new GameAreaEvent("Pause",0,0));
                break;
            default:
                break;
        }
    }
}
