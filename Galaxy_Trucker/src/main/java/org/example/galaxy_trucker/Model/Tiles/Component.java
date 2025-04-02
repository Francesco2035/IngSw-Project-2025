package org.example.galaxy_trucker.Model.Tiles;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Tiles.ComponentCheckers.ComponentChecker;
import org.example.galaxy_trucker.Model.Tiles.ComponentGetters.ComponentGetter;
import org.example.galaxy_trucker.Model.Tiles.ComponentSetters.ComponentSetter;

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
    private ComponentGetter componentGetter;
    private ComponentSetter componentSetter;
    private ComponentChecker componentChecker;


    public Component() {}

    public ComponentSetter getComponentSetter(){
        return componentSetter;
    };
    public ComponentGetter getComponentGetter(){
        return componentGetter;
    };
    public ComponentChecker getComponentChecker(){
        return componentChecker;
    };


    public void setComponentSetter(ComponentSetter componentSetter){
        this.componentSetter = componentSetter;
    };
    public void setComponentGetter(ComponentGetter componentGetter){
        this.componentGetter = componentGetter;
    };
    public void setComponentChecker(ComponentChecker componentChecker){
        this.componentChecker = componentChecker;
    };


    public Object get(ComponentGetter cg){
        setComponentGetter(cg);
        return getComponentGetter().get();
    }

    public void set(ComponentSetter cg){
        setComponentSetter(cg);
        getComponentSetter().set();
    }


    //i metodi da overridare sono sa impostare come

    //initType
    public abstract void initType();


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
