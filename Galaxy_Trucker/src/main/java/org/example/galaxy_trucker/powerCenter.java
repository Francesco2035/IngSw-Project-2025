package org.example.galaxy_trucker;

public class powerCenter extends Component{


    private int privEnergy;

    public powerCenter(int numEnergy) {
        super();
        this.privEnergy = numEnergy;
    }


    public powerCenter() {
    }

    @Override
    public int getAbility() {
        return privEnergy;
    }


    @Override
    public int setAbility() {
        try {
            if(this.getAbility() == 0) throw new IllegalArgumentException("cannot exceed 0");
            else this.privEnergy--;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return this.privEnergy;
    }
}
