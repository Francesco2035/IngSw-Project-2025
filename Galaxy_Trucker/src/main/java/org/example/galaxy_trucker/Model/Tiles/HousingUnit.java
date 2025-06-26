package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.util.ArrayList;

public abstract class HousingUnit extends Component{


    private int x;
    private int y;
    private ArrayList<HousingUnit> nearbyHousingUnits = new ArrayList<>();
    private ArrayList<HousingUnit> unitsListeners = new ArrayList<>();

    public abstract int getNumHumans();
    public abstract void setNumHumans(int numHumans);

    public abstract boolean isPurpleAlien();
    public abstract void setPurpleAlien(boolean purpleAlien);
    public abstract boolean isBrownAlien();
    public abstract void setBrownAlien(boolean brownAlien);

    public abstract boolean isNearPurpleAddon();
    public abstract void setNearPurpleAddon(boolean nearPurpleAddon);
    public abstract boolean isNearBrownAddon();
    public abstract void setNearBrownAddon(boolean nearBrownAddon);




    @Override
    public void rotate(Boolean direction){}

    public ArrayList<HousingUnit> getNearbyHousingUnits() {
        return nearbyHousingUnits;
    }

//    public ArrayList<HousingUnit> getUnitsListeners() {
//        return unitsListeners;
//    }
//
//    public void setUnitsListeners(HousingUnit unitListener) {
//        unitsListeners.add(unitListener);
//    }

    //true addcrew
    public abstract void notifyUnit(boolean type, HousingUnit unit);

    public abstract int kill();

    public abstract void accept(ComponentAction visitor, PlayerState State);

    public abstract void addCrew(int humans, boolean purple, boolean brown);

    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        //System.out.println("HousingUnit insert called, i'm here: " + x + ", " + y);
        //playerBoard.getHousingUnits().remove(this);
        playerBoard.getHousingUnits().add(this);
        playerBoard.setNumHumans(getNumHumans());
        this.x = x;
        this.y = y;
        tile.sendUpdates(null,getNumHumans(),isPurpleAlien(),isBrownAlien(),0);
    }

    @Override
    public void remove(PlayerBoard playerBoard)  {
        playerBoard.getHousingUnits().remove(this);
        playerBoard.getConnectedHousingUnits().remove(this);
        playerBoard.setNumHumans(-getNumHumans());
        if (isBrownAlien()){
            playerBoard.setBrownAlien(false);
        }
        if (isPurpleAlien()){
            playerBoard.setPurpleAlien(false);
        }
        for (HousingUnit unit : nearbyHousingUnits){
            unit.notifyUnit(false, this);
        }
        tile.sendUpdates();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public boolean isPopulated(){
        return getNumHumans() > 0 || isBrownAlien() || isPurpleAlien();
    }

    public void checkNearbyUnits(PlayerBoard pb){
        if(getNumHumans() > 0 || isBrownAlien() || isPurpleAlien()){
            int x = getX();
            int y = getY();
            int[][] validPlayerBoard = pb.getValidPlayerBoard();
            int index = 0;
            pb.getConnectedHousingUnits().remove(this);
            ArrayList<HousingUnit> nearbyunits = getNearbyHousingUnits();

            if (validPlayerBoard[x-1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(1), pb.getTile(x-1,y).getConnectors().get(3)) && pb.getHousingUnits().contains(pb.getTile(x-1,y).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x-1,y).getComponent()))));
                }
            }
            if (validPlayerBoard[x+1][y] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(3), pb.getTile(x+1,y).getConnectors().get(1)) && pb.getHousingUnits().contains( pb.getTile(x+1,y).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x+1,y).getComponent()))));
                }
            }
            if (validPlayerBoard[x][y-1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(0), pb.getTile(x,y-1).getConnectors().get(2)) && pb.getHousingUnits().contains( pb.getTile(x,y-1).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y-1).getComponent()))));
                }
            }
            if (y < 9 && validPlayerBoard[x][y+1] == 1  && pb.checkConnection(pb.getTile(x,y).getConnectors().get(2), pb.getTile(x,y+1).getConnectors().get(0)) && pb.getHousingUnits().contains( pb.getTile(x,y+1).getComponent())) {
                if (pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent()))).isPopulated()){
                    nearbyunits.add(pb.getHousingUnits().get(pb.getHousingUnits().indexOf((pb.getTile(x,y+1).getComponent()))));
                }
            }

            if(!nearbyunits.isEmpty()){
                pb.getConnectedHousingUnits().remove(this);
                pb.getConnectedHousingUnits().add(this);
            }
        }
    }

}
