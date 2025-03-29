package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("BLUE")
public class BLUE implements Goods{

    private int value = 1;

    public BLUE() {}

    @Override
    public int getValue(){
        return value;
    }
}
