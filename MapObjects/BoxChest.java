package MapObjects;

import java.awt.*;
import Interface.IField;
import static Config.Param.*;

public class BoxChest extends Field implements IField{

    private final Color chestColor = new Color(boxColor);
    private final Color checkpointColor = new Color(26316);
    public BoxChest(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(boxColor),Type.MOVING);
        previousField = new Floor(height,width,positionx,positiony,fieldHeight,fieldWidth);
    }
    @Override
    public int isCorrectPosition(){
        if(previousField.isCheckPoint()) return 0;
        else return 1;
    }
    @Override
    public Color getChestColor(){return chestColor;}
    @Override
    public Color getCheckpointColor(){return checkpointColor;}
    @Override
    public void draw(Graphics g){
        super.draw(g);
    }
}