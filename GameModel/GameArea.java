package GameModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

import Event.*;
import Interface.ISideBlock;
import MapObjects.*;
import Interface.IGameArea;
import static Config.Param.*;

/**
 * Klasa odpowiadająca za obszar gry i planszę
 */
public class GameArea extends JPanel implements IGameArea, KeyListener, Runnable{
    /**
     * Wysokość panelu
     */
    private int panelHeight;
    /**
     * Szerokość panelu
     */
    private int panelWidth;
    /**
     * liczba kolumn siatki
     */
    private int colsNumber;
    /**
     * Liczba wierszy siatki
     */
    private int rowsNumber;
    /**
     * Tablica obiektów planszy
     */
    private Field[][] fields;
    /**
     * Tablica obiektów pól zakrytych
     */
    private Field[][] backgroundFields;
    /**
     * Obiekt pierwszego gracza
     */
    private Player player;
    /**
     * Wymiary okna
     */
    private Dimension gameScreenSize;
    /**
     * Kolor tła
     */
    private Color colors[] = {new Color(2293760), new Color(2303302), new Color(2303252)};
    /**
     * Trzon nazwy pliku mapy
     */
    private String mapTagName;
    /**
     * Obiekt ruchu gracza
     */
    private Move playerSetUp;
    /**
     * Numer obecnej mapy
     */
    private int currentMap;
    /**
     * Liczba pozostałych bloków do ułożenia
     */
    private int blocksAmounts;
    /**
     * Liczba graczy
     */
    private int numberOfPlayers;
    /**
     * Lista słuchaczy z panelu bocznego gry
     */
    private ArrayList<ISideBlock> listeners;
    /**
     * Pula punktów obecnej mapy
     */
    private int pool;
    /**
     * Obecna liczba punktów
     */
    private int score;
    /**
     * Liczba możliwych pociągnięć skrzyń
     */
    private int boxPull;
    /**
     * Liczba możliwych zresetowań planszy
     */
    private int reset;
    /**
     * Typ boolowski określający czy przebiega proces resetu mapy
     */
    private boolean resetProcess;
    /**
     * Typ boolowski określający czy przebiega pauza
     */
    private boolean paused;
    /**
     * Typ boolowski określający odwrotny przebieg ruchu
     */
    private boolean reverseMove;
    /**
     * Typ boolowski określający czy przebiega proces animacji
     */
    private boolean animation;
    /**
     * Typ boolowski określający czy przebiega proces ruchu
     */
    private boolean move;
    /**
     * Typ boolowski określający czy gra została zakończona
     */
    private boolean gameOver;
    /**
     * Obiekt zdarzenia ruchu
     */
    private MoveEvent moveEvent;
    /**
     * Liczba klatek animacji
     */
    private int fps;

    /**
     * Konstruktor planszy gry
     * @param width szerokość okna
     * @param height wysokość okna
     */
    public GameArea(int width, int height) {
        boxPull=numberOfBoxPull;
        reset=numberOfResets;
        paused=false;
        reverseMove=false;
        resetProcess=false;
        gameOver=false;
        fps=32;
        move=false;
        animation = false;
        listeners = new ArrayList<>();
        setFrame(width,height);
        this.getSize().getHeight();
        addKeyListener(this);
        currentMap = 1;
        loadMap();
    }

    /**
     * Dodawanie obserwatorów do wszystkich pól gry
     */
    public void addFieldListener(){
        playerSetUp.add(this);
        for(Field[] fieldArray : backgroundFields)
        {
            for(Field field: fieldArray){
                field.add(this);
            }
        }
        for(Field[] fieldArray : fields)
        {
            for(Field field: fieldArray){
                field.add(this);
            }
        }
    }

    /**
     * Metoda ustawiająca wymiary okna
     * @param width szerokość okna
     * @param height wysokość okna
     */
    public void setFrame(int width, int height){
        panelHeight = height;
        panelWidth = (int)Math.round(width*0.6);
        gameScreenSize = new Dimension(panelWidth,panelHeight);
        setPreferredSize(gameScreenSize);
    }

