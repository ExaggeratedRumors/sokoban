package interfaces;

import event.GameEvent;
import event.SideBlockEvent;

public interface IGameFrame {
    void add(IApplicationMenu listener);
    void remove(IApplicationMenu listener);
    void notify(GameEvent event);
    void sideBlockEvent(SideBlockEvent event);
}
