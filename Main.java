/**
 * Created by Dominik Gajda & Konrad Polański
 */

import GameModel.ApplicationMenu;

/**
 * Klasa Main odpowiedzialna za uruchomienie programu
 */
public class Main {

    /**
     * Utworzenie połączenia z serwerem i otworzenie programu.
     * @param args tablica argumentów
     */
    public static void main(String[] args) {
        ApplicationMenu application = new ApplicationMenu();
        application.runMenu();
    }
}