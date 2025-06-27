package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a pair of integers with additional functionality for
 * equality checking, hash code generation, and mutable properties.
 * This class is serializable and integrates JSON property mapping
 * for serialization/deserialization.
 */
public class IntegerPair implements Serializable {

    /**
     * Represents the first integer in the pair.
     * This field is serialized to and deserialized from the JSON property "first".
     */
    @JsonProperty("first")
    private int first;

    /**
     * Represents the second integer value of the pair.
     * This field is mapped to a JSON property named "second"
     * to support serialization and deserialization.
     */
    @JsonProperty("second")
    private int second;

    /**
     * Constructs an IntegerPair with the specified integer values.
     *
     * @param first the first integer value of the pair
     * @param second the second integer value of the pair
     */
    public IntegerPair(int first, int second) {
        this.first = first;
        this.second = second;
    }


    /**
     * Returns a string representation of the IntegerPair object.
     * The format of the returned string includes the values of the
     * {@code first} and {@code second} fields.
     *
     * @return a string representation of the IntegerPair, showing the values of its fields.
     */
    @Override
    public String toString() {
        return "IntegerPair{x=" + first + ", y=" + second + "}";
    }


    /**
     * Compares this object to the specified object for equality.
     *
     * @param obj the object to compare with this instance
     * @return true if the specified object is equal to this instance, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // Verifica se sono lo stesso oggetto
        if (this == obj) return true;

        // Verifica se l'oggetto è nullo o di tipo diverso
        if (obj == null || getClass() != obj.getClass()) return false;

        // Confronta i valori
        IntegerPair that = (IntegerPair) obj;
        return first == that.first && second == that.second;
    }

    /**
     * Sets the values of the first and second properties of this object.
     *
     * @param value1 the new value to set for the first property
     * @param value2 the new value to set for the second property
     */
    public void setValue(int value1, int value2) {
        this.first = value1;
        this.second = value2;
    }

    /**
     * Computes the hash code for this instance of the class using the fields of the object.
     *
     * @return an integer representing the hash code of the object, calculated using the
     *         first and second fields.
     */
    // Sovrascrizione di hashCode
    @Override
    public int hashCode() {
        // Usa Objects.hash per combinare i campi
        return Objects.hash(first, second);
    }


    /**
     * Default constructor for the IntegerPair class.
     * Initializes an instance with default integer values for both fields.
     * This constructor is primarily used for JSON deserialization.
     */
    //setter per json
    public IntegerPair() {}

    /**
     * Retrieves the value of the first integer in the pair.
     *
     * @return the value of the first integer
     */
    public int getFirst() {
        return first;
    }
    /**
     * Retrieves the value of the second integer in the pair.
     *
     * @return the second integer in the pair.
     */
    public int getSecond() {
        return second;
    }

    /**
     * Sets the value of the first integer in the pair.
     *
     * @param first the new value to assign to the first integer
     */
    public void setFirst(int first) {
        this.first = first;
    }
    /**
     * Sets the value of the second integer in this pair.
     *
     * @param second the new value to set for the second integer
     */
    public void setSecond(int second) {
        this.second = second;
    }


}

