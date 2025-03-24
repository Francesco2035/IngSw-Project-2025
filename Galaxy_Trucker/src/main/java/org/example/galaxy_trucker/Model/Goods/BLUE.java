package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BLUE implements Goods{

    @JsonProperty("BLUE")
    private int value = 1;

    @Override
    public int getValue(){
        return value;
    }
}
