package interfaces;

import event.FieldEvent;
import event.GameAreaEvent;
import event.MoveEvent;

public interface IGameArea {
    void add(ISideBlock listener);
    void remove(ISideBlock listener);
    void notify(GameAreaEvent event);
    void fieldEvent(FieldEvent event);
    void moveEvent(MoveEvent event);

}
