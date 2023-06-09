package engine;

public class MapObject {
    private final boolean isCollision;
    private final boolean isDestination;

    public MapObject(boolean isCollision, boolean isDestination) {
        this.isCollision = isCollision;
        this.isDestination = isDestination;
    }

    public boolean isCollision() {
        return isCollision;
    }

    public boolean isDestination() {
        return isDestination;
    }
}
