package engine;

public class Player extends MapObject {
    private int x, y;
    public Player(int y, int x) {
        super(false, false);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
}
