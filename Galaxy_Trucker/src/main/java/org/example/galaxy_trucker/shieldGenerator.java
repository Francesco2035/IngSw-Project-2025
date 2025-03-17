package org.example.galaxy_trucker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class shieldGenerator extends Component{


    private ArrayList<Integer> protectedDirections;

    public shieldGenerator() {
    }

    @Override
    public void initType(){
        if (type.equals("nord-ovest")) {
            protectedDirections = new ArrayList<>();
            protectedDirections.add(1);
            protectedDirections.add(2);
        }
    }
}
