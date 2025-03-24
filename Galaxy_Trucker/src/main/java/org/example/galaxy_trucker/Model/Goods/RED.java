package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RED implements Goods{

    @JsonProperty("RED")
    private int value = 4;

    @Override
    public int getValue(){
        return value;
    }
}
