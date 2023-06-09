package interfaces;

import event.GameAreaEvent;
import event.SideBlockEvent;

public interface ISideBlock {

    void add(IGameFrame listener);
    void remove(IGameFrame listener);
    void notify(SideBlockEvent event);
    void gameAreaEvent(GameAreaEvent event);

}
