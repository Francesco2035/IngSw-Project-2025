package org.example.galaxy_trucker;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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

    public Component() {;
    }

    public void referencePlance(PlayerBoard myBoard) {
        this.myBoard = myBoard;
    }

    public void initType(){}
    public int getAbility(){
        return 0;
    }
    public ArrayList<Goods> getAbility(Goods good){return null;}
    public int setAbility(){return 0;}      //volendo si può in powerCenter utilizzare l'altro setAbility e semplicemente non utilizzare l'int di input, ma così è visivamente più puliti
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien){
        return 0;
    }
    public int setAbility(Goods good){return 0;}
    public int unSetAbility(Goods good){return 0;}





//  metodi per json
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public int getNumHumans() {
        return -100000;
    }

}
