package GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Event.*;
import Interface.ISideBlock;
import Interface.IGameFrame;
import static Config.Param.*;


public class SideBlock extends JPanel implements ActionListener, ISideBlock, Runnable{
    private int panelWidth;
    private int panelHeight;
    private Dimension sideBlockDimensions;
    private int currentScore;
    private int totalScore;
    private int previousScore;
    private int resetNumber;
    private int pullNumber;
    private boolean paused;
    private ActionListener listener;
    private ArrayList<IGameFrame> listeners;
    JLabel sideBlockDescriptions;
    JLabel totalScoreDescription;
    JLabel resetBlock;
    JLabel pullBlock;
    JLabel pauseLabel;
    JLabel freeSpace;

    public SideBlock(int panelWidth, int panelHeight, ActionListener listener, int score){
        paused=false;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
        this.listener = listener;
        listeners = new ArrayList<>();
        currentScore = score;
        previousScore=0;
        totalScore = score+previousScore;
        resetNumber = numberOfResets;
        pullNumber = numberOfBoxPull;
        sideBlockDimensions= new Dimension((int)(0.3*panelWidth),panelHeight);
        this.setPreferredSize(sideBlockDimensions);
        this.setBackground(new Color(1840170));
        setLayout(new GridBagLayout());
        sideBlockLayout();
    }

    private void sideBlockLayout(){

        GridBagConstraints constraint = new GridBagConstraints();

        sideBlockDescriptions= new JLabel("Pula punków planszy: " + currentScore);
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipady = 0;
        constraint.weighty = 0.1;
        constraint.anchor = GridBagConstraints.PAGE_START;
        constraint.insets = new Insets(40,20,0,20);
        constraint.gridx = 1;
        constraint.gridwidth = 2;
        constraint.gridy = 2;
        sideBlockDescriptions.setForeground(Color.RED);
        add(sideBlockDescriptions, constraint);

        totalScoreDescription= new JLabel("Wynik totalny: " + totalScore);
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipady = 0;
        constraint.weighty = 0.1;
        constraint.anchor = GridBagConstraints.PAGE_START;
        constraint.insets = new Insets(20,35,0,20);
        constraint.gridx = 1;
        constraint.gridwidth = 2;
        constraint.gridy = 3;
        totalScoreDescription.setForeground(Color.PINK);
        add(totalScoreDescription, constraint);

        resetBlock= new JLabel("Liczba resetów: " + resetNumber);
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipady = 0;
        constraint.weighty = 0.1;
        constraint.anchor = GridBagConstraints.PAGE_START;
        constraint.insets = new Insets(10,35,0,20);
        constraint.gridx = 1;
        constraint.gridwidth = 1;
        constraint.gridy = 4;
        resetBlock.setForeground(Color.CYAN);
        add(resetBlock, constraint);

        pullBlock= new JLabel("Liczba pociągnięć bloku: " + pullNumber);
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipady = 0;
        constraint.weighty = 0.6;
        constraint.anchor = GridBagConstraints.PAGE_START;
        constraint.insets = new Insets(10,20,0,20);
        constraint.gridx = 1;
        constraint.gridwidth = 2;
        constraint.gridy = 5;
        pullBlock.setForeground(Color.GREEN);
        add(pullBlock, constraint);

        freeSpace= new JLabel(" ");
        freeSpace.setFont(new Font("Stencil", Font.BOLD, 42));
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipady = 0;
        constraint.weighty = 0.6;
        constraint.anchor = GridBagConstraints.PAGE_END;
        constraint.insets = new Insets(10,40,50,50);
        constraint.gridx = 1;
        constraint.gridwidth = 2;
        constraint.gridy = 6;
        freeSpace.setForeground(Color.GRAY);
        add(freeSpace, constraint);

        pauseLabel= new JLabel("PAUZA");
        pauseLabel.setFont(new Font("Stencil", Font.BOLD, 42));
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipady = 0;
        constraint.weighty = 0.6;
        constraint.anchor = GridBagConstraints.PAGE_END;
        constraint.insets = new Insets(10,40,50,50);
        constraint.gridx = 1;
        constraint.gridwidth = 2;
        constraint.gridy = 6;
        pauseLabel.setForeground(Color.GRAY);
        add(pauseLabel, constraint);
        pauseLabel.setVisible(false);
    }

    public void defaultDimensions(int width, int height) {
        sideBlockDimensions = new Dimension((int) Math.round(0.3 * width), height);
        setPreferredSize(sideBlockDimensions);
        repaint();
    }

    public int getCurrentScore(){ return currentScore; }

    public void run(){
        try {
            while (true) {
                sideBlockDescriptions.setText("Pula punków planszy: " + currentScore);
                totalScoreDescription.setText("Wynik totalny: " + totalScore);
                resetBlock.setText("Liczba resetów (przycisk X): " + resetNumber);
                pullBlock.setText("Liczba pociągnięć bloku (przycisk Z): " + pullNumber);
                pauseLabel.setVisible(paused);
                defaultDimensions(panelWidth, panelHeight);
                Thread.sleep(14);
            }
        } catch (Exception e){}
    }

    @Override
    public void add(IGameFrame listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(IGameFrame listener) {
        listeners.remove(listeners.indexOf(listener));
    }

    @Override
    public void notify(SideBlockEvent event) {
        for(IGameFrame listener : listeners){
            listener.sideBlockEvent(event);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {}


    @Override
    public void gameAreaEvent(GameAreaEvent event){
        String cmd = event.getCommand();
        switch (cmd){
            case "Move":
                currentScore=event.getPool();
                totalScore=previousScore+currentScore;
                break;
            case "Pull":
                pullNumber=event.getValue();
                currentScore=event.getPool();
                totalScore=previousScore+currentScore;
                break;
            case "Reset":
                resetNumber=event.getValue();
                currentScore= event.getPool();
                totalScore=previousScore+currentScore;
                break;
            case "EndMap":
                previousScore+=event.getPool();
                totalScore=previousScore;
                break;
            case "LastMap":
                notify(new SideBlockEvent(pullNumber,resetNumber,totalScore, event.getValue(), true, "LastMap"));
                break;
            case "NewMap":
                currentScore+=event.getPool();
                totalScore=previousScore+currentScore;
                break;
            case "OutOfPoints":
                notify(new SideBlockEvent(pullNumber,resetNumber,totalScore,event.getValue(),false,"OutOfPoints"));
                break;
            case "Pause":
                if(paused) paused=false;
                else paused=true;
                break;
            default:
                break;
        }

    }

}
