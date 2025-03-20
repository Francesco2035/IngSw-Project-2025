package org.example.galaxy_trucker.Model;

import javafx.util.Pair;

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

