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
    public void setNumHumans(int numHumans) {this.numHumans = numHumans;}


    @Override
    public int getAbility() {
        return numHumans;
    }



    @Override
    public void initType(int numHumans, boolean purpleAlien, boolean brownAlien) {
        this.numHumans = numHumans;
    }



    @Override
    public int setAbility(int numAbility, boolean purpleAlien, boolean brownAlien) {
        if(this.numHumans>0) this.numHumans -= numAbility;
        return numHumans;
    }


}
