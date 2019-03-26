package Event;

/**
 * Klasa zdarzenia okna gry
 */
public class GameEvent {
    /**
     * Komenda zdarzenia
     */
    private String command;
    /**
     * Nazwa gracza
     */
    private String nickName;
    /**
     * Przechowywanie wyniku
     */
    private int score;

    /**
     * Konstruktor eventu gry
     * @param command komenda
     * @param score wynik
     */
    public GameEvent(String command, String nickName, int score){
        this.command=command;
        this.nickName=nickName;
        this.score=score;
    }

    /**
     * Pobranie komendy
     * @return komenda
     */
    public String getCommand() {
        return command;
    }

    /**
     * Pobiera nazwę gracza
     * @return nickname
     */
    public String getNickName(){ return nickName; }

    /**
     * Pobiera liczbę punków
     * @return score
     */
    public int getScore() {
        return score;
    }
}
