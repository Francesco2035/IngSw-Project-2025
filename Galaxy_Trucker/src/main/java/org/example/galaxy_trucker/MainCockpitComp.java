package org.example.galaxy_trucker;

public class MainCockpitComp extends Component {

    private int numHumans;
    private int color;

    public MainCockpitComp(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getNumHumans() {
        return numHumans;
    }

    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }


    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
        this.numHumans -= numAbility;
        return numHumans;
    }


}
