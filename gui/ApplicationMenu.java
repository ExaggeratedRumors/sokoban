package gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import interfaces.IApplicationMenu;
import event.GameEvent;
import static utils.Param.*;


public class ApplicationMenu extends JFrame implements ActionListener, IApplicationMenu {
    private Dimension menuDimensions;
    private JPanel menuPanel;
    private GameFrame mainWindow;
    private Scoreboard scoreboard;

    public void runMenu() {
        menuDimensions = new Dimension(menuWidth, menuHeight);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sokoban");
        menuLayout();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void menuLayout() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(menuWidth, menuHeight));
        menuPanel.setBackground(new Color(1840170));

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, rigidHeight)));

        JLabel title = new JLabel("Menu", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(new Color(5046272));
        title.setFont(new Font("Stencil", Font.BOLD, 42));
        menuPanel.add(title);

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, 2*rigidHeight)));

        JButton newGameButton = new JButton("Nowa gra");
        newGameButton.setMaximumSize(new Dimension(newGameButtonWidth,newGameButtonHeight));
        newGameButton.setActionCommand("NewGame");
        newGameButton.addActionListener(this);
        newGameButton.setFocusable(false);
        newGameButton.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(newGameButton);

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, rigidHeight)));

        JButton ladderButton = new JButton("Lista najlepszych wyników");
        ladderButton.setMaximumSize(new Dimension(ladderButtonWidth,ladderButtonHeight));
        ladderButton.setActionCommand("ScoreLabel");
        ladderButton.addActionListener(this);
        ladderButton.setFocusable(false);
        ladderButton.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(ladderButton);

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, 2*rigidHeight)));

        JButton exitGameButton = new JButton("Wyjście");
        exitGameButton.setMaximumSize(new Dimension(exitButtonWidth,exitButtonHeight));
        exitGameButton.setActionCommand("Exit");
        exitGameButton.addActionListener(this);
        exitGameButton.setFocusable(false);
        exitGameButton.setAlignmentX(CENTER_ALIGNMENT);

        menuPanel.add(exitGameButton);

        getContentPane().add(menuPanel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        switch (cmd) {
            case "NewGame" -> {
                this.setVisible(false);
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                mainWindow = new GameFrame(this, scoreboard.getLowestScore());
                mainWindow.setVisible(true);
                mainWindow.add((IApplicationMenu) this);
            }
            case "ScoreLabel" -> {
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                this.remove(menuPanel);
                this.add(scoreboard);
                this.revalidate();
                this.repaint();
            }
            case "backButton2" -> {
                this.remove(scoreboard);
                scoreboard = null;
                this.add(menuPanel);
                this.revalidate();
                this.repaint();
            }
            case "Pause" -> mainWindow.pause();
            case "Exit" -> System.exit(0);
            default -> {
            }
        }
    }

    @Override
    public void applicationMenuEvent(GameEvent event) {
        String cmd = event.command();
        switch (cmd) {
            case "Play" -> {
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                scoreboard.newScore(event.nickName(), event.score());
                this.setVisible(false);
                mainWindow.dispose();
                mainWindow = new GameFrame(this, scoreboard.getLowestScore());
                mainWindow.add((IApplicationMenu) this);
            }
            case "Back" -> {
                mainWindow.dispose();
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                scoreboard.newScore(event.nickName(), event.score());
                this.setVisible(true);
            }
            case "ScoreLabel" -> {
                mainWindow.dispose();
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                scoreboard.newScore(event.nickName(), event.score());
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                this.remove(menuPanel);
                this.add(scoreboard);
                this.revalidate();
                this.repaint();
                this.setVisible(true);
            }
            default -> {
            }
        }
    }

}
