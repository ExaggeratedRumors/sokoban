package Event;

public class MoveEvent {
    private String command;
    private int[] direction;
    private boolean collision;
    public MoveEvent(String command, int[] direction, boolean collision){
        this.command=command;
        this.direction=direction;
        this.collision=collision;
    }

    public String getCommand(){
        return command;
    }
    public int[] getDirection(){
        return direction;
    }
    public boolean isCollision(){
        return collision;
    }

}
