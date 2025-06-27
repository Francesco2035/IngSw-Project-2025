package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Model.IntegerPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for handling JSON operations using Jackson library.
 */
public class JsonHelper {

    /**
     * A static and immutable instance of the ObjectMapper class from the Jackson library.
     * This mapper is used for performing JSON serialization and deserialization throughout the utility class.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

//    public static int readInt(JsonNode root, String fieldName) {
//        if (root.has(fieldName)) {
//            return root.get(fieldName).asInt();
//        }
//        throw new IllegalArgumentException("Missing or invalid integer field: " + fieldName);
//    }

//    public static boolean readBoolean(JsonNode root, String fieldName) {
//        if (root.has(fieldName)) {
//            return root.get(fieldName).asBoolean();
//        }
//        throw new IllegalArgumentException("Missing or invalid boolean field: " + fieldName);
//    }

//    public static ArrayList<IntegerPair> readIntegerPairs(JsonNode root, String fieldName) {
//        ArrayList<IntegerPair> pairs = new ArrayList<>();
//        if (root.has(fieldName)) {
//            JsonNode coordsArray = root.get(fieldName);
//            for (JsonNode node : coordsArray) {
//                int x = node.get("x").asInt();
//                int y = node.get("y").asInt();
//                pairs.add(new IntegerPair(x, y));
//            }
//        }
//        else{
//            throw new IllegalArgumentException("Missing or invalid coordinate field: " + fieldName);
//        }
//        return pairs;
//    }

//    public static ArrayList<IntegerPair> readUniqueIntegerPairs(JsonNode root, String fieldName) {
//        Set<IntegerPair> pairSet = new HashSet<>();
//        if (root.has(fieldName)) {
//            JsonNode coordsArray = root.get(fieldName);
//            for (JsonNode node : coordsArray) {
//                int x = node.get("x").asInt();
//                int y = node.get("y").asInt();
//                pairSet.add(new IntegerPair(x, y));
//            }
//        }
//        else{
//            throw new IllegalArgumentException("Missing or invalid coordinate field: " + fieldName);
//        }
//        return new ArrayList<>(pairSet);
//    }


    /**
     * Parses a JSON string and converts it into a JsonNode object.
     *
     * @param json the JSON string to be parsed
     * @return a JsonNode representing the parsed JSON structure
     * @throws IllegalArgumentException if the input string is invalid JSON or cannot be parsed
     */
    public static JsonNode parseJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error parsing JSON", e);
        }
    }

    /**
     * Retrieves the text value of a specified field from a given JSON node.
     * If the field is absent or does not contain a valid text value, an exception is thrown.
     *
     * @param root      the root JSON node from which the field is to be retrieved
     * @param fieldName the name of the field whose text value is to be retrieved
     * @return the text value of the specified field
     * @throws IllegalArgumentException if the specified field is missing or does not contain a valid text value
     */
    public static String getRequiredText(JsonNode root, String fieldName) {
        if (root.has(fieldName)) {
            return root.get(fieldName).asText();
        }
        throw new IllegalArgumentException("Missing or invalid text field: " + fieldName);
    }


//    public static JsonNode getNode(JsonNode root, String fieldName) {
//        if (root.has(fieldName)) {
//            return root.get(fieldName);
//        } else {
//            throw new IllegalArgumentException("Field '" + fieldName + "' is missing in the JSON.");
//        }
//    }
}
