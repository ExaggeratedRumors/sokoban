package MapObjects;

import java.awt.*;

import Interface.IField;
import static Config.Param.*;

/**
 * Klasa skrzyń przemieszczanych przez gracza
 */
public class BoxChest extends Field implements IField{

    /**
     * Kolor skrzyni
     */
    private final Color chestColor = new Color(boxColor);
    /**
     * Kolor skrzyni po przeniesieniu na punkt docelowy
     */
    private final Color checkpointColor = new Color(26316);

    /**
     * Konstruktor
     * @param height wysokość okna gry
     * @param width szerokość okna gry
     * @param positionx pozycja X na mapie
     * @param positiony pozycja Y na mapie
     * @param fieldHeight wysokość pola
     * @param fieldWidth szerokość pola
     */
    public BoxChest(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(boxColor),Type.MOVING);
        previousField = new Floor(height,width,positionx,positiony,fieldHeight,fieldWidth);
    }

    /**
     * Metoda sprawdzająca czy skrzynia jest na polu docelowym
     */
    @Override
    public int isCorrectPosition(){
        if(previousField.isCheckPoint()) return 0;
        else return 1;
    }

    /**
     * Pobiera kolor skrzyni
     * @return kolor skrzyni
     */
    @Override
    public Color getChestColor(){return chestColor;}

    /**
     * Pobiera kolor skrzyni znajdującej się na polu docelowym
     * @return kolor skrzyni
     */
    @Override
    public Color getCheckpointColor(){return checkpointColor;}

    /**
     * Metoda rysująca model obiektu
     * @param g parametr graficzny
     */
    @Override
    public void draw(Graphics g){
        super.draw(g);
    }
}