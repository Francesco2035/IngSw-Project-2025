package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents the "GREEN" type of goods in the system.
 * This class implements the Goods interface and provides a specific value for the "GREEN" type.
 * It is used to define goods with a predefined value.
 *
 * An instance of this class will have a value of 2, which can be retrieved using the `getValue` method.
 * The class is annotated with `@JsonTypeName("GREEN")` to support serialization and deserialization with JSON.
 */
@JsonTypeName("GREEN")
public class GREEN implements Goods{

    /**
     * Represents the predefined value associated with the "GREEN" type of goods.
     * This field holds an integer value of 2, defining the specific value
     * for this type. It is a key property of the "GREEN" goods category,
     * used for identification and comparison in the system.
     */
    private int value = 2;

    /**
     * Default constructor for the GREEN class.
     *
     * Initializes an instance of the GREEN class representing a good with
     * a predefined value specific to the "GREEN" type. This value can be
     * retrieved using the {@code getValue()} method.
     */
    public GREEN() {}

    /**
     * Retrieves the predefined value associated with this type of goods.
     *
     * @return the integer value representing the specific type of goods
     */
    @Override
    public int getValue(){
        return value;
    }
}
