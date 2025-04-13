package org.example.galaxy_trucker.Controller.Commands;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;

public class InsertTileCommand implements Command{

    private int x = -1;
    private int y = -1;
    private Player player;
    int rotation;
    String title;
    int position;

    public InsertTileCommand(Player player, int x, int y, int rotation, String title, int position) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.rotation = rotation;
        this.title = title;
        this.position = position;
    }


    @Override
    public void execute() {
        switch (title){
            case "InsertTile": {
                Tile tile = player.getCurrentTile();
                int rotations = (rotation % 360) / 90;
                for (int i = 0; i < rotations; i++) {
                    tile.RotateDx();
                }
                player.getmyPlayerBoard().insertTile(tile, x, y);
                player.setCurrentTile(null);
                break;
            }
            case "ToBuffer" :{
                Tile tile = player.getCurrentTile();
                int rotations = (rotation % 360) / 90;
                for (int i = 0; i < rotations; i++) {
                    tile.RotateDx();
                }
                player.PlaceInBuffer();
                break;
            }
            case "FromBuffer" :{

                player.SelectFromBuffer(position);
                break;
            }
            case "Pick Tile":{
                player.PickNewTile(position);
                break;
            }
            case "Discard":{
                player.DiscardTile();
            }
        }

    }
}
