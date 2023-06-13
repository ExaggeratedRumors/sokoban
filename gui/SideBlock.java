package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import event.*;
import interfaces.ISideBlock;
import interfaces.IGameFrame;
import static utils.Param.*;


public class SideBlock extends JPanel implements ActionListener, ISideBlock, Runnable{
    private final int panelWidth;
    private final int panelHeight;
    private Dimension sideBlockDimensions;
    private int currentScore;
    private int totalScore;
    private int previousScore;
    private int resetNumber;
    private int pullNumber;
    private boolean paused;
    private final ArrayList<IGameFrame> listeners;
    JLabel sideBlockDescriptions;
    JLabel totalScoreDescription;
    JLabel resetBlock;
    JLabel pullBlock;
    JLabel pauseLabel;
    JLabel freeSpace;

    public SideBlock(int panelWidth, int panelHeight, int score){
        paused=false;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
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

        sideBlockDescriptions= new JLabel("Pula punktów planszy: " + currentScore);
        sideBlockDescriptions.setFont(new Font("Arial", Font.PLAIN, 18));
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

        totalScoreDescription= new JLabel("Wynik: " + totalScore);
        totalScoreDescription.setFont(new Font("Arial", Font.PLAIN, 18));
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
        resetBlock.setFont(new Font("Arial", Font.PLAIN, 18));
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
        pullBlock.setFont(new Font("Arial", Font.PLAIN, 18));
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

        freeSpace= new JLabel("Poruszaj się przy użyciu AWDS");
        freeSpace.setFont(new Font("Arial", Font.PLAIN, 18));
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

        pauseLabel= new JLabel("Spauzuj (P)");
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

    public void run(){
        while (true) {
            try {
            sideBlockDescriptions.setText("Ruch (AWDS): " + currentScore);
            totalScoreDescription.setText("Wynik: " + totalScore);
            resetBlock.setText("Resety (X): " + resetNumber);
            pullBlock.setText("Pociągnięcia bloków (Z): " + pullNumber);
            freeSpace.setText("Spauzuj (P)");
            freeSpace.setVisible(!paused);
            pauseLabel.setVisible(paused);
            defaultDimensions(panelWidth, panelHeight);
            Thread.sleep(14);
            } catch (Exception ignored){}
        }
    }

    @Override
    public void add(IGameFrame listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(IGameFrame listener) {
        listeners.remove(listener);
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
        String cmd = event.command();
        switch (cmd) {
            case "Move" -> {
                currentScore = event.pool();
                totalScore = previousScore + currentScore;
            }
            case "Pull" -> {
                pullNumber = event.value();
                currentScore = event.pool();
                totalScore = previousScore + currentScore;
            }
            case "Reset" -> {
                resetNumber = event.value();
                currentScore = event.pool();
                totalScore = previousScore + currentScore;
            }
            case "EndMap" -> {
                previousScore += event.pool();
                totalScore = previousScore;
            }
            case "LastMap" ->
                    notify(new SideBlockEvent(pullNumber, resetNumber, totalScore, event.value(), true, "LastMap"));
            case "NewMap" -> {
                currentScore += event.pool();
                totalScore = previousScore + currentScore;
            }
            case "OutOfPoints" ->
                    notify(new SideBlockEvent(pullNumber, resetNumber, totalScore, event.value(), false, "OutOfPoints"));
            case "Pause" -> paused = !paused;
            default -> {
            }
        }

    }

}
