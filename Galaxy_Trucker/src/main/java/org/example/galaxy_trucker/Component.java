package org.example.galaxy_trucker;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "componentType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = plasmaDrill.class, name = "plasmaDrill"),
        @JsonSubTypes.Type(value = hotWaterHeater.class, name = "hotWaterHeater"),
        @JsonSubTypes.Type(value = powerCenter.class, name = "powerCenter"),           //BatteryComp
        @JsonSubTypes.Type(value = storageCompartment.class, name = "storageCompartment"),
        @JsonSubTypes.Type(value = modularHousingUnit.class, name = "modularHousingUnit"),
        @JsonSubTypes.Type(value = sewerPipes.class, name = "sewerPipes"),
        @JsonSubTypes.Type(value = specialStorageCompartment.class, name = "specialStorageCompartment"),
        @JsonSubTypes.Type(value = alienAddons.class, name = "alienAddons"),
        @JsonSubTypes.Type(value = shieldGenerator.class, name = "shieldGenerator"),
        @JsonSubTypes.Type(value = spaceVoid.class, name = "spaceVoid")
})
public abstract class Component {

    PlayerPlance myPlance;

    public Component() {;
    }

    public void referencePlance(PlayerPlance myPlance) {
        this.myPlance = myPlance;
    }

    public int getAbility(){
        return 0;
    }

    public int setAbility(){
        return 0;
    }

}
