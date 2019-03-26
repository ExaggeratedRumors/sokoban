package Config;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public final class Param {

    //===============================================
    //Stałe do menu
    //===============================================

    /**
     * Boczna granica menu
     */
    public static int borderHorizontal;
    /**
     * Wertykalna granica menu
     */
    public static int borderVertical;
    /**
     * Szerokość przerwy między przyciskami
     */
    public static int rigidWidth;
    /**
     * Wysokosć przerwy między przeciskami
     */
    public static int rigidHeight;
    /**
     * Szerokość przycisku Nowej gry
     */
    public static int newGameButtonWidth;
    /**
     * Wysokość przycisku Nowej gry
     */
    public static int newGameButtonHeight;
    /**
     * Szerokość przycisku tabeli wyników
     */
    public static int ladderButtonWidth;
    /**
     * Wysokość przycisku tabeli wyników
     */
    public static int ladderButtonHeight;
    /**
     * Szerokość przycisku wyjścia
     */
    public static int exitButtonWidth;
    /**
     * Wysokość przycisku wyjścia
     */
    public static int exitButtonHeight;
    /**
     * Szerokość okna menu
     */
    public static int menuWidth;
    /**
     * Wysokość okna menu
     */
    public static int menuHeight;

    //===============================================
    //Stałe do gry
    //===============================================
    /**
     * Liczba kolumn layoutu
     */
    public static int gameCols;
    /**
     * Liczba wierszy layoutu
     */
    public static int gameRows;
    /**
     * wysokość planszy
     */
    public static int gameHeight;
    /**
     * szerokość planszy
     */
    public static int gameWidth;
    /**
     * szerokość bloku bocznego
     */
    public static int sideBlockWidth;
    /**
     * Wysokość bloku bocznego
     */
    public static int sideBlockHeight;
    /**
     * Pula punktów
     */
    public static int scorePool;
    /**
    * szerokość pola
    */
    public static int fieldWidth;
    /**
     * Kolor pola gracza
     */
    public static int playerColor;
    /**
     * Kolor podłogi
     */
    public static int floorColor;
    /**
     * Kolor ściany
     */
    public static int wallColor;
    /**
     * Kolor kolor punktu docelowego
     */
    public static int destinationPointColor;
    /**
     * Kolor skrzyni
     */
    public static int boxColor;
    /**
     * Liczba użyć resetu planszy
     */
    public static int numberOfResets;
    /**
     * Liczba użyć mechanizmu pociągniecia bloku
     */
    public static int numberOfBoxPull;
    /**
     * ścieżka mapy
     */
    public static String mapPath;
    /**
     * Trzon nazwy mapy
     */
    public static String mapName="map";
    /**
     * Liczba map
     */
    public static int numberOfMaps;
    /**
     * ścieżka pliku configuracyjnego
     */
    public static final String configParams="params.xml";
    /**
     * Statyczna metoda pozwalająca na używanie stałych z pliku konfiguracyjnego globalnie
     */
    static{ parseParam(); }


    /**
     * Parsowanie pól
     */
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
            // GRA
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

