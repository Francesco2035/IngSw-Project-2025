package org.example.galaxy_trucker;

import javafx.util.Pair;

import javax.lang.model.type.NullType;

import java.util.ArrayList;


public class PairTest {
    private ArrayList<Pair<Integer, Integer>> Pairtest;

    PairTest(ArrayList<Pair<Integer, Integer>> Pairtest) {
        this.Pairtest = Pairtest;
    }

    public int GetFirstNumber( int N) {
        return this.Pairtest.get(N).getKey();
    }
    public int GetSecondNumber( int N) {
        return this.Pairtest.get(N).getValue();
    }
}
