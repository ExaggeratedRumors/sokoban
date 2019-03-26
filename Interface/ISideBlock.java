package Interface;

import Event.GameAreaEvent;
import Event.SideBlockEvent;

/**
 * Interfejs obiektów panelu bocznego gry
 */
public interface ISideBlock {

    /**
     * Metoda dodajaca słuchacza
     * @param listener
     */
    void add(IGameFrame listener);

    /**
     * Metoda usuwająca słuchacza
     * @param listener
     */
    void remove(IGameFrame listener);

    /**
     * Metoda zawiadamiająca słuchaczy o zajściu zdarzenia
     * @param event
     */
    void notify(SideBlockEvent event);

    /**
     * Metoda obsługująca zdarzenie obiektu pola gry
     * @param event
     */
    void gameAreaEvent(GameAreaEvent event);

}
