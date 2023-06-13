package communication;

import java.io.Serializable;

public class DTO implements Serializable {
    byte[] map;
    char command;

    public DTO(byte[] map) {
        this.map = map;
        this.command = '0';
    }

    public DTO(char cmd) {
        this.map = new byte[2];
        map[0] = 1;
        map[1] = 25;
        this.command = cmd;
    }

    public byte[] getMap() { return map; }
    public char getCommand() { return command; }
}
