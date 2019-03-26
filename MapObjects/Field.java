package MapObjects;

import java.awt.*;
import java.util.ArrayList;

import Event.FieldEvent;
import Interface.IField;
import Interface.IGameArea;


/**
 * Klasa abstrakcyjna pola na planszy
 */
public abstract class Field implements IField{
    /**
     * Wysokość okna gry
     */
    private int panelHeight;
    /**
     * Szerokośc okna gry
     */
    private int panelWidth;
    /**
     * Kolor pola
     */
    protected Color color;
    /**
     * Pozycja pola w osi X
     */
    private double positionX;
    /**
     * Pozycja pola w osi Y
     */
    private double positionY;
    /**
     * Wysokość pola
     */
    private double fieldHeight;
    /**
     * Szerokość pola
     */
    private double fieldWidth;
    /**
     * Kształt obiektu pola
     */
    private Rectangle field;
    /**
     * Przesunięcie obiektu w osi X
     */
    private double deltaX;
    /**
     * Przesunięcie obiektu w osi Y
     */
    private double deltaY;
    /**
     * Liczba klatek animacji
     */
    private double fps;
    /**
     * Typ pola
     */
    private Type type;
    /**
     * Pole będące punktem docelowym
     */
    protected boolean checkPoint;
    /**
     * Obiekt pola zasłanianego przez pole obiektu klasy
     */
    protected Field previousField;
    /**
     * Typ pola: Kolizja (ściana), Poruszające się (skrzynie), Stały (podłoga, punkt docelowy)
     */
    public enum Type{
        COLLISION,
        MOVING,
        STILL
    }
    /**
     * Lista słuchaczy
     */
    private ArrayList<IGameArea> listeners;

    /**
     * Konstruktor klasy pola planszy
     * @param height wysokość planszy gry
     * @param width szerokość planszy gry
     * @param positionX pozycja w osi horyzontalnej
     * @param positionY pozycja w osi wertykalnej
     * @param fieldHeight wysokość obiektu
     * @param fieldWidth szerokość obiektu
     * @param fieldColor kolor obiektu
     * @param type typ pola
     */
    public Field(int height, int width, double positionX, double positionY, double fieldHeight, double fieldWidth, Color fieldColor, Type type) {
        fps = 0.03125;
        panelHeight = height;
        panelWidth = width;
        this.positionX=1.0*positionX;
        this.positionY=1.0*positionY;
        this.fieldHeight=1.0*fieldHeight;
        this.fieldWidth=1.0*fieldWidth;
        color=fieldColor;
        field = new Rectangle((int)Math.round(positionX*panelWidth+0.2*panelWidth),(int)Math.round(positionY*panelHeight+0.2*panelHeight),(int)Math.round(fieldWidth*panelWidth),(int)Math.round(fieldHeight*panelHeight));
        listeners = new ArrayList<>();
        this.type=type;
        checkPoint = false;
        deltaX=field.getWidth()*fps;
        deltaY=field.getHeight()*fps;
    }

    /**
     * Metoda zmieniająca lokalizację pola na mapie
     * @param offsetX przesunięcie w osi X
     * @param offsetY przesunięcie w osi Y
     */
    public void setOffset(double offsetX, double offsetY) {
      field.setLocation((int)Math.round(field.getX()+offsetX*deltaX),(int)Math.round(field.getY()+offsetY*deltaY));
    }

    /**
     * Metoda zmieniająca rozmiar pola
     * @param positionx pozycja w osi X
     * @param positiony pozycja w osi Y
     * @param width szerokość pola
     * @param height wysokość pola
     */
    public void changeSize(double positionx, double positiony, double width, double height) {
        field.setLocation((int)Math.round(positionx),(int)Math.round(positiony));
        field.setSize((int)width,(int)height);
    }

    /**
     * Metoda ustawiająca zasłonięte pole
     * @param newField zasłonięte pole
     */
    public void setPreviousField(Field newField){previousField = newField;}

