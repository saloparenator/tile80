/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.TileUciGame;
import tile80.Tool;
import ucigame.Image;
import ucigame.Sprite;

/**
 *
 * @author martin
 */
public class Mover extends TileUciGame{
    private static final Logger LOG = Logger.getLogger(TileUciGame.class.getName());
    
    Map <String,Sprite> mapSprite;
    int col,row;
    
    @Override
    public void setup()
    {
        col=1;row=1;
        window.size(1024, 512);
        window.title("move with arrow");
        framerate(30);
        canvas.background(0);
        
        String json = Tool.loadFileJson("data/abandonauts.json");
        System.out.println(":"+json);
        mapSprite = loadSpriteSheet(json,8,8);

    }

    @Override
    public void draw()
    {
        canvas.clear();
        
        mapSprite.get("blueBlock").position(col*8, row*8);
        mapSprite.get("blueBlock").draw();
  
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

}
