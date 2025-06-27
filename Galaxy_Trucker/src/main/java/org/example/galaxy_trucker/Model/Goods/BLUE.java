package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents the "BLUE" type of goods in the system.
 * This class implements the Goods interface and provides a specific value for the "BLUE" type.
 * It is used to define goods with a predefined value.
 *
 * An instance of this class will have a value of 1, which can be retrieved using the `getValue` method.
 * The class is annotated with `@JsonTypeName("BLUE")` to support serialization and deserialization with JSON.
 */
@JsonTypeName("BLUE")
public class BLUE implements Goods{

    /**
     * Represents the default value associated with the "BLUE" type of goods.
     * This field holds an integer value of 1, defining the specific value for this type.
     * It is a key characteristic of the "BLUE" goods category, used for identification
     * and comparison in the system.
     */
    private int value = 1;

    /**
     * Default constructor for the BLUE class.
     *
     * Initializes an instance of the BLUE class representing a good with
     * a predefined value specific to the "BLUE" type. This value can be
     * retrieved using the {@code getValue()} method.
     */
    public BLUE() {}

    /**
     * Retrieves the predefined value associated with this type of goods.
     *
     * @return the integer value representing the type of goods
     */
    @Override
    public int getValue(){
        return value;
    }
}
