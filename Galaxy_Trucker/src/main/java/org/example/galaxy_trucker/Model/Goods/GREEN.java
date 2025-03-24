package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GREEN implements Goods{

    @JsonProperty("GREEN")
    private int value = 2;

    @Override
    public int getValue(){
        return value;
    }
}
