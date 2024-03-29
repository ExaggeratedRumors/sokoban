package interfaces;

import event.MoveEvent;

public interface IMove {
    void add(IGameArea listener);
    void remove(IGameArea listener);
    void notify(MoveEvent event);

}
