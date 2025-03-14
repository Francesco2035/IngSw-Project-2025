package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GAGenTest {

    @Test
    void getDeck() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        List<Tile> deck = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        for (Tile comp : deck) {
            comp.getComponent().initType();
            System.out.println("ID: " + comp.getId());

            if (comp.getComponent() instanceof plasmaDrill) {
                plasmaDrill drill = (plasmaDrill) comp.getComponent();
                System.out.println("È una PlasmaDrill!");
            } else if (comp.getComponent() instanceof hotWaterHeater) {
                hotWaterHeater heater = (hotWaterHeater) comp.getComponent();
                System.out.println("È un HotWaterHeater!");
            }

            System.out.println("Connectors: " + comp.getConnectors());
        }
        assertEquals(2, deck.getFirst().getComponent().getAbility()); //testo se il primo powerCenter ha privEnergy=2 dopo l'operazione di initType
    }
}