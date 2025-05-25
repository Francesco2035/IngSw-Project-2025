package org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.EventVisitor;
import org.example.galaxy_trucker.Model.Goods.Goods;

import java.util.ArrayList;

public class RewardsEvent implements Event {


    @JsonProperty("rewards")
    ArrayList<Goods> rewards;

    public RewardsEvent() {

    }

    public RewardsEvent(ArrayList<Goods> rewards) {
        this.rewards = rewards;
    }



    @Override
    public void accept(EventVisitor visitor) {

    }

    @Override
    public String message() {
        return "";
    }


    public ArrayList<Goods> getRewards() {
        return rewards;
    }
}
