package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class PrepController {

    Player CurrentPlayer;

    public void PickTile(){
        CurrentPlayer.PickNewTile();
    }

    public void PickTile(int index){
        CurrentPlayer.PickNewTile(index);
    }

    public void TakeFromBuffer(int index){
        CurrentPlayer.SelectFromBuffer(index);
    }

    public void PlaceInBuffer(){
        CurrentPlayer.PlaceInBuffer();
    }

    public void DiscardTile(){
        CurrentPlayer.DiscardTile();
    }

    public void RotateL(){
        CurrentPlayer.LeftRotate();
    }

    public void RotateR(){
        CurrentPlayer.RightRotate();
    }


    public void PlaceTile(IntegerPair coords){
        CurrentPlayer.PlaceTile(coords);
    }

    public void StartTimer() throws InterruptedException {
        CurrentPlayer.StartTimer();
    }




}
