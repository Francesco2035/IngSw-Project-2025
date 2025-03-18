package org.example.galaxy_trucker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.Tiles.Component;
import org.example.galaxy_trucker.Model.Tiles.Tile;
import org.example.galaxy_trucker.Model.Tiles.alienAddons;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class alienAddonsTest {

    @Test
    void getAbility() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});

        Component comp = Tiles.get(143).getComponent();
        comp.initType();
        assertEquals(1, comp.getAbility());


        comp = Tiles.get(134).getComponent();
        comp.initType();
        assertEquals(0, comp.getAbility());


    }

    @Test
    void initType() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File TilesJSON = new File("src/main/resources/org/example/galaxy_trucker/Tiles.JSON");  //add file json
        ArrayList<Tile> Tiles = mapper.readValue(TilesJSON, new TypeReference<ArrayList<Tile>>() {});


        alienAddons alienAddon = ((alienAddons) Tiles.get(135).getComponent());
        alienAddon.initType();
        assertFalse(alienAddon.isWhatColor());
    }
}