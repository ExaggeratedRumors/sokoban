package MapObjects;

import java.awt.*;
import java.util.ArrayList;
import Event.FieldEvent;
import Interface.IField;
import Interface.IGameArea;

public abstract class Field implements IField{
    private int panelHeight;
    private int panelWidth;
    protected Color color;
    private double positionX;
    private double positionY;
    private final double fieldHeight;
    private final double fieldWidth;
    private final Rectangle field;
    private double deltaX;
    private double deltaY;
    private final double fps;
    private final Type type;
    protected boolean checkPoint;
    protected Field previousField;
    public enum Type{
        COLLISION,
        MOVING,
        STILL
    }
    private final ArrayList<IGameArea> listeners;

    public Field(int height, int width, double positionX, double positionY, double fieldHeight, double fieldWidth, Color fieldColor, Type type) {
        fps = 0.03125;
        panelHeight = height;
        panelWidth = width;
        this.positionX = positionX;
        this.positionY = positionY;
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        color=fieldColor;
        field = new Rectangle((int)Math.round(positionX*panelWidth+0.2*panelWidth),(int)Math.round(positionY*panelHeight+0.2*panelHeight),(int)Math.round(fieldWidth*panelWidth),(int)Math.round(fieldHeight*panelHeight));
        listeners = new ArrayList<>();
        this.type=type;
        checkPoint = false;
        deltaX=field.getWidth()*fps;
        deltaY=field.getHeight()*fps;
    }

    public void setOffset(double offsetX, double offsetY) {
      field.setLocation((int)Math.round(field.getX()+offsetX*deltaX),(int)Math.round(field.getY()+offsetY*deltaY));
    }

    public void changeSize(double positionx, double positiony, double width, double height) {
        field.setLocation((int)Math.round(positionx),(int)Math.round(positiony));
        field.setSize((int)width,(int)height);
    }


    public void setPreviousField(Field newField){previousField = newField;}

    public void setColor(Color newColor){ color = newColor; }

    public void setPosition(double x, double y){
        positionY = y;
        positionX = x;
    }

    public Rectangle getField(){return field;}

    public boolean isCheckPoint(){return checkPoint;}

    public double getPositionX(){return positionX;}

    public double getPositionY(){return positionY;}


    public double getFieldWidth(){return fieldWidth;}


    public int isCorrectPosition(){ return 0; }

    public Field getPreviousField(){return previousField;}


    public Type getType(){return type;}

    public Color getChestColor(){return color;}

    public Color getCheckpointColor(){return color;}

    public Color getColor(){return color;}


    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect((int)Math.round(field.getX()),(int)Math.round(field.getY()),(int)Math.round(field.getWidth()),(int)Math.round(field.getHeight()));
        g.setColor(Color.black);
        g.drawRect((int)Math.round(field.getX()),(int)Math.round(field.getY()),(int)Math.round(field.getWidth()),(int)Math.round(field.getHeight()));
    }

    public void defaultDimensions(int width, int height) {
        panelHeight=height;
        panelWidth=width;
        deltaX=field.getWidth()*fps;
        deltaY=field.getHeight()*fps;
        field.setLocation((int)Math.round(positionX*width+0.2*width), (int)Math.round(positionY*height+0.2*height));
        field.setSize((int)Math.round(fieldWidth*width), (int)Math.round(fieldHeight*height));
    }
    @Override
    public void add(IGameArea listener) {
        listeners.add(listener);
            notify(new FieldEvent("Good Position"));
    }
    @Override
    public void remove(IGameArea listener) {
        listeners.remove(listener);
    }
    @Override
    public void notify(FieldEvent event) {
        for (IGameArea observer : listeners) {
            observer.fieldEvent(event);
        }
    }

}