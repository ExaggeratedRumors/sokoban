package model;

import java.awt.*;
import interfaces.IField;
import static utils.Param.*;

public class Wall extends Field implements IField{
    public Wall(int height, int width, double positionx, double positiony, double fieldHeight, double fieldWidth) {
        super(height,width,positionx,positiony,fieldHeight,fieldWidth, new Color(wallColor),Type.COLLISION);
        previousField = null;
    }

    @Override
    public void draw(Graphics g){ super.draw(g); }
}