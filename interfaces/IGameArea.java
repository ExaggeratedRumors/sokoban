package interfaces;

import event.GameAreaEvent;
import event.MoveEvent;

public interface IGameArea {
    void add(ISideBlock listener);
    void remove(ISideBlock listener);
    void notify(GameAreaEvent event);
    void moveEvent(MoveEvent event);
}
