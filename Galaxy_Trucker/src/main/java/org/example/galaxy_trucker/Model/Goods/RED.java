package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RED")
public class RED implements Goods{

    private int value = 4;

    public RED() {}

    @Override
    public int getValue(){
        return value;
    }
}
