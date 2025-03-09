package org.example.galaxy_trucker;

public class BatteryComp extends Component{

    private Tile[][] myPlance;


    private int privEnergy;

    public BatteryComp(PlayerPlance myPlance) {
        super(myPlance);
        this.privEnergy = 0;
    }



    public void setPrivEnergy(int privEnergy) {
        this.privEnergy = privEnergy;
    }


    public int getPrivEnergy() {
        return privEnergy;
    }


    @Override
    public int getAbility() {
        return privEnergy;
    }
    @Override
    public int setAbility() {
        try {
            if(this.getAbility() < 1) throw new IllegalArgumentException("cannot exceed 0");
            this.privEnergy--;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return this.privEnergy;
    }
}
