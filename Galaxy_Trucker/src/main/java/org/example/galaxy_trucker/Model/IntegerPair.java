package org.example.galaxy_trucker.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class IntegerPair implements Serializable {

    @JsonProperty("first")
    private int first;

    @JsonProperty("second")
    private int second;

    public IntegerPair(int first, int second) {
        this.first = first;
        this.second = second;
    }


    @Override
    public String toString() {
        return "IntegerPair{x=" + first + ", y=" + second + "}";
    }


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

    public void setValue(int value1, int value2) {
        this.first = value1;
        this.second = value2;
    }

    // Sovrascrizione di hashCode
    @Override
    public int hashCode() {
        // Usa Objects.hash per combinare i campi
        return Objects.hash(first, second);
    }


    //setter per json
    public IntegerPair() {}

    public int getFirst() {
        return first;
    }
    public int getSecond() {
        return second;
    }

    public void setFirst(int first) {
        this.first = first;
    }
    public void setSecond(int second) {
        this.second = second;
    }


}

