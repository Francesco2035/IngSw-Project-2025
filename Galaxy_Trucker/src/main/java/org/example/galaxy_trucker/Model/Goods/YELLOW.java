package org.example.galaxy_trucker.Model.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents the "YELLOW" type of goods in the system.
 * This class implements the Goods interface and provides a specific value for the "YELLOW" type.
 * It is used to define goods with a predefined value.
 *
 * An instance of this class will have a value of 3, which can be retrieved using the `getValue` method.
 * The class is annotated with `@JsonTypeName("YELLOW")` to support serialization and deserialization with JSON.
 */
@JsonTypeName("YELLOW")
public class YELLOW implements Goods{

    /**
     * Represents the predefined value associated with the "YELLOW" type of goods.
     * This field holds an integer value of 3, defining the specific value for this type.
     * It serves as a distinguishing characteristic of the "YELLOW" goods category,
     * enabling identification and comparison within the system.
     */
    private int value = 3;

    /**
     * Default constructor for the YELLOW class.
     *
     * Initializes an instance of the YELLOW class, representing a good with
     * a predefined value specific to the "YELLOW" type. This value can be
     * retrieved using the {@code getValue()} method. The "YELLOW" type of goods
     * has a constant value of 3.
     */
    public YELLOW() {}

    /**
     * Retrieves the predefined value associated with the "YELLOW" type of goods.
     * This method returns the integer value representing the specific type of goods
     * implemented by this class.
     *
     * @return the integer value representing the "YELLOW" type of goods
     */
    @Override
    public int getValue(){
        return value;
    }
}
