package org.example.galaxy_trucker.Controller.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenEvent implements Event{

    String token;


    public TokenEvent(@JsonProperty("token") String token) {
        this.token = token;
    }

    public TokenEvent() {

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

}
