package model;

import java.awt.*;
import interfaces.IField;
import static utils.Param.*;


public class DestinationPoint extends Field implements IField{

    public DestinationPoint(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(destinationPointColor),Type.STILL);
        previousField = null;
        checkPoint = true;
    }
    @Override
    public void draw(Graphics g){ super.draw(g); }

}