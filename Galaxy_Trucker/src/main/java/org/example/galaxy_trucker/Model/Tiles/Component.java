package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;


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

    public abstract void rotate(Boolean direction);
    public abstract boolean controlValidity(PlayerBoard pb, int x, int y);

    public abstract void insert(PlayerBoard playerBoard);
    public abstract void remove(PlayerBoard playerBoard);

//  metodi per json
    public int getType() {return type;}
    public void setType(int type) {this.type = type;}

}



//    //getAbility
//    public  int getAbility();
//    public  ArrayList<Goods> getAbility(Goods good);
//    public  ArrayList<Integer> getAbility(int integer);
//
//    //setAbility
//    public  int setAbility();
//    public  int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien);
//    public  int setAbility(Goods good, boolean select);
//    public  void setAbility(boolean direzione);
