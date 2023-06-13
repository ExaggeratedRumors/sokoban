package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import event.MoveEvent;
import interfaces.IGameArea;
import interfaces.IMove;
import model.Field;
import model.Player;

public class Move implements KeyListener, IMove{
    private int xPosition;
    private int yPosition;
    private final Player player;
    private Field[][] fields;
    private Direction direction;
    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        STAY
    }
    private final ArrayList<IGameArea> listeners;

    public Move(int x, int y, Player player) {
        listeners = new ArrayList<>();
        xPosition = x;
        yPosition = y;
        this.player = player;
    }

    public void setFields(Field[][] fields){
        this.fields = fields;
    }

    @Override
    public void keyTyped(KeyEvent event){}

    @Override
    public void keyReleased(KeyEvent event){
        int keyCode = event.getKeyCode();
        if(
            keyCode == KeyEvent.VK_UP ||keyCode == KeyEvent.VK_DOWN ||
            keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT
        ) direction = Direction.STAY;
    }

   @Override
    public void keyPressed(KeyEvent event) {
       int keyCode = event.getKeyCode();
       if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
           direction = Direction.UP;
           getMove('w');
       }
       if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
           direction = Direction.DOWN;
           getMove('s');
       }
       if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
           direction = Direction.LEFT;
           getMove('a');
       }
       if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
           direction = Direction.RIGHT;
           getMove('d');
       }
       if (keyCode == KeyEvent.VK_SPACE) {
           notify(new MoveEvent("Pause",'p', null,false));
       }
       if(keyCode == KeyEvent.VK_X){
           notify(new MoveEvent("Reset",'x', null,false));
       }
       if(keyCode == KeyEvent.VK_Z){
           checkPullBox();
       }
   }

    public int[] setOffset()
    {
        int[] offsetArray = {0,0};
        if(direction == Direction.UP) offsetArray[1] = -1;
        if(direction == Direction.DOWN) offsetArray[1] = 1;
        if(direction == Direction.LEFT) offsetArray[0] = -1;
        if(direction == Direction.RIGHT) offsetArray[0] = 1;
        return offsetArray;
    }

    public void checkPullBox() {
        int surrounding = 0;
        int[] offsetArray = {0,0};
            if(fields[yPosition+1][xPosition].getType()== Field.Type.MOVING) {
                offsetArray[1] = -1;
                surrounding++;
            }
            if(fields[yPosition-1][xPosition].getType()== Field.Type.MOVING) {
                offsetArray[1] = 1;
                surrounding++;
            }
            if(fields[yPosition][xPosition+1].getType()== Field.Type.MOVING) {
                offsetArray[0] = -1;
                offsetArray[1] = 0;
                surrounding++;
            }
            if(fields[yPosition][xPosition-1].getType()== Field.Type.MOVING) {
                offsetArray[0] = 1;
                offsetArray[1] = 0;
                surrounding++;
            }
        if(xPosition + offsetArray[0] >= 0 && yPosition + offsetArray[1] >= 0){
            if (fields[yPosition+offsetArray[1]][xPosition+offsetArray[0]].getType()==Field.Type.STILL && surrounding==1){
                notify(new MoveEvent("Pull",'z', offsetArray,false));
            }
        }
    }

    public void getMove(char code) {
        int[] offsetArray = setOffset();
        if(fields == null) return;
        if(xPosition + offsetArray[0] >= 0 && yPosition + offsetArray[1] >= 0){
            if (fields[yPosition+offsetArray[1]][xPosition+offsetArray[0]].getType() == Field.Type.STILL){
                notify(new MoveEvent("Move",code, offsetArray,false));
            }
            else if(fields[yPosition+offsetArray[1]][xPosition+offsetArray[0]].getType() == Field.Type.MOVING) {
                if(fields[yPosition+2*offsetArray[1]][xPosition+2*offsetArray[0]].getType() == Field.Type.STILL){
                    notify(new MoveEvent("Move", code, offsetArray,true));
                }
            }
        }
    }

    public void setMove(int[] offsetArray, boolean collision, boolean reverse){
        if(!collision && !reverse){
            fields[yPosition][xPosition] = player.getPreviousField();
            fields[yPosition][xPosition].setPosition(player.getPositionX(), player.getPositionY());
            player.setPosition(player.getPositionX() + offsetArray[0] * player.getFieldWidth(), player.getPositionY() + offsetArray[1] * player.getFieldWidth());
            yPosition += offsetArray[1];
            xPosition += offsetArray[0];
            player.setPreviousField(fields[yPosition][xPosition]);
            fields[yPosition][xPosition] = player;
        }
        else if(collision && !reverse){
            double tempX,tempY,tempWidth;
            tempX = player.getPositionX();
            tempY = player.getPositionY();
            tempWidth = player.getFieldWidth();
            player.setPosition(tempX + offsetArray[0]*tempWidth, tempY + offsetArray[1]*tempWidth);
            fields[yPosition][xPosition] = player.getPreviousField();
            fields[yPosition][xPosition].setPosition(tempX, tempY);
            Field tempChest = fields[yPosition+offsetArray[1]][xPosition+offsetArray[0]];
            player.setPreviousField(tempChest.getPreviousField());
            tempChest.setPosition(tempX + 2*offsetArray[0]*tempWidth, tempY + 2*offsetArray[1]*tempWidth);
            tempChest.setPreviousField(fields[yPosition+2*offsetArray[1]][xPosition+2*offsetArray[0]]);
            if(tempChest.getPreviousField().isCheckPoint()) tempChest.setColor(tempChest.getCheckpointColor());
            else tempChest.setColor(tempChest.getChestColor());
            fields[yPosition+2*offsetArray[1]][xPosition+2*offsetArray[0]] = tempChest;
            yPosition+=offsetArray[1];
            xPosition+=offsetArray[0];
            fields[yPosition][xPosition] = player;
        }
        else {
            double tempX,tempY,tempWidth;
            tempX = player.getPositionX();
            tempY = player.getPositionY();
            tempWidth = player.getFieldWidth();
            Field tempChest = fields[yPosition-offsetArray[1]][xPosition-offsetArray[0]];
            tempChest.setPosition(tempX,tempY );
            fields[yPosition-offsetArray[1]][xPosition-offsetArray[0]] = tempChest.getPreviousField();
            fields[yPosition-offsetArray[1]][xPosition-offsetArray[0]].setPosition(tempX-offsetArray[0]*tempWidth,tempY-offsetArray[1]*tempWidth);
            tempChest.setPreviousField(player.getPreviousField());
            player.setPosition(tempX + offsetArray[0]*tempWidth, tempY + offsetArray[1]*tempWidth);
            player.setPreviousField(fields[yPosition+offsetArray[1]][xPosition+offsetArray[0]]);
            fields[yPosition][xPosition] = tempChest;
            if(tempChest.getPreviousField().isCheckPoint()) tempChest.setColor(tempChest.getCheckpointColor());
            else tempChest.setColor(tempChest.getChestColor());
            yPosition+=offsetArray[1];
            xPosition+=offsetArray[0];
            fields[yPosition][xPosition] = player;
        }
    }

    public void animation(int[] direction, boolean collision, boolean reverse){
        player.setOffset(direction[0],direction[1]);
        fields[yPosition][xPosition]=player;

        if((direction[0]==1 || direction[1]==1) && !collision){
            Field temp = fields[yPosition+direction[1]][xPosition+direction[0]];
            temp.changeSize(99*temp.getField().getX()-direction[0]*temp.getField().getWidth(),99*temp.getField().getY()-direction[1]*temp.getField().getHeight(),temp.getField().getWidth()*direction[0],temp.getField().getHeight());
            fields[yPosition+direction[1]][xPosition+direction[0]]=temp;
        }
        if(collision){
            fields[yPosition+direction[1]][xPosition+direction[0]].setOffset(direction[0],direction[1]);
            Field temp = fields[yPosition+2*direction[1]][xPosition+2*direction[0]];
            temp.changeSize(99*temp.getField().getX()-2*direction[0]*temp.getField().getWidth(),99*temp.getField().getY()-2*direction[1]*temp.getField().getHeight(),temp.getField().getWidth()*2*direction[0]*2,temp.getField().getHeight()*2*direction[1]*2);
            fields[yPosition+2*direction[1]][xPosition+2*direction[0]]=temp;
        }
        if(reverse){
            fields[yPosition-direction[1]][xPosition-direction[0]].setOffset(direction[0],direction[1]);
        }
    }

    public void refreshPosition(int panelWidth,int panelHeight){
        if(fields == null) return;
        for(Field[] fieldArray : fields)
        {
            for(Field field: fieldArray){
                field.defaultDimensions(panelWidth,panelHeight);
            }
        }
    }

    public Field[][] getFields(){ return fields; }

    @Override
    public void add(IGameArea listener){listeners.add(listener);}

    @Override
    public void remove(IGameArea listener){ listeners.remove(listener); }

    @Override
    public void notify(MoveEvent event){
        for(IGameArea listener : listeners){
            listener.moveEvent(event);
        }
    }
}
