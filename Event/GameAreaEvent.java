package Event;

public class GameAreaEvent {

    private String command;
    private int pool;
    private int value;
    public GameAreaEvent(String command, int pool, int value){
        this.command=command;
        this.pool=pool;
        this.value=value;
    }
    public String getCommand(){
        return command;
    }
    public int getPool() {
        return pool;
    }
    public int getValue() {
        return value;
    }
}


