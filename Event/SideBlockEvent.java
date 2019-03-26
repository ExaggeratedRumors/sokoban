package Event;

/**
 * Klasa zdarzenia panelu bocznego
 */
public class SideBlockEvent {
    /**
     * Liczba pozostałych prób pociągnięć bloków
     */
    private int pullNumber;
    /**
     * Liczba pozostałych prób zresetowania planszy
     */
    private int resetNumber;
    /**
     * Wynik momentu gry
     */
    private int score;
    /**
     * Numer mapy
     */
    private int mapNumber;
    /**
     * Typ boolowski określający czy zostały ukończone wszystkie mapy
     */
    private boolean success;
    /**
     * Komenda zdarzenia
     */
    private String command;

    /**
     * Kontruktor eventu
     * @param pullNumber liczba prób pociągnięć bloków
     * @param resetNumber liczba pozostałych prób zresetowania planszy
     * @param score wynik
     * @param success ukończenie wszystkich map
     * @param command komenda zdarzenia
     */
    public SideBlockEvent(int pullNumber, int resetNumber, int score, int mapNumber, boolean success, String command) {
        this.pullNumber = pullNumber;
        this.resetNumber = resetNumber;
        this.score = score;
        this.mapNumber = mapNumber;
        this.success = success;
        this.command = command;
    }

    /**
     * Pobranie liczby prób pociągnięć bloków
     * @return liczba pozostałych prób pociągnieć bloków
     */
    public int getPullNumber() {
        return pullNumber;
    }

    /**
     * Pobranie liczby resetów planszy
     * @return liczba resetów
     */
    public int getResetNumber() {
        return resetNumber;
    }

    /**
     * Pobranie liczby punktów
     * @return wynik
     */
    public int getScore() {
        return score;
    }

    /**
     * Pobranie liczby punktów
     * @return wynik
     */
    public int getMapNumber(){ return mapNumber; }

    /**
     * Pobranie sukcesu ukończenia gry
     * @return typ boolowski ukończenia wszystkich map
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Pobranie komendy zdarzenia
     * @return komenda zdarzenia
     */
    public String getCommand() {
        return command;
    }

}
