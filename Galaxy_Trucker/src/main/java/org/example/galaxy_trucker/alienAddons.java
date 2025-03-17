package org.example.galaxy_trucker;

public class alienAddons extends Component{



    //purple=true, brown=false
    private boolean whatColor;

    public alienAddons() {
    }



    @Override
    public void initType(){
        if(type.equals("purple")) whatColor = true;
        else if(type.equals("brown")) whatColor = false;
    }
}
