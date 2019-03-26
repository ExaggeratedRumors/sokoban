package Interface;

import Event.GameEvent;

/**
 * Interfejs okna menu
 */
public interface IApplicationMenu {

    /**
     * Obsługa zdarzenia po zakończeniu gry
     * @param event Obiekt zdarzenia
     */
    void applicationMenuEvent(GameEvent event);
}
