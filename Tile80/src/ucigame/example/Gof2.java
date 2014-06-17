/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ucigame.example;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.Console80;
import tile80.Tag80;
import tile80.Tile80;
import tile80.World80;
import tile80.World80Graph;
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
   
    public static class Neighbor extends Yield<Pair<Integer,Integer>>{
        private final Pair<Integer,Integer> center;
        int i;
        public Neighbor(Pair center){
            this.center=center;
            i=0;
        }
        @Override
        public boolean end() {
            return i>8;
        }
        @Override
        public Pair<Integer,Integer> yield() {
            Pair p = new Pair(center.getValue0()+i%3-1,
                              center.getValue1()+i/3-1);
            i += 1+(i==3?1:0);
            return p;
        }
        
    };
    static Function<Tile80,Pair<Integer,Integer>> onlyCoord = new Function<Tile80, Pair<Integer, Integer>>() {
        @Override
        public Pair<Integer, Integer> apply(Tile80 input) {
            return input.getPos();
        }
    };
    
    public static int countAliveNeighbor(Pair<Integer,Integer> pos, World80 world){
        int n=0;
        for (Pair around : new Neighbor(pos))
            if(!Tile80.nothing.equals(world.getTileByPos(around)))
                n++;
        return n;   
    }
    
    Tag80 alive = new Tag80(){

        @Override
        public String getName() {
            return "alive";
        }

        @Override
        public String getDescription() {
            return "is alive";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            ImmutableSet.Builder<Tile80> t = ImmutableSet.builder();

            int n = countAliveNeighbor(self.getPos(),world);
            if (n==3 || n==2)
                t.add(self);
            
            for(Pair<Integer,Integer> pos : new Neighbor(self.getPos())){
                if (countAliveNeighbor(pos,world)==3){
                    t.add(Tile80.from(pos, ""+pos.hashCode(), ImmutableSet.of(alive)));
                }
            }
            
            return t.build();
        }
        
    };

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
        framerate(15);
        canvas.background(0);
        console = new Console80UciGame(this, "Monospaced", 8, 255, 255, 255, 200);
        
        window.showFPS();
        mapSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/tiles.json"));
        world = new World80Graph.Builder()
                            .addTile(Tile80.from(10, 10, "a", ImmutableSet.of(alive)))
                            .addTile(Tile80.from(10, 9, "b", ImmutableSet.of(alive)))
                            .addTile(Tile80.from(10, 8, "c", ImmutableSet.of(alive)))
                            .addTile(Tile80.from(9, 10, "d", ImmutableSet.of(alive)))
                            .addTile(Tile80.from(8, 9, "e", ImmutableSet.of(alive)))
                            .build();
        click = Collections.newSetFromMap(new ConcurrentHashMap<Pair,Boolean>());
        pause=false;
        keyboard.typematicOff();
        event=ImmutableSet.of();
    }

    @Override
    public void draw()
    {
        canvas.clear();
        for (Pair<Integer,Integer> p : FluentIterable.from(world.getTileLst()).transform(onlyCoord)){
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
                b.addTile(Tile80.from(p,""+p.hashCode(),ImmutableSet.of(alive)));
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
