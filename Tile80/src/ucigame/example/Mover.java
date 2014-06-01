/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ucigame.example;

import java.util.Map;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.SpriteFactory;
import ucigame.tile80.SpriteFactoryUciGame;
import tool.Json;
import ucigame.Sprite;
import ucigame.Ucigame;

/**
 *
 * @author martin
 */
public class Mover extends Ucigame{
    private static final Logger LOG = Logger.getLogger(SpriteFactoryUciGame.class.getName());
    
    Map <String,SpriteFactoryUciGame> mapTileSprite,
                        mapAstroSprite;
    int col,row;
    Sprite button;
    
    @Override
    public void setup()
    {
        col=1;row=1;
        window.size(1024, 512);
        window.title("move with arrow");
        framerate(15);
        canvas.background(0);
        
        String json = Json.loadFileJson("data/tiles.json");
        mapTileSprite = SpriteFactoryUciGame.makeSpriteMapFactoryUciGame(this,json,8,8);
        mapAstroSprite = SpriteFactoryUciGame.makeSpriteMapFactoryUciGame(this,Json.loadFileJson("data/astro.json"), 8, 8);
//        
//                for(String s : arrayOfAvailableFonts())
//        System.out.println(s);
                
        canvas.font("Monospaced", 0, 8,255,255,255);
    }

    @Override
    public void draw()
    {
        canvas.clear();
        
        Sprite tmp =  mapAstroSprite.get("astroWalk").makeSprite(col*8, row*8);
        tmp.draw();
        canvas.putText("button", 100,100);
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
    }

        @Override
    public void onMousePressed()
    {
        //click.add(new Pair(mouse.x()/8,mouse.y()/8));
    }
}
