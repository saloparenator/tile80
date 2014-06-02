/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ucigame.example;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import tile80.Console80;
import tool.Json;
import ucigame.Sprite;
import ucigame.Ucigame;
import ucigame.tile80.Console80UciGame;
import ucigame.tile80.Sprite80UciGame;

/**
 *
 * @author martin
 */
public class Mover extends Ucigame{
    private static final Logger LOG = Logger.getLogger(Sprite80UciGame.class.getName());
    
    Map <String,Sprite80UciGame> mapTileSprite,
                                 mapAstroSprite,
                                 mapInterface;
    int col,row;
    
    Console80 console;
    String mode;
    
    @Override
    public void setup()
    {
        col=30;row=30;
        
        window.size(1024, 512);
        window.title("move with arrow");
        window.showFPS();
        framerate(30);
        canvas.background(0);

        mapTileSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/tiles.json"));
        mapAstroSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/astro.json"));
        mapInterface = Sprite80UciGame.makeSpriteFactoryUciGame(this, Json.loadFileJson("data/cursor.json"));
        
        console = new Console80UciGame(this, "Monospaced", 8, 255, 255, 255, 200);
        
        mode ="player";
    }

    @Override
    public void draw()
    {
            canvas.clear();
            mapAstroSprite.get("astroWalk").makeSprite(col, row).draw();
            console.draw(0, 8, 3);
    }

    @Override
    public void onKeyPress()
    {
        if (keyboard.isDown(keyboard.UP, keyboard.W))
            row--;
        if (keyboard.isDown(keyboard.DOWN, keyboard.S))
            row++;
        if (keyboard.isDown(keyboard.LEFT, keyboard.A))
            col--;
        if (keyboard.isDown(keyboard.RIGHT, keyboard.D))
            col++;
        console.addMessage("keyboard("+col+","+row+")");
     }

}
