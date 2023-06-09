package model;

import java.awt.*;

import interfaces.IField;
import static utils.Param.*;


public class Player extends Field implements IField{
    public Player(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(playerColor),Type.STILL);
        previousField = new Floor(height,width,positionx,positiony,fieldHeight,fieldWidth);
    }

    @Override
    public void draw(Graphics g){ super.draw(g); }

}