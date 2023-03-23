package Event;

public class GameEvent {

    private String command;
    private String nickName;
    private int score;
    public GameEvent(String command, String nickName, int score){
        this.command=command;
        this.nickName=nickName;
        this.score=score;
    }
    public String getCommand() {
        return command;
    }
    public String getNickName(){ return nickName; }
    public int getScore() {
        return score;
    }
}
