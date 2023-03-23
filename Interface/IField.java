package Interface;

import Event.FieldEvent;

public interface IField {
    void add(IGameArea listener);
    void remove(IGameArea listener);
    void notify(FieldEvent event);
}
