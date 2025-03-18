package org.example.galaxy_trucker.Model.Tiles;

public class MainCockpitComp extends Component {

    private int numHumans;
    private int color;
    private String type;

    public MainCockpitComp(int color) {

        this.color = color;
        this.type="MainCockpit";
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
    @Override
    public String getType() {
        return type;
    }


}

