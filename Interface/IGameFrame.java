package Interface;

import Event.GameEvent;
import Event.SideBlockEvent;

/**
 * Interfejs ramki gry
 */
public interface IGameFrame {

    /**
     * Metoda dodajaca słuchacza
     * @param listener
     */
    void add(IApplicationMenu listener);

    /**
     * Metoda usuwająca słuchacza
     * @param listener
     */
    void remove(IApplicationMenu listener);

    /**
     * Metoda zawiadamiająca słuchaczy o zajściu zdarzenia
     * @param event
     */
    void notify(GameEvent event);

    /**
     * Metoda obsługująca zdarzenie obiektu pola panelu bocznego
     * @param event
     */
    void sideBlockEvent(SideBlockEvent event);
}
