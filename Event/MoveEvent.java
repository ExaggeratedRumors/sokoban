package Event;

/**
 * Klasa zdarzenia ruchu
 */
public class MoveEvent {
    /**
     * komenda zdarzenia
     */
    private String command;
    /**
     * Tablica ruchu gracza (x,y)
     */
    private int[] direction;
    /**
     * Typ boolowski sprawdzania kolizji
     */
    private boolean collision;
    /**
     * Konstruktor
     * @param command
     */
    public MoveEvent(String command, int[] direction, boolean collision){
        this.command=command;
        this.direction=direction;
        this.collision=collision;
    }

    /**
     * Pobranie komendy zdarzenia
     * @return komenda
     */
    public String getCommand(){
        return command;
    }

    /**
     * Pobieranie kierunku ruchu
     * @return tablica ruchu gracza
     */
    public int[] getDirection(){
        return direction;
    }

    /**
     * Sprawdzanie czy obiekt jest kolizją (skrzynia)
     * @return typ boolowski
     */
    public boolean isCollision(){
        return collision;
    }

}
