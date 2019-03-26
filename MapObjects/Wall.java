package MapObjects;

import java.awt.*;

import Interface.IField;
import static Config.Param.*;

/**
 * Klasa ściany, na którą gracz nie może się przemieścić
 */
public class Wall extends Field implements IField{

    /**
     * Konstruktor
     * @param height wysokość okna gry
     * @param width szerokość okna gry
     * @param positionx pozycja X na mapie
     * @param positiony pozycja Y na mapie
     * @param fieldHeight wysokość pola
     * @param fieldWidth szerokość pola
     */
    public Wall(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(wallColor),Type.COLLISION);
        previousField = null;
    }

    /**
     * Metoda rysująca model obiektu
     * @param g parametr graficzny
     */
    @Override
    public void draw(Graphics g){ super.draw(g); }

}