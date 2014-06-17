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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.Console80;
import ucigame.tile80.Sprite80UciGame;
import tool.Json;
import tool.Yield;
import ucigame.Ucigame;
import ucigame.tile80.Console80UciGame;

/**
 *
 * @author martin
 */
public class Gof extends Ucigame{
    public static class Neighbor extends Yield<Pair>{
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
        public Pair yield() {
            Pair p = new Pair(center.getValue0()+i%3-1,
                              center.getValue1()+i/3-1);
            i += 1+(i==3?1:0);
            return p;
        }
        
    };
    public static Set<Pair> iterate(final Set<Pair> world)
    {
        Function<Pair, Iterable<Pair>> getAliveNeighbor = new Function<Pair, Iterable<Pair>>(){
            @Override
            public Iterable<Pair> apply(Pair input) {
                return FluentIterable.from(new Neighbor(input))
                                     .filter(Predicates.not(Predicates.in(world)));
            }
        };
        Predicate<Pair> ifThereIsReproduction = new Predicate<Pair>(){
            @Override
            public boolean apply(Pair input) {
                return FluentIterable.from(new Neighbor(input))
                                     .filter(Predicates.in(world))
                                     .size()==3;
            }
        };
        Predicate<Pair> ifThereIsSurvivor = new Predicate<Pair>(){
            @Override
            public boolean apply(Pair input) {
                int count = FluentIterable.from(new Neighbor(input))
                                          .filter(Predicates.in(world))
                                          .size();
                return count==3 || count==2;
            }
        };

        Set s = ImmutableSet.builder()
                            .addAll(FluentIterable.from(world)
                                                  .transformAndConcat(getAliveNeighbor)
                                                  .filter(ifThereIsReproduction))
                            .addAll(FluentIterable.from(world)
                                                  .filter(ifThereIsSurvivor))
                            .build();
        return s;
    }
    
    private static final Logger LOG = Logger.getLogger(Sprite80UciGame.class.getName());
    
    Map <String,Sprite80UciGame> mapSprite;
    
    Set<Pair> world;
    Set<Pair> click;
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
        
        mapSprite = Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/tiles.json"));
        world = ImmutableSet.of();
        click = new HashSet<>();
        pause=false;
        keyboard.typematicOff();
    }

    @Override
    public void draw()
    {
        canvas.clear();
        for (Pair<Integer,Integer> p : world)
            mapSprite.get("greenBlock1").makeSprite(p.getValue0(), p.getValue1()).draw();
        for (Pair<Integer,Integer> p : click)
            mapSprite.get("blueBlock1").makeSprite(p.getValue0(), p.getValue1()).draw();
        console.draw(0, 8, 3);
        
        if(!pause){
            ImmutableSet.Builder b = ImmutableSet.builder();
            b.addAll(click);
            click.clear();
            b.addAll(iterate(world));
            world=b.build();
        }
    }

    @Override
    public void onKeyPress()
    {
        if (keyboard.isDown(keyboard.SPACE))
            pause = !pause;
        if(pause)
            console.addMessage("paused");
        else
            console.addMessage("play");
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
