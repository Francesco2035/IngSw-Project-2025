package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("GREEN")
public class GREEN implements Goods{

    private int value = 2;

    public GREEN() {}

    @Override
    public int getValue(){
        return value;
    }
}
