package event;

public record SideBlockEvent(int pullNumber,
                             int resetNumber,
                             int score,
                             int mapNumber,
                             boolean success,
                             String command) {
}
