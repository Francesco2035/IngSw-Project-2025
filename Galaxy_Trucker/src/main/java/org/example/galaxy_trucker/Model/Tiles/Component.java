package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "componentType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlasmaDrill.class, name = "plasmaDrill"),
        @JsonSubTypes.Type(value = HotWaterHeater.class, name = "hotWaterHeater"),
        @JsonSubTypes.Type(value = PowerCenter.class, name = "powerCenter"),
        @JsonSubTypes.Type(value = StorageCompartment.class, name = "storageCompartment"),
        @JsonSubTypes.Type(value = ModularHousingUnit.class, name = "modularHousingUnit"),
        @JsonSubTypes.Type(value = SewerPipes.class, name = "sewerPipes"),
        @JsonSubTypes.Type(value = SpecialStorageCompartment.class, name = "specialStorageCompartment"),
        @JsonSubTypes.Type(value = AlienAddons.class, name = "alienAddons"),
        @JsonSubTypes.Type(value = ShieldGenerator.class, name = "shieldGenerator"),
        @JsonSubTypes.Type(value = SpaceVoid.class, name = "spaceVoid")
})
public abstract class Component {

    int type;



    public Component() {}

    //i metodi da overridare sono sa impostare come

    //initType
    public abstract void initType();

//
//    //getAbility
//    public  int getAbility();
//    public  ArrayList<Goods> getAbility(Goods good);
//    public  ArrayList<Integer> getAbility(int integer);
//
//
//    //setAbility
//    public  int setAbility();
//    public  int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien);
//    public  int setAbility(Goods good, boolean select);
//    public  void setAbility(boolean direzione);
//
//
//
//    public  boolean controlValidity(PlayerBoard pb, int x, int y);
//





    public abstract void rotate(Boolean direction);


    public  void insert(PlayerBoard playerBoard){
        return;
    };

    public void remove(PlayerBoard playerBoard){
        return;
    }

    public  abstract void accept(ComponentActionVisitor visitor, PlayerStates State);


    public abstract boolean controlValidity(PlayerBoard pb, int x, int y);


//  metodi per json
    public int getType() {return type;}
    public void setType(int type) {this.type = type;}


//    public boolean isPurpleAlien() {return false;}
//    public boolean isBrownAlien() {
//        return false;
//    }
//






}
