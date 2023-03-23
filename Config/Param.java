package Config;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public final class Param {


    public static int borderHorizontal;
    public static int borderVertical;
    public static int rigidWidth;
    public static int rigidHeight;
    public static int newGameButtonWidth;
    public static int newGameButtonHeight;
    public static int ladderButtonWidth;
    public static int ladderButtonHeight;
    public static int exitButtonWidth;
    public static int exitButtonHeight;
    public static int menuWidth;
    public static int menuHeight;

    public static int gameCols;
    public static int gameRows;
    public static int gameHeight;
    public static int gameWidth;
    public static int sideBlockWidth;
    public static int sideBlockHeight;
    public static int scorePool;
    public static int fieldWidth;
    public static int playerColor;
    public static int floorColor;
    public static int wallColor;
    public static int destinationPointColor;
    public static int boxColor;
    public static int numberOfResets;
    public static int numberOfBoxPull;
    public static String mapPath;
    public static String mapName="map";
    public static int numberOfMaps;

    public static final String configParams="params.xml";
    static{ parseParam(); }

    public static void parseParam (){
        try {

            File config = new File(configParams);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(config);
            doc.getDocumentElement().normalize();

            ///////////////////////////
            // MENU
            ///////////////////////////

            borderHorizontal=Integer.parseInt(doc.getElementsByTagName("borderHorizontal").item(0).getTextContent());
            borderVertical=Integer.parseInt(doc.getElementsByTagName("borderVertical").item(0).getTextContent());
            rigidHeight=Integer.parseInt(doc.getElementsByTagName("rigidHeight").item(0).getTextContent());
            rigidWidth=Integer.parseInt(doc.getElementsByTagName("rigidWidth").item(0).getTextContent());
            newGameButtonWidth=Integer.parseInt(doc.getElementsByTagName("newGameButtonWidth").item(0).getTextContent());
            newGameButtonHeight=Integer.parseInt(doc.getElementsByTagName("newGameButtonHeight").item(0).getTextContent());
            ladderButtonWidth=Integer.parseInt(doc.getElementsByTagName("ladderButtonWidth").item(0).getTextContent());
            ladderButtonHeight=Integer.parseInt(doc.getElementsByTagName("ladderButtonHeight").item(0).getTextContent());
            exitButtonWidth=Integer.parseInt(doc.getElementsByTagName("exitButtonWidth").item(0).getTextContent());
            exitButtonHeight=Integer.parseInt(doc.getElementsByTagName("exitButtonHeight").item(0).getTextContent());
            menuWidth=Integer.parseInt(doc.getElementsByTagName("menuWidth").item(0).getTextContent());
            menuHeight=Integer.parseInt(doc.getElementsByTagName("menuHeight").item(0).getTextContent());

            ///////////////////////////
            // GAME
            ///////////////////////////
            gameRows=Integer.parseInt(doc.getElementsByTagName("gameRows").item(0).getTextContent());
            gameCols=Integer.parseInt(doc.getElementsByTagName("gameCols").item(0).getTextContent());
            gameWidth=Integer.parseInt(doc.getElementsByTagName("gameWidth").item(0).getTextContent());
            gameHeight=Integer.parseInt(doc.getElementsByTagName("gameHeight").item(0).getTextContent());
            sideBlockHeight=Integer.parseInt(doc.getElementsByTagName("sideBlockHeight").item(0).getTextContent());
            sideBlockWidth=Integer.parseInt(doc.getElementsByTagName("sideBlockWidth").item(0).getTextContent());
            playerColor=Integer.parseInt(doc.getElementsByTagName("playerColor").item(0).getTextContent());
            boxColor=Integer.parseInt(doc.getElementsByTagName("boxColor").item(0).getTextContent());
            floorColor=Integer.parseInt(doc.getElementsByTagName("floorColor").item(0).getTextContent());
            wallColor=Integer.parseInt(doc.getElementsByTagName("wallColor").item(0).getTextContent());
            destinationPointColor=Integer.parseInt(doc.getElementsByTagName("destinationPointColor").item(0).getTextContent());
            scorePool=Integer.parseInt(doc.getElementsByTagName("scorePool").item(0).getTextContent());
            numberOfResets=Integer.parseInt(doc.getElementsByTagName("numberOfResets").item(0).getTextContent());
            numberOfBoxPull=Integer.parseInt(doc.getElementsByTagName("numberOfBoxPull").item(0).getTextContent());
            fieldWidth=Integer.parseInt(doc.getElementsByTagName("fieldWidth").item(0).getTextContent());
            mapPath=doc.getElementsByTagName("lvlPath").item(0).getTextContent();
            mapName=doc.getElementsByTagName("mapName").item(0).getTextContent();
            numberOfMaps=Integer.parseInt(doc.getElementsByTagName("numberOfMaps").item(0).getTextContent());

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

