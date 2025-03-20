package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Boards.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

import java.util.ArrayList;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "componentType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = plasmaDrill.class, name = "plasmaDrill"),
        @JsonSubTypes.Type(value = hotWaterHeater.class, name = "hotWaterHeater"),
        @JsonSubTypes.Type(value = powerCenter.class, name = "powerCenter"),
        @JsonSubTypes.Type(value = storageCompartment.class, name = "storageCompartment"),
        @JsonSubTypes.Type(value = modularHousingUnit.class, name = "modularHousingUnit"),
        @JsonSubTypes.Type(value = sewerPipes.class, name = "sewerPipes"),
        @JsonSubTypes.Type(value = specialStorageCompartment.class, name = "specialStorageCompartment"),
        @JsonSubTypes.Type(value = alienAddons.class, name = "alienAddons"),
        @JsonSubTypes.Type(value = shieldGenerator.class, name = "shieldGenerator"),
        @JsonSubTypes.Type(value = spaceVoid.class, name = "spaceVoid")
})
public abstract class Component {

    PlayerBoard myBoard;
    String type;


    public Component() {}


    public void referencePlance(PlayerBoard myBoard) {
        this.myBoard = myBoard;
    }


    //initType
    public void initType(){}
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {}


    //getAbility
    public int getAbility(){
        return 0;
    }
    public ArrayList<Goods> getAbility(Goods good){return null;}
    public ArrayList<Integer> getAbility(int integer){return null;}


    //setAbility
    public int setAbility(){return 0;}      //volendo si può in powerCenter utilizzare l'altro setAbility e semplicemente non utilizzare l'int di ingresso, ma così è visivamente più puliti
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien){
        return 0;
    }
    public int setAbility(Goods good, boolean select){return 0;}
    public void setAbility(boolean direzione){}



//  metodi per json
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public boolean isPurpleAlien() {return false;}
    public boolean isBrownAlien() {
        return false;
    }

    public ArrayList<Goods> getGoods(){return null;}


}
