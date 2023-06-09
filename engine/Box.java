package engine;

public class Box {
    private boolean checked;
    private int x, y;
    public Box(int y, int x) {
        this.x = x;
        this.y = y;
        checked = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public boolean matchPosition(int x, int y) {
        return this.x == x && this.y == y;
    }
}
