package Interface;

import Event.GameAreaEvent;
import Event.SideBlockEvent;

public interface ISideBlock {

    void add(IGameFrame listener);
    void remove(IGameFrame listener);
    void notify(SideBlockEvent event);
    void gameAreaEvent(GameAreaEvent event);

}
