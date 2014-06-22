/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ucigame.example;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.Console80;
import tile80.behaviors80.Behavior80;
import tile80.behaviors80.Gof;
import tile80.tile80.Tile80;
import tile80.world80.World80;
import tile80.world80.World80Graph;
import tile80.world80.World80HOF;
import tool.Json;
import tool.Yield;
import ucigame.Ucigame;
import ucigame.tile80.Console80UciGame;
import ucigame.tile80.Sprite80UciGame;

/**
 *
 * @author martin
 */
public class Gof2 extends Ucigame{

    Behavior80 gof = new Gof();

    private static final Logger LOG = Logger.getLogger(Sprite80UciGame.class.getName());
    
    Map <String,Sprite80UciGame> mapSprite;
    
    World80 world;
    Set<Pair> click;
    Set<String> event;
    boolean pause;
    Console80 console;
    @Override
    public void setup()
    {
        window.size(1024, 512);
        window.title("space bar to pause");
        framerate(30);
        canvas.background(0);
        console = new Console80UciGame(this, "Monospaced", 8, 255, 255, 255, 200);
        
        window.showFPS();
        mapSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/tiles.json"));
        world = World80.load(World80HOF.builder(),Json.loadFileJson("data/gof.json"));
       
        click = Collections.newSetFromMap(new ConcurrentHashMap<Pair,Boolean>());
        pause=false;
        keyboard.typematicOff();
        event=ImmutableSet.of();
    }

    static Pair<Integer,Integer> topLeft = new Pair(0,0);
    static Pair<Integer,Integer> bottomRight = new Pair(128,64);
    @Override
    public void draw()
    {
        canvas.clear();
        for (Pair<Integer,Integer> p : FluentIterable.from(world.getTileByRect(topLeft, bottomRight)).transform(Gof.onlyCoord)){
            mapSprite.get("greenBlock1").makeSprite(p.getValue0(), p.getValue1()).draw();
        }
        for (Pair<Integer,Integer> p : click){
            mapSprite.get("blueBlock1").makeSprite(p.getValue0(), p.getValue1()).draw();
        }
        console.addMessage("size:"+Iterables.size(world.getTileLst()));
        console.draw(0, 8, 3);
        
        if(!pause){
            world = world.crunch(event);
        }
    }

    @Override
    public void onKeyPress()
    {
        if (keyboard.isDown(keyboard.SPACE))
            pause = !pause;
        if(pause){
            console.addMessage("paused");
            for (Tile80 t : world.getTileLst())
                click.add(t.getPos());
        }
        else{
            console.addMessage("play");
            World80Graph.Builder b = World80Graph.builder();
            for (Pair p : click)
                b.addTile(Tile80.from(p,
                                      ""+p.hashCode(),
                                      ImmutableSet.of("gof"), 
                                      ImmutableSet.of(gof),
                                      ImmutableMap.of("self","wink")));
            click.clear();
            world = b.build();
        }
    }
    
    @Override
    public void onMousePressed()
    {
        Pair p = new Pair(mouse.x()/8,mouse.y()/8);
        if (click.contains(p)){
            click.remove(p);
        }
        else{
            click.add(p);
        }
    }
 
}
