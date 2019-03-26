package Interface;

import Event.FieldEvent;

/**
 * Interfejs pojedynczego pola
 */
public interface IField {

    /**
     * Metoda dodająca słuchacza
     * @param listener
     */
    void add(IGameArea listener);

    /**
     * Metoda usuwająca słuchacza
     * @param listener
     */
    void remove(IGameArea listener);

    /**
     * Metoda zawiadamiająca słuchaczy o zdarzeniu
     * @param event
     */
    void notify(FieldEvent event);
}
