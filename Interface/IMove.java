package Interface;

import Event.MoveEvent;

/**
 * Interfejs obiektów odpowiedzialnych za ruch
 */
public interface IMove {

    /**
     * Metoda dodajaca słuchacza
     * @param listener
     */
    void add(IGameArea listener);

    /**
     * Metoda usuwająca słuchacza
     * @param listener
     */
    void remove(IGameArea listener);

    /**
     * Metoda zawiadamiająca słuchaczy o zajściu zdarzenia
     * @param event
     */
    void notify(MoveEvent event);

}