    /**
     * Metoda ustawiająca kolor pola
     * @param newColor
     */
    public void setColor(Color newColor){ color = newColor; }

    /**
     * Metoda ustawiająca pozycję pola
     * @param x pozycja w osi X
     * @param y pozycja w osi Y
     */
    public void setPosition(double x, double y){
        positionY = y;
        positionX = x;
    }

    /**
     * Pobranie obiektu kształtu pola
     * @return obiekt kształtu pola
     */
    public Rectangle getField(){return field;}

    /**
     * Sprawdzanie czy pole jest miejscem docelowym
     * @return czy pole jest polem docelowym
     */
    public boolean isCheckPoint(){return checkPoint;}

    /**
     * Metoda pobierająca pozycję w osi X
     * @return pozycja w osi X
     */
    public double getPositionX(){return positionX;}

    /**
     * Metoda pobierająca pozycję w osi Y
     * @return pozycja w osi Y
     */
    public double getPositionY(){return positionY;}

    /**
     * Metoda pobierająca szerokość pola
     * @return szerokość pola
     */
    public double getFieldWidth(){return fieldWidth;}

    /**
     * Metoda pobierająca wysokość pola
     * @return wysokość pola
     */
    public double getFieldHeight(){return fieldHeight;}

    /**
     * Metoda sprawdzająca czy obiekt ruchomy jest we właściwym miejscu
     */
    public int isCorrectPosition(){ return 0; }

    /**
     * Metoda pobierająca zasłonięte pole
     * @return obiekt zasłoniętego pola
     */
    public Field getPreviousField(){return previousField;}

    /**
     * Metoda pobierająca typ obiektu
     * @return
     */
    public Type getType(){return type;}

    /**
     * Metoda pobierająca kolor pola
     * @return
     */
    public Color getChestColor(){return color;}

    /**
     * Metoda pobierająca kolor pola
     * @return
     */
    public Color getCheckpointColor(){return color;}

    /**
     * Metoda pobierająca kolor pola
     * @return kolor pola
     */
    public Color getColor(){return color;}

    /**
     * Metoda rysująca kształt i wypełnienie obiektu
     * @param g parametr graficzny
     */
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect((int)Math.round(field.getX()),(int)Math.round(field.getY()),(int)Math.round(field.getWidth()),(int)Math.round(field.getHeight()));
        g.setColor(Color.black);
        g.drawRect((int)Math.round(field.getX()),(int)Math.round(field.getY()),(int)Math.round(field.getWidth()),(int)Math.round(field.getHeight()));
    }

    /**
     * Metoda ustawiająca domyślne wymiary pola
     * @param height wysokość ekranu gry
     * @param width szerokość ekranu gry
     */
    public void defaultDimensions(int width, int height) {
        panelHeight=height;
        panelWidth=width;
        deltaX=field.getWidth()*fps;
        deltaY=field.getHeight()*fps;
        field.setLocation((int)Math.round(positionX*width+0.2*width), (int)Math.round(positionY*height+0.2*height));
        field.setSize((int)Math.round(fieldWidth*width), (int)Math.round(fieldHeight*height));
    }

    /**
     * Metoda dodająca słuchacza
     * @param listener słuchacz
     */
    @Override
    public void add(IGameArea listener) {
        listeners.add(listener);
            notify(new FieldEvent("Good Position"));
    }

    /**
     * Metoda usuwająca słuchacza
     * @param listener słuchacz
     */
    @Override
    public void remove(IGameArea listener) {
        listeners.remove(listeners.indexOf(listener));
    }

    /**
     * Metoda zawiadamiająca słuchaczy o zdarzeniu
     * @param event zdarzenie
     */
    @Override
    public void notify(FieldEvent event) {
        for (IGameArea observer : listeners) {
            observer.fieldEvent(event);
        }
    }

}