package Interface;

import Event.FieldEvent;
import Event.GameAreaEvent;
import Event.MoveEvent;

public interface IGameArea {
    void add(ISideBlock listener);
    void remove(ISideBlock listener);
    void notify(GameAreaEvent event);
    void fieldEvent(FieldEvent event);
    void moveEvent(MoveEvent event);

}
