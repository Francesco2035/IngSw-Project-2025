package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JsonHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static int readInt(JsonNode root, String fieldName) {
        if (root.has(fieldName)) {
            return root.get(fieldName).asInt();
        }
        throw new IllegalArgumentException("Missing or invalid integer field: " + fieldName);
    }

    public static boolean readBoolean(JsonNode root, String fieldName) {
        if (root.has(fieldName)) {
            return root.get(fieldName).asBoolean();
        }
        throw new IllegalArgumentException("Missing or invalid boolean field: " + fieldName);
    }

    public static ArrayList<IntegerPair> readIntegerPairs(JsonNode root, String fieldName) {
        ArrayList<IntegerPair> pairs = new ArrayList<>();
        if (root.has(fieldName)) {
            JsonNode coordsArray = root.get(fieldName);
            for (JsonNode node : coordsArray) {
                int x = node.get("x").asInt();
                int y = node.get("y").asInt();
                pairs.add(new IntegerPair(x, y));
            }
        }
        return pairs;
    }

    public static ArrayList<IntegerPair> readUniqueIntegerPairs(JsonNode root, String fieldName) {
        Set<IntegerPair> pairSet = new HashSet<>();
        if (root.has(fieldName)) {
            JsonNode coordsArray = root.get(fieldName);
            for (JsonNode node : coordsArray) {
                int x = node.get("x").asInt();
                int y = node.get("y").asInt();
                pairSet.add(new IntegerPair(x, y));
            }
        }
        return new ArrayList<>(pairSet);
    }


    public static JsonNode parseJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error parsing JSON", e);
        }
    }

    public static String getRequiredText(JsonNode root, String fieldName) {
        if (root.has(fieldName)) {
            return root.get(fieldName).asText();
        }
        throw new IllegalArgumentException("Missing or invalid text field: " + fieldName);
    }


    public static JsonNode getNode(JsonNode root, String fieldName) {
        if (root.has(fieldName)) {
            return root.get(fieldName);
        } else {
            throw new IllegalArgumentException("Field '" + fieldName + "' is missing in the JSON.");
        }
    }
}
