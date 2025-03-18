package org.example.galaxy_trucker;

public class plasmaDrill extends Component{


    private boolean isDouble;

    public plasmaDrill() {}


    public boolean isDouble() {
        return isDouble;
    }
    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }


    @Override
    public int getAbility() {
        if (isDouble) {return 2;}
        else {return 1;}
    }


    @Override
    public void initType(){
        if (type.equals("single")) isDouble = false;
        else if (type.equals("double")) isDouble = true;
    }
}
