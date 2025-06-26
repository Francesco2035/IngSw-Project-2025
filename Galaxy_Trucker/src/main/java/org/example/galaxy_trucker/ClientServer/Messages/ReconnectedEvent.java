package org.example.galaxy_trucker.ClientServer.Messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReconnectedEvent implements Event{

    String token;
    String gameId;
    String playerId;
    int lv;

    public ReconnectedEvent() {

    }

    @JsonCreator
    public ReconnectedEvent(@JsonProperty("token") String token,@JsonProperty("gameId") String gameId,@JsonProperty("playerId") String playerId,@JsonProperty("lv") int lv){
        this.token = token;
        this.gameId = gameId;
        this.playerId = playerId;
        this.lv = lv;
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

    public String getToken() {
        return token;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getLv() {
        return lv;
    }
}
