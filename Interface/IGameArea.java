package Interface;

import Event.FieldEvent;
import Event.GameAreaEvent;
import Event.MoveEvent;

/**
 * Interfejs GameArea
 */
public interface IGameArea {

    /**
     * Metoda dodająca słuchacza
     * @param listener
     */
    void add(ISideBlock listener);

    /**
     * Metoda usuwająca słuchacza
     * @param listener
     */

    void remove(ISideBlock listener);

    /**
     * Metoda zawiadamiająca słuchaczy o zdarzeniu
     * @param event
     */
    void notify(GameAreaEvent event);

    /**
     * Metoda obsługująca zdarzenie obiektu pola
     * @param event
     */
    void fieldEvent(FieldEvent event);

    /**
     * Metoda obsługująca zdarzenie obiektu ruchu
     * @param event
     */
    void moveEvent(MoveEvent event);

}
