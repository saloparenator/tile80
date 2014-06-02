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
    Map<String,Sprite> mapSprite;
    int col,row,lastcol,lastrow;
    
    Console80 console;
    String mode;
    
    @Override
    public void setup()
    {
        col=30;row=30;
        lastcol=col;
        lastrow=row;
        mapSprite = new HashMap<>();
        
        window.size(1024, 512);
        window.title("move with arrow");
        window.showFPS();
        framerate(30);
        canvas.background(0);

        mapTileSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/tiles.json"),8,8);
        mapAstroSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/astro.json"), 8, 8);
        mapInterface = Sprite80UciGame.makeSpriteFactoryUciGame(this, Json.loadFileJson("data/cursor.json"), 8, 8);
        
        console = new Console80UciGame(this, "Monospaced", 8, 255, 255, 255, 200);
        mapSprite.put("player", mapAstroSprite.get("astroWalk").makeSprite(col, row) );
        mapSprite.put("cursor", mapInterface.get("cursor").makeSprite(512,255));
        
        mode ="player";
    }

    @Override
    public void draw()
    {
            canvas.clear();
            for (Sprite spr : mapSprite.values()){
                spr.draw();
            }
            console.draw(0, 8, 3);
    }

    @Override
    public void onKeyRelease(){
        if (keyboard.isDown(keyboard.Q)){
            console.addMessage("tab pressed");
            if (mode.equals("player")){
                mode="edit";
                lastcol=col;
                lastrow=row;
                console.addMessage("player mode");
            }
            else{
                mode="player";
                col=lastcol;
                row=lastrow;
                console.addMessage("edit mode");
            }
        }
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
        console.addMessage("keyboard("+lastcol+","+lastrow+")=>("+col+","+row+")");
        if (mode.equals("player")){
           if (lastcol!=col || lastrow!=row)
                mapSprite.put("player", mapAstroSprite.get(lastcol-col>0?"astroWalk2":"astroWalk").makeSprite(col, row) );
        }
        else
        {
            mapSprite.get("cursor").nextX(col*8);
            mapSprite.get("cursor").nextY(row*8);
        }
     }

}
