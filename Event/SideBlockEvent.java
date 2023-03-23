package Event;

public class SideBlockEvent {

    private int pullNumber;
    private int resetNumber;
    private int score;
    private int mapNumber;
    private boolean success;
    private String command;

    public SideBlockEvent(int pullNumber, int resetNumber, int score, int mapNumber, boolean success, String command) {
        this.pullNumber = pullNumber;
        this.resetNumber = resetNumber;
        this.score = score;
        this.mapNumber = mapNumber;
        this.success = success;
        this.command = command;
    }

    public int getPullNumber() {
        return pullNumber;
    }
    public int getResetNumber() {
        return resetNumber;
    }
    public int getScore() {
        return score;
    }
    public int getMapNumber(){ return mapNumber; }
    public boolean getSuccess() {
        return success;
    }
    public String getCommand() {
        return command;
    }

}
