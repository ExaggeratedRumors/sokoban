package Event;

/**
 * Klasa zdarzenia wewnątrz planszy gry
 */
public class GameAreaEvent {

    /**
     * Komenda
     */
    private String command;
    /**
     * Liczba punktów
     */
    private int pool;
    /**
     * Wybrana wartość
     */
    private int value;

    /**
     * Konstruktor
     * @param command
     * @param pool
     */
    public GameAreaEvent(String command, int pool, int value){
        this.command=command;
        this.pool=pool;
        this.value=value;
    }

    /**
     * Pobranie komendy zdarzenia
     * @return komenda
     */
    public String getCommand(){
        return command;
    }

    /**
     * Pobranie puli punktów
     * @return pool
     */
    public int getPool() {
        return pool;
    }

    /**
     * Pobranie wartości
     * @return value
     */
    public int getValue() {
        return value;
    }
}


