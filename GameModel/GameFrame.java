package GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import Event.GameEvent;
import Event.SideBlockEvent;
import Interface.IApplicationMenu;
import Interface.IGameFrame;
import Interface.ISideBlock;
import static Config.Param.*;

/**
 * Klasa odpowiadająca za okno gry (plansza gry + panel boczny)
 */
public class GameFrame extends JFrame implements IGameFrame, ComponentListener, ActionListener{
    /**
     * Wynik
     */
    private int score;
    /**
     * Najniższy wynik na liście
     */
    private int lowestScore;
    /**
     * Szerokość okna
     */
    private int width;
    /**
     * Wysokość okna
     */
    private int height;
    /**
     * Obiekt bocznego panelu
     */
    private SideBlock sideBlock;
    /**
     * Status gry
     */
    private Status status;
    /**
     * Obiekt zdarzenia
     */
    private ActionListener listener;
    /**
     * Obiekt graficzny
     */
    private Graphics graphics;
    /**
     * Obiekt graficzny
     */
    private Image gObject;
    /**
     * Obiekt planszy gry
     */
    private GameArea gameArea;
    /**
     * Lista listenerów informowanych o zdarzeniach
     */
    private ArrayList<IApplicationMenu> listeners;
    /**
     * Nazwa gracza
     */
    private String nickname;
    /**
     * Obiekt wątku głównego
     */
    private Thread thread;
    /**
     * Obiekt wątku panelu bocznego
     */
    private Thread sbThread;
    /**
     * Typ wyliczeniowy statusu gry
     */
    private enum Status{
        RUNNING,
        PAUSE,
        GAMEOVER
    }

    /**
     * Konstruktor
     * @param listener
     */
    public GameFrame(ActionListener listener, int lowestScore) {
        this.lowestScore=lowestScore;
        nickname=null;
        this.listener = listener;
        status = Status.RUNNING;

        setFrame();
        setGameLayout();
        pack();
        setLocationRelativeTo(null);
        addComponentListener(this);

        listeners = new ArrayList<>();
        sideBlock.add((IGameFrame)this);
        thread = new Thread(gameArea);
        sbThread = new Thread(sideBlock);
        gameArea.add((ISideBlock)sideBlock);
        sbThread.start();
        thread.start();
    }

    /**
     * Układ ekranu gry
     */
    private void setGameLayout(){
        gameArea = new GameArea(width, height);
        score = gameArea.getPool();
        getContentPane().add(gameArea,BorderLayout.CENTER);
        sideBlock = new SideBlock(width, height, listener, score);
        getContentPane().add(sideBlock,BorderLayout.EAST);
    }

    /**
     * Metoda ustawiająca rozmiary okna gry
     */
    private void setFrame(){
        width = gameWidth;
        height = gameHeight;
        setTitle("Sokoban");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(gameWidth,gameHeight));
        setVisible(true);
    }

    /**
     * Metoda odpowiedzialna za zakończenie gry
     * @param mapNumber
     * @param score
     */
    private void gameOver(int mapNumber, int score){
        status = Status.GAMEOVER;
        gameArea.setGameOver();
        if(mapNumber<=numberOfMaps) {
            score=0;
        }
        else if(mapNumber>numberOfMaps && score>=lowestScore) {
            while (nickname == null || nickname.length() == 0) {
                nickname = JOptionPane.showInputDialog(this, "Wygrałeś! Zdobyłeś "+score+" punków. Proszę podać nazwę gracza", "Nazwa gracza", JOptionPane.INFORMATION_MESSAGE);
                if (nickname == null) {
                    break;
                }
            }
        }
        Object[] options={
                "Graj od nowa","Powrót do menu","Lista najlepszych wyników"
        };
        switch(JOptionPane.showOptionDialog(this,  "Ukończono grę na "+mapNumber+" mapie i zdobyto "+score+" punktów.","GameOver"
                ,JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1])) {
            case JOptionPane.YES_OPTION:
                notify(new GameEvent("Play",nickname,score));
                break;
            case JOptionPane.NO_OPTION:
                notify(new GameEvent("Back",nickname,score));
                break;
            case JOptionPane.CANCEL_OPTION:
                notify(new GameEvent("ScoreLabel",nickname,score));
                break;
            default:
                break;
        }
    }

    /**
     * Dodawanie listenerów
     * @param listener
     */
    @Override
    public void add(IApplicationMenu listener) {
        listeners.add(listener);
    }

    /**
     * Usuwanie listenerów
     * @param listener
     */
    @Override
    public void remove(IApplicationMenu listener) {
        listeners.remove(listeners.indexOf(listener));
    }

    /**
     * Notyfikacja o evencie wszystkich listenerów
     * @param event obiekt zdarzenia
     */
    @Override
    public void notify(GameEvent event) {
        for(IApplicationMenu listener : listeners){
            listener.applicationMenuEvent(event);
        }
    }

    /**
     * Obsługa zdarzeń panelu bocznego
     * @param event Obiekt panelu informacyjnego
     */
    public void sideBlockEvent(SideBlockEvent event) {
        String command=event.getCommand();
        switch (command){
            case "OutOfPoints":
                gameOver(event.getMapNumber(),event.getScore());
                break;
            case "LastMap":
                gameOver(event.getMapNumber(),event.getScore());
                break;
            default:
                break;
        }
    }

    /**
     * Metoda odpowiedzialna za ruch komponentów
     * @param event obiekt zdarzenia komponentu graficznego
     */
    @Override
    public void componentMoved(ComponentEvent event){}

    /**
     * Metoda odpowiedzialna za pokazywanie komponentów
     * @param event obiekt zdarzenia komponentu graficznego
     */
    @Override
    public void componentShown(ComponentEvent event){}

    /**
     * Metoda odpowiedzialna za ukrywanie komponentów
     * @param event obiekt zdarzenia komponentu graficznego
     */
    @Override
    public void componentHidden(ComponentEvent event){}

    /**
     * Metoda odpowiedzialna za zmianę rozmiarów komponentów
     * @param event obiekt zdarzenia komponentu graficznego
     */
    @Override
    public void componentResized(ComponentEvent event) {
        sideBlock.defaultDimensions(getContentPane().getWidth(), getContentPane().getHeight());
        gameArea.defaultDimensions(getContentPane().getWidth(), getContentPane().getHeight());
    }

    /**
     * Metoda odpowiedzialna za rysowanie komponentów na planszy
     * @param g
     */
    @Override
    public void paint(Graphics g)
    {
        gObject=createImage(getWidth(),getHeight());
        graphics = gObject.getGraphics();
        super.paint(graphics);
        draw(graphics);
        g.setColor(Color.black);
        g.drawImage(gObject,0,0,this);
    }

    /**
     * Metoda odpowiedzialna za odrysowywanie komponentów
     * @param g
     */
    private void draw(Graphics g){
        repaint();
    }

    /**
     * Metoda obsługująca zdarzenie w głównym oknie gry
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        switch (cmd) {
            //Pauza
            //
            default:
                break;
        }
    }

    /**
     * Pauzowanie rozgrywki na poziomie głównego ekranu gry
     */
    public void pause(){
        if(status==Status.PAUSE) status=Status.RUNNING;
        else status=Status.PAUSE;
        gameArea.pause();
    }
}

