package event;

public record MoveEvent(String command, char code, int[] direction, boolean collision) {

}
