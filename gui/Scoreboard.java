package gui;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class Scoreboard extends JPanel {

    private record Score(String nickName, int score) {
    }
    ArrayList<Score> scoreLadder;
    public int numberOfScores;
    public Scoreboard(int panelWidth, int panelHeight, ActionListener menuListener) {
        numberOfScores=15;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        scoreLadder = new ArrayList<>();
        loadScoreboard();
        add(createTitle(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createBackButton(menuListener), BorderLayout.SOUTH);
        setBackground(new Color(12629968));
        setVisible(true);
    }
    private void loadScoreboard() {
        try {
            File xmlInputFile = new File("utils\\scoreboard.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlInputFile);
            doc.getDocumentElement().normalize();
            NodeList nl = doc.getElementsByTagName("index");
            for (int temp = 0; temp < nl.getLength(); temp++) {
                Node node = nl.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    scoreLadder.add(new Score(element.getElementsByTagName("nickName").item(0).getTextContent(), Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent())));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Nie znaleziono pliku", "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveScore(){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element source = doc.createElement("scoreboard");
            doc.appendChild(source);
            for(int i = 0 ; i < numberOfScores ; i++) {
                if(scoreLadder.size() <= i) break;
                Element index = doc.createElement("index");
                source.appendChild(index);
                index.setAttribute("id", Integer.toString(i));
                Element nickName = doc.createElement("nickName");
                nickName.appendChild(doc.createTextNode(scoreLadder.get(i).nickName()));
                index.appendChild(nickName);
                Element score = doc.createElement("score");
                score.appendChild(doc.createTextNode(Integer.toString(scoreLadder.get(i).score())));
                index.appendChild(score);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource docSource = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Config\\scoreboard.xml"));
            transformer.transform(docSource, result);
        }
        catch (Exception ignored){}
    }

    private JTable createTable() {
        sortLadder();
        saveScore();
        Vector<Vector> rows = new Vector<>();
        for (int i = 0; i < numberOfScores; i++) {
            Vector<String> row = new Vector<>();
            String rowNumber = "nr." + (i + 1);
            row.add(rowNumber);
            row.add(scoreLadder.get(i).nickName());
            row.add(Integer.toString(scoreLadder.get(i).score()));
            rows.add(row);
        }
        Vector<String> cols = new Vector<>();
        cols.addElement("nr");
        cols.addElement("Nazwa Gracza");
        cols.addElement("Liczba punktów");
        JTable scoreLadder = new JTable(rows, cols);
        scoreLadder.setBackground(new Color(12629968));
        scoreLadder.setEnabled(false);
        scoreLadder.getTableHeader().setReorderingAllowed(false);
        setFont(new Font("Stencil", Font.PLAIN, 12));
        return scoreLadder;
    }

    private JButton createBackButton(ActionListener menuListener) {
        JButton backButton = new JButton("Powrót do menu");
        backButton.setFocusable(false);
        backButton.addActionListener(menuListener);
        backButton.setActionCommand("backButton2");
        return backButton;
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("<html><br>Lista najlepszych wyników<br><br></html>");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setFont(new Font("Stencil", Font.BOLD, 20));
        title.setForeground(new Color(7546379));
        return title;
    }

    public void sortLadder() {
        scoreLadder.sort((pair, t1) -> Integer.compare(t1.score(), pair.score()));
    }

    public void newScore(String nickname, int score){
        scoreLadder.add(new Score(nickname, score));
        sortLadder();
        saveScore();
    }

    public int getLowestScore(){
        return scoreLadder.get(numberOfScores-1).score();
    }
}

