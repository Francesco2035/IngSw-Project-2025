package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    public void Jackson(){
        ArrayList<IntegerPair> pairs = new ArrayList<>();
        pairs.add(new IntegerPair(1, 2));
        GiveAttackCommand attackCommand = new GiveAttackCommand(pairs, "gameid", "",2, "title", "token");
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            String json = objectMapper.writeValueAsString(attackCommand);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());

        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

}