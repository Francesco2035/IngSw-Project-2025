package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;

public class YELLOW implements Goods{

    @JsonProperty("YELLOW")
    private int value = 3;

    @Override
    public int getValue(){
        return value;
    }
}
