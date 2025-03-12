package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GAGen {

    ObjectMapper mapper = new ObjectMapper();
    File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
    List<Tile> deck = mapper.readValue(TilesJSON, new TypeReference<List<Tile>>() {});


    private GAGen() throws IOException {

        //parsa json per connectors e component e chiama il builder di tile ripetuto fino a EOF
    }





    public List<Tile> getDeck() {
        return deck;
    }



}




















//ObjectMapper mapper = new ObjectMapper(factory);
//JsonNode root = mapper.readTree(TilesJSON);
//
//int id = root.get("id").asInt();
//String component = root.get("component").asText();
//String ability = root.get("ability").asText();
//ArrayList<String> myconn = new ArrayList<>();
//JsonNode connectors =  root.get("connectors");
//            for (JsonNode connector : connectors) {
//        myconn.add(connector.asText());
//        }
//
//Tile deckTile = new Tile(id, )
