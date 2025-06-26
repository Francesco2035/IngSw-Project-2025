package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents the "RED" type of goods in the system.
 * This class implements the Goods interface and provides a specific value for the "RED" type.
 * It is used to define goods with a predefined value.
 *
 * An instance of this class will have a value of 4, which can be retrieved using the `getValue` method.
 * The class is annotated with `@JsonTypeName("RED")` to support serialization and deserialization with JSON.
 */
@JsonTypeName("RED")
public class RED implements Goods{

    /**
     * Represents the predefined value associated with the "RED" type of goods.
     * This field holds an integer value of 4, defining the specific value
     * for this type. It serves as a key property for the "RED" goods category,
     * allowing for identification and comparison within the system.
     */
    private int value = 4;

    /**
     * Default constructor for the RED class.
     *
     * Initializes an instance of the RED class, representing a good with
     * a predefined value specific to the "RED" type. This value can be
     * retrieved using the {@code getValue()} method. The "RED" type of goods
     * has a constant value of 4.
     */
    public RED() {}

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
