package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CommandTest {

    @Test
    public void Jackson(){
        ArrayList<IntegerPair> pairs = new ArrayList<>();
        pairs.add(new IntegerPair(1, 2));
        ObjectMapper objectMapper = new ObjectMapper();



        AcceptCommand ac = new AcceptCommand("gameid", "",2, "title", true,"token");
        try{
            String json = objectMapper.writeValueAsString(ac);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");


        AddCrewCommand acc = new AddCrewCommand(2, false, false, new IntegerPair(8, 9), "gameid", "",2, "title","token");
        try{
            String json = objectMapper.writeValueAsString(acc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }



        System.out.println("\n");
        System.out.println("\n");




        BuildingCommand bc = new BuildingCommand(5, 6, 0, 1, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(bc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }



        System.out.println("\n");
        System.out.println("\n");


        ChoosingPlanetsCommand cpc = new ChoosingPlanetsCommand(1, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(cpc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");




        DebugShip ds = new DebugShip("gameid", "",2, "title", "token", 2);
        try{
            String json = objectMapper.writeValueAsString(ds);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("\n");



        DefendFromLargeCommand dflc = new DefendFromLargeCommand(new IntegerPair(4, 4), new IntegerPair(7 ,8), "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(dflc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("\n");



        DefendFromSmallCommand dfsc = new DefendFromSmallCommand(new IntegerPair(7 ,8), "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(dfsc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }



        System.out.println("\n");
        System.out.println("\n");


        FinishBuildingCommand fbc = new FinishBuildingCommand(6, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(fbc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");


        GiveAttackCommand attackCommand = new GiveAttackCommand(pairs, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(attackCommand);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");



        GiveSpeedCommand speedCommand = new GiveSpeedCommand(pairs, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(speedCommand);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");




        HandleCargoCommand hcc = new HandleCargoCommand(1, new IntegerPair(7, 8), 8, new IntegerPair(5, 6), "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(hcc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");



        KillCommand kc = new KillCommand(pairs, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(kc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");




        LobbyCommand lbc = new LobbyCommand("title");
        try{
            String json = objectMapper.writeValueAsString(lbc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");




        LoginCommand lgc = new LoginCommand("gameid", "",2, "title", 4);
        try{
            String json = objectMapper.writeValueAsString(lgc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");




        QuitCommand qc = new QuitCommand("gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(qc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");


        ReadyCommand rc = new ReadyCommand("gameid", "",2, "title", true, "token");
        try{
            String json = objectMapper.writeValueAsString(rc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");



        ReconnectCommand rcc = new ReconnectCommand("token", "gameid", "",2, "title" );
        try{
            String json = objectMapper.writeValueAsString(rcc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");


        RemoveTileCommand rtc = new RemoveTileCommand(3, 4, "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(rtc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }



        System.out.println("\n");
        System.out.println("\n");



        SelectChunkCommand scc = new SelectChunkCommand(new IntegerPair(7, 8), "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(scc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");


        Theft t = new Theft(4, new IntegerPair(7, 8), "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(t);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        System.out.println("\n");
        System.out.println("\n");



        TheftCommand tc = new TheftCommand(4, new IntegerPair(7, 8), "gameid", "",2, "title", "token");
        try{
            String json = objectMapper.writeValueAsString(tc);
            System.out.println(json);
            Command command = objectMapper.readValue(json, Command.class);
            System.out.println(command.getClass());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


    }
}