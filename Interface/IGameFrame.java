package Interface;

import Event.GameEvent;
import Event.SideBlockEvent;

public interface IGameFrame {
    void add(IApplicationMenu listener);
    void remove(IApplicationMenu listener);
    void notify(GameEvent event);
    void sideBlockEvent(SideBlockEvent event);
}
