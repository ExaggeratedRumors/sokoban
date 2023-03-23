package GameModel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Interface.IApplicationMenu;
import Event.GameEvent;
import static Config.Param.*;


public class ApplicationMenu extends JFrame implements ActionListener, IApplicationMenu {
    private JLabel title;
    private Dimension menuDimensions;
    private JPanel menuPanel;
    private JButton newGameButton;
    private JButton ladderButton;
    private JButton exitGameButton;
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

        title = new JLabel("Menu", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(new Color(5046272));
        title.setFont(new Font("Stencil", Font.BOLD, 42));
        menuPanel.add(title);

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, 2*rigidHeight)));

        newGameButton = new JButton("Nowa gra");
        newGameButton.setMaximumSize(new Dimension(newGameButtonWidth,newGameButtonHeight));
        newGameButton.setActionCommand("NewGame");
        newGameButton.addActionListener(this);
        newGameButton.setFocusable(false);
        newGameButton.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(newGameButton);

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, rigidHeight)));

        ladderButton = new JButton("Lista najlepszych wyników");
        ladderButton.setMaximumSize(new Dimension(ladderButtonWidth,ladderButtonHeight));
        ladderButton.setActionCommand("ScoreLabel");
        ladderButton.addActionListener(this);
        ladderButton.setFocusable(false);
        ladderButton.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(ladderButton);

        menuPanel.add(Box.createRigidArea(new Dimension(rigidWidth, 2*rigidHeight)));

        exitGameButton = new JButton("Wyjście");
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
            case "NewGame":
                this.setVisible(false);
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                mainWindow = new GameFrame(this, scoreboard.getLowestScore());
                mainWindow.setVisible(true);
                mainWindow.add((IApplicationMenu)this);
                break;
            case "ScoreLabel":
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                this.remove(menuPanel);
                this.add(scoreboard);
                this.revalidate();
                this.repaint();
                break;
            case "backButton2":
                this.remove(scoreboard);
                scoreboard = null;
                this.add(menuPanel);
                this.revalidate();
                this.repaint();
                break;
            case "Pause":
                mainWindow.pause();
                break;
            case "Exit":
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void applicationMenuEvent(GameEvent event) {
        String cmd = event.getCommand();
        switch(cmd){
            case "Play":
                System.out.println("No jestem tutaj");
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                scoreboard.newScore(event.getNickName(),event.getScore());
                this.setVisible(false);
                mainWindow.dispose();
                mainWindow = new GameFrame(this,scoreboard.getLowestScore());
                mainWindow.add((IApplicationMenu)this);
                break;
            case "Back":
                mainWindow.dispose();
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                scoreboard.newScore(event.getNickName(),event.getScore());
                this.setVisible(true);
                break;
            case "ScoreLabel":
                mainWindow.dispose();
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                scoreboard.newScore(event.getNickName(),event.getScore());
                scoreboard = new Scoreboard((int) menuDimensions.getWidth(), (int) menuDimensions.getHeight(), this);
                this.remove(menuPanel);
                this.add(scoreboard);
                this.revalidate();
                this.repaint();
                this.setVisible(true);
                break;
            default:
                break;
        }
    }

}
