package org.example.galaxy_trucker;

public class BatteryComp extends Component{

    private int privEnergy;
//    private int numEnergy;

    public BatteryComp(int numEnergy) {
        super();
        this.privEnergy = numEnergy;
//        this.numEnergy = numEnergy;
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