    /**
     * Ustawianie rozmiarów obiektów w zależności od rozmiarów okna gry
     * @param height
     * @param width w
     */
    public void defaultDimensions(int width,int height) {
        panelHeight = height;
        panelWidth = (int)Math.round(width*0.6);
        playerSetUp.refreshPosition(panelWidth,panelHeight);
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

    /**
     * Metoda ładująca mapę
     */
    private void loadMap(){
        mapTagName = mapName+currentMap;
        getMapArray();
        addFieldListener();
        this.setFocusable(true);
    }

    /**
     * Metoda obsługująca wciśnięcie przycisku
     * @param keyEvent zdarzenie związane z przyciskiem
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    /**
     * Metoda obsługująca naciśnięcie przycisku
     * @param keyEvent zdarzenie związane z przyciskiem
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) { playerSetUp.keyPressed(keyEvent); }

    /**
     * Metoda obsługująca zwolnienie przycusku.
     * @param keyEvent zdarzenie związane z przyciskiem
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        playerSetUp.keyReleased(keyEvent);
    }

    /**
     * Główny wątek programu
     */
    @Override
    public void run() {
        try {
            while (!gameOver) {
                System.out.println();
                if (!resetProcess) {
                    if (!checkPosition()) {
                        if (animation && !reverseMove) {
                            move = true;
                            animation = false;
                            for (int i = 0; i < fps; i++) {
                                if (!paused) {
                                    playerSetUp.animation(moveEvent.getDirection(), moveEvent.isCollision(), false);
                                    Thread.sleep(9);
                                } else i--;
                            }
                            playerSetUp.setMove(moveEvent.getDirection(), moveEvent.isCollision(), false);
                            move = false;
                        } else if (animation && reverseMove) {
                            move = true;
                            animation = false;
                            reverseMove = false;
                            for (int i = 0; i < fps; i++) {
                                if (!paused) {
                                    playerSetUp.animation(moveEvent.getDirection(), moveEvent.isCollision(), true);
                                    Thread.sleep(9);
                                } else i--;
                            }
                            playerSetUp.setMove(moveEvent.getDirection(), moveEvent.isCollision(), true);
                            move = false;
                        }
                        fields = playerSetUp.getFields();
                        playerSetUp.refreshPosition(panelWidth, panelHeight);
                    } else {
                        if(currentMap!=numberOfMaps) {
                            notify(new GameAreaEvent("EndMap",score,0));
                            currentMap++;
                            loadMap();
                            notify(new GameAreaEvent("NewMap",score,0));
                        }
                        else {
                            currentMap++;
                            System.out.println(currentMap);
                            notify(new GameAreaEvent("LastMap",score,currentMap));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Metoda sprawdzająca czy wszystkie skrzynie są na pozycjach docelowych
     */
    public boolean checkPosition(){
        if(!resetProcess) {
            int temp = 0;
            for (Field[] fieldArray : fields) {
                for (Field field : fieldArray) {
                    temp += field.isCorrectPosition();
                }
            }
            blocksAmounts = temp;
            if (temp == 0) return true;
            else return false;
        }
        return false;
    }

    /**
     * Metoda wywołująca pauzę
     */
    public void pause(){
        if(paused) paused=false;
        else paused=true;
    }

    /**
     * Metoda wywołująca koniec gry
     */
    public void setGameOver(){
        if(!gameOver) gameOver=true;
    }

    /**
     * Metoda tworząca tablicę pól planszy
     * @return  zwraca dwuwymiarową tablicę obiektów
     */
    private Field[][] getMapArray() {
        try {
            Path mapLevelPath = Paths.get(mapPath, mapTagName+".txt");
            byte[] tempMapArray = Files.readAllBytes(mapLevelPath);

            String tempRows = "";
            String tempCols = "";
            String tempPool = "";
            int temp = 0;

            while((char)tempMapArray[temp] != ',')
            {
                tempRows += (char)tempMapArray[temp];
                temp++;
            }
            temp++;
            while((char)tempMapArray[temp] != ',')
            {
                tempCols += (char)tempMapArray[temp];
                temp++;
            }
            temp++;
            while((char)tempMapArray[temp] != '\r')
            {
                tempPool += (char)tempMapArray[temp];
                temp++;
            }
            rowsNumber = Integer.parseInt(tempRows);
            colsNumber = Integer.parseInt(tempCols);
            pool = Integer.parseInt(tempPool);
            score=pool;
            numberOfPlayers = 0;
            blocksAmounts = 0;

            double tempWidth=0.8*1/Math.max(colsNumber,rowsNumber);
            fields = new Field[rowsNumber][colsNumber];
            backgroundFields = new Field[rowsNumber][colsNumber];
            for (int temp_i = 0; temp_i < rowsNumber; temp_i++) {
                for (int temp_j = 0; temp_j < colsNumber; temp_j++) {
                    if ((char) tempMapArray[temp] == '_') {
                        fields[temp_i][temp_j] = new Floor(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                        backgroundFields[temp_i][temp_j] = new Floor(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                    } else if ((char) tempMapArray[temp] == 'X') {
                        fields[temp_i][temp_j] = new Wall(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                        backgroundFields[temp_i][temp_j] = new Wall(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                    } else if ((char) tempMapArray[temp] == '*') {
                        fields[temp_i][temp_j] = new BoxChest(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth,tempWidth);
                        backgroundFields[temp_i][temp_j] = new Floor(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                        blocksAmounts++;
                    } else if ((char) tempMapArray[temp] == '.') {
                        fields[temp_i][temp_j] = new DestinationPoint(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                        backgroundFields[temp_i][temp_j] = new DestinationPoint(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                    } else if ((char) tempMapArray[temp] == '@') {
                        player = new Player(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                        fields[temp_i][temp_j] = player;
                        backgroundFields[temp_i][temp_j] = new Floor(panelHeight, panelWidth,temp_j*tempWidth, temp_i*tempWidth, tempWidth, tempWidth);
                        numberOfPlayers++;
                        if(numberOfPlayers == 1) playerSetUp = new Move(temp_j, temp_i, player);
                    } else { temp_j--; }
                    temp++;
                }
            }
            playerSetUp.setFields(fields);
            if(numberOfPlayers > 1) {
                throw new IllegalArgumentException("Mapa zawiera zbyt wielu graczy");
            }
        } catch (Exception e) {
            System.out.println("Nie wczytano pliku");
        }
        return fields;
    }

    /**
     * Pobranie puli punktów obecnej mapy
     * @return pula punków
     */
    public int getPool(){ return pool; }

    /**
     * Metoda rysująca tło i obiekty planszy
     * @param g parametr graficzny
     */
    public void paint(Graphics g) {
        drawBackground(g);
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

    /**
     * Metoda tworząca tło pola gry
     * @param g parametr graficzny
     */
    public void drawBackground(Graphics g) {
        for (int i = 0; i  <= getHeight(); i += 1) {
            g.setColor(colors[i % colors.length]);
            g.fillRect(0, i, getWidth(), getHeight());
        }
    }

    /**
     * Metoda dodajaca słuchacza
     * @param listener
     */
    @Override
    public void add(ISideBlock listener) {
        listeners.add(listener);
    }

    /**
     * Metoda usuwająca słuchacza
     * @param listener
     */
    @Override
    public void remove(ISideBlock listener) {
        listeners.remove(listeners.indexOf(listener));
    }

    /**
     * Metoda zawiadamiająca słuchaczy panelu bocznego o zajściu zdarzenia
     * @param event
     */
    @Override
    public void notify(GameAreaEvent event) {
        for(ISideBlock listener : listeners){
            listener.gameAreaEvent(event);
        }
    }

    /**
     * Metoda obsługująca zdarzenie związane z polami gry
     * @param event
     */
    @Override
    public void fieldEvent(FieldEvent event){}

    /**
     * Metoda obsługująca zdarzenie związane z ruchem obiektów
     * @param event
     */
    @Override
    public void moveEvent(MoveEvent event)
    {
        String cmd = event.getCommand();
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
