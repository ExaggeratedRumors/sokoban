package event;

public record MoveEvent(String command, int[] direction, boolean collision) {

}
