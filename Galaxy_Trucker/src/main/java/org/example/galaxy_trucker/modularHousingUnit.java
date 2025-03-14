package org.example.galaxy_trucker;

public class modularHousingUnit extends Component {

    private int numHumans;
    private boolean purpleAlien;
    private boolean brownAlien;


    public modularHousingUnit() {
        this.numHumans = 0;
        this.purpleAlien = false;
        this.brownAlien = false;
    }


    public int getNumHumans() {
        return numHumans;
    }

    public void setNumHumans(int numHumans) {
        this.numHumans = numHumans;
    }

    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    public void setPurpleAlien(boolean purpleAlien) {
        this.purpleAlien = purpleAlien;
    }

    public boolean isBrownAlien() {
        return brownAlien;
    }

    public void setBrownAlien(boolean brownAlien) {
        this.brownAlien = brownAlien;
    }


    @Override
    public int setAbility(int numAbility){
        this.numHumans -= numAbility;
        return numHumans;
    }

}
