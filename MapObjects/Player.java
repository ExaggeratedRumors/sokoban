package MapObjects;

import java.awt.*;

import Interface.IField;
import static Config.Param.*;

/**
 * Klasa pola na którym znajduje się gracz
 */
public class Player extends Field implements IField{

    /**
     * Konstruktor
     * @param height wysokość okna gry
     * @param width szerokość okna gry
     * @param positionx pozycja X na mapie
     * @param positiony pozycja Y na mapie
     * @param fieldHeight wysokość pola
     * @param fieldWidth szerokość pola
     */
    public Player(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(playerColor),Type.STILL);
        previousField = new Floor(height,width,positionx,positiony,fieldHeight,fieldWidth);
    }

    /**
     * Metoda rysująca model obiektu
     * @param g parametr graficzny
     */
    @Override
    public void draw(Graphics g){ super.draw(g); }


}