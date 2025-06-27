package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the jason helper
 */
class JsonHelperTest {
    /**
     * tests the excepion caught by the jsonhelper
     */
    @Test
    void testJsonHelper() {
        JsonNode node = JsonHelper.parseJson("  { \"id\": 23, \"component\":  {\"componentType\": \"storageCompartment\", \"type\":2}, \"connectors\": [{\"type\":\"UNIVERSAL\"}, {\"type\":\"NONE\"}, {\"type\":\"SINGLE\"}, {\"type\":\"NONE\"}] },\n");
        try {
            JsonHelper.parseJson("MHANZ");
        } catch (Exception e) {
            assertEquals("Error parsing JSON", e.getMessage());
        }


        JsonHelper.getRequiredText(node, "id");
        try {
            JsonHelper.getRequiredText(node, "MHANZ");
        } catch (Exception e) {
            assertEquals("Missing or invalid text field: MHANZ", e.getMessage());
        }

    }
}