package Event;

/**
 * Klasa zdarzenia obiektu pola gry
 */
public class FieldEvent {

    /**
     * Komenda zdarzenia
     */
    private String command;

    /**
     * Pobranie komendy zdarzenia
     * @param command
     */
    public FieldEvent(String command){
        this.command=command;
    }
    /**
     * Ustawienie komendy zdarzenia
     * @return
     */
    public String getCommand() {
        return command;
    }
}
