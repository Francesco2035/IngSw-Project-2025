package org.example.galaxy_trucker.Model.Tiles;

public class powerCenter extends Component{



    private int privEnergy;

    public powerCenter(int numEnergy) {
        this.privEnergy = numEnergy;
    }


    public powerCenter() {}



    public int getPrivEnergy() {
        return privEnergy;}
    public void setPrivEnergy(int privEnergy) {
        this.privEnergy = privEnergy;}




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



    @Override
    public void initType() {
        if(type.equals("double")) setPrivEnergy(2);
        else if (type.equals("triple")) setPrivEnergy(3);
    }
}

