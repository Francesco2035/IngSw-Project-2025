package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("YELLOW")
public class YELLOW implements Goods{

    private int value = 3;

    public YELLOW() {}

    @Override
    public int getValue(){
        return value;
    }
}
