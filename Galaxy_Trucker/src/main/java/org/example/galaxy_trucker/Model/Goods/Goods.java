package org.example.galaxy_trucker.Model.Goods;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serial;
import java.io.Serializable;


/**
 * Represents a general interface for goods within the system.
 * This interface defines the contract for goods to implement, ensuring
 * that all goods have a specific value associated with them.
 *
 * The Goods interface supports polymorphic serialization and deserialization
 * using Jackson annotations. Concrete implementations of this interface are
 * defined as subtypes and are identified by unique type names.
 *
 * Subtypes include:
 * - BLUE
 * - GREEN
 * - YELLOW
 * - RED
 *
 * Each subtype corresponds to a specific type of good with a distinct value.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)


@JsonSubTypes({
        @JsonSubTypes.Type(value = BLUE.class, name = "BLUE"),
        @JsonSubTypes.Type(value = GREEN.class, name = "GREEN"),
        @JsonSubTypes.Type(value = YELLOW.class, name = "YELLOW"),
        @JsonSubTypes.Type(value = RED.class, name = "RED")
})


public interface Goods extends Serializable {
    /**
     * Retrieves the value associated with this type of goods.
     * Each concrete implementation of the Goods interface defines
     * a specific value which can be accessed through this method.
     *
     * @return the integer value representing the specific type of goods
     */
    int getValue();
}
