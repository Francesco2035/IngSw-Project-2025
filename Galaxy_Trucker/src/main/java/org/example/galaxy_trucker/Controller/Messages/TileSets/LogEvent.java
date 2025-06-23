package org.example.galaxy_trucker.Controller.Messages.TileSets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;

public class LogEvent implements Event {


    String effect = "";
    int x;
    int y;
    int direction;
    int type;




    @JsonCreator
    public LogEvent( @JsonProperty("effect") String Effect, @JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("direction") int direction, @JsonProperty("type") int type) {

        if (Effect == null){
            this.effect = "";
        }
        else {
            this.effect = Effect;
        }
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String message() {
        return effect;
    }
    @JsonIgnore
    public int getX() {
        return x;
    }
    @JsonIgnore
    public int getY() {
        return y;
    }
    @JsonIgnore
    public int getDirection() {
        return direction;
    }
    @JsonIgnore
    public int getType() {
        return type;
    }

}
