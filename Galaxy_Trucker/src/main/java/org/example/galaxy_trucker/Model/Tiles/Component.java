package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.ComponentCheckers.ComponentChecker;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.ComponentGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentSetters.ComponentSetter;

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

    String type;
    private ComponentGetter componentGetter;
    private ComponentSetter componentSetter;
    private ComponentChecker componentChecker;


    public Component() {}

    public  ComponentSetter getComponentSetter(){
        return componentSetter;
    };
    public  ComponentGetter getComponentGetter(){
        return componentGetter;
    };
    public  ComponentChecker getComponentChecker(){
        return componentChecker;
    };


    public  void setComponentSetter(ComponentSetter componentSetter){
        this.componentSetter = componentSetter;
    };
    public  void setComponentGetter(ComponentGetter componentGetter){
        this.componentGetter = componentGetter;
    };
    public  void setComponentChecker(ComponentChecker componentChecker){
        this.componentChecker = componentChecker;
    };

    public Object get(ComponentGetter cg){
        setComponentGetter(cg);
        return getComponentGetter().get();
    }



    //i metodi da overridare sono sa impostare come abstract

    //initType
    public abstract void initType();
    public abstract void initType(int numHumans, boolean purpleAlien, boolean brownAlien);


    //getAbility
    public abstract int getAbility();
    public abstract ArrayList<Goods> getAbility(Goods good);
    public abstract ArrayList<Integer> getAbility(int integer);


    //setAbility
    public abstract int setAbility();
    public abstract int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien);
    public abstract int setAbility(Goods good, boolean select);
    public abstract void setAbility(boolean direzione);



    public abstract boolean controlValidity(PlayerBoard pb, int x, int y);


//  metodi per json
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public boolean isPurpleAlien() {return false;}
    public boolean isBrownAlien() {
        return false;
    }







}
