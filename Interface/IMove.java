package Interface;

import Event.MoveEvent;

public interface IMove {
    void add(IGameArea listener);
    void remove(IGameArea listener);
    void notify(MoveEvent event);

}
