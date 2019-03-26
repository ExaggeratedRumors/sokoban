package MapObjects;

import java.awt.*;

import Interface.IField;
import static Config.Param.*;

/**
 * Klasa punktu docelowego, na którego pole należy przesunąć skrzynie
 */
public class DestinationPoint extends Field implements IField{

    /**
     * Konstruktor
     * @param height wysokość okna gry
     * @param width szerokość okna gry
     * @param positionx pozycja X na mapie
     * @param positiony pozycja Y na mapie
     * @param fieldHeight wysokość pola
     * @param fieldWidth szerokość pola
     */
    public DestinationPoint(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(destinationPointColor),Type.STILL);
        previousField = null;
        checkPoint = true;
    }

    /**
     * Metoda rysująca model obiektu
     * @param g parametr graficzny
     */
    @Override
    public void draw(Graphics g){ super.draw(g); }

}