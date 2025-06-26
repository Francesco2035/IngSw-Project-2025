package org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

public class RewardsEvent implements Event {

    @JsonProperty("rewards")
    ArrayList<Goods> rewards;

    public RewardsEvent() {

    }

    @JsonCreator
    public RewardsEvent(@JsonProperty("rewards") ArrayList<Goods> rewards) {
        this.rewards = rewards;
    }



    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    @Override
    public String message() {
        return "";
    }


    public ArrayList<Goods> getRewards() {
        return rewards;
    }
}
