package org.example.galaxy_trucker.Model.Tiles;

public class alienAddons extends Component{


//purple=true, brown=false
    private boolean whatColor;

    public alienAddons() {}


    public boolean isWhatColor() {
        return whatColor;
    }

    public void setWhatColor(boolean whatColor) {
        this.whatColor = whatColor;
    }


    @Override
    public int getAbility() {
        if (whatColor) {
            return 1;
        } else return 0;
    }

    @Override
    public void initType(){
        if(type.equals("purple")) whatColor = true;
        else if(type.equals("brown")) whatColor = false;
    }
}
