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


public class GameFrame extends JFrame implements IGameFrame, ComponentListener, ActionListener{

    private final int lowestScore;
    private int width;
    private int height;
    private SideBlock sideBlock;
    private Status status;
    private final ActionListener listener;
    private GameArea gameArea;
    private final ArrayList<IApplicationMenu> listeners;
    private String nickname;

    private enum Status{
        RUNNING,
        PAUSE,
        GAMEOVER
    }

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
        Thread thread = new Thread(gameArea);
        Thread sbThread = new Thread(sideBlock);
        gameArea.add((ISideBlock)sideBlock);
        sbThread.start();
        thread.start();
    }

    private void setGameLayout(){
        gameArea = new GameArea(width, height);
        int score = gameArea.getPool();
        getContentPane().add(gameArea,BorderLayout.CENTER);
        sideBlock = new SideBlock(width, height, score);
        getContentPane().add(sideBlock,BorderLayout.EAST);
    }

    private void setFrame(){
        width = gameWidth;
        height = gameHeight;
        setTitle("Sokoban");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(gameWidth,gameHeight));
        setVisible(true);
    }

    private void gameOver(int mapNumber, int score){
        status = Status.GAMEOVER;
        gameArea.setGameOver();
        if(mapNumber<=numberOfMaps) {
            score=0;
        }
        else if(score >= lowestScore) {
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
        switch (JOptionPane.showOptionDialog(this, "Ukończono grę na " + mapNumber + " mapie i zdobyto " + score + " punktów.", "GameOver"
                , JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])) {
            case JOptionPane.YES_OPTION -> notify(new GameEvent("Play", nickname, score));
            case JOptionPane.NO_OPTION -> notify(new GameEvent("Back", nickname, score));
            case JOptionPane.CANCEL_OPTION -> notify(new GameEvent("ScoreLabel", nickname, score));
            default -> {
            }
        }
    }

    @Override
    public void add(IApplicationMenu listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(IApplicationMenu listener) {
        listeners.remove(listener);
    }

    @Override
    public void notify(GameEvent event) {
        for(IApplicationMenu listener : listeners){
            listener.applicationMenuEvent(event);
        }
    }

    public void sideBlockEvent(SideBlockEvent event) {
        String command=event.command();
        switch (command) {
            case "OutOfPoints", "LastMap" -> gameOver(event.mapNumber(), event.score());
            default -> {
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent event){}

    @Override
    public void componentShown(ComponentEvent event){}

    @Override
    public void componentHidden(ComponentEvent event){}

    @Override
    public void componentResized(ComponentEvent event) {
        sideBlock.defaultDimensions(getContentPane().getWidth(), getContentPane().getHeight());
        gameArea.defaultDimensions(getContentPane().getWidth(), getContentPane().getHeight());
    }

    @Override
    public void paint(Graphics g)
    {
        Image gObject = createImage(getWidth(), getHeight());
        Graphics graphics = gObject.getGraphics();
        super.paint(graphics);
        draw();
        g.setColor(Color.black);
        g.drawImage(gObject,0,0,this);
    }

    private void draw(){
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if ("Pause".equals(cmd)) {
            pause();
        }
    }

    public void pause(){
        if(status==Status.PAUSE) status=Status.RUNNING;
        else status=Status.PAUSE;
        gameArea.pause();
    }
}

