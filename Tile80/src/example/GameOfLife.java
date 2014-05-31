/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.Tile;
import tile80.TileUciGame;
import tile80.Tool;
import ucigame.Sprite;
import ucigame.Ucigame;

/**
 *
 * @author martin
 */
public class GameOfLife extends TileUciGame{
    public static Set<Pair> iterate(final Set<Pair> world)
    {
        Function<Pair, Iterable<Pair>> getAliveNeighbor = new Function<Pair, Iterable<Pair>>(){
            @Override
            public Iterable<Pair> apply(Pair input) {
                return Iterables.filter(Tile.getNeighbor(input),Predicates.not(Predicates.in(world)));
            }
        };
        Predicate<Pair> ifThereIsReproduction = new Predicate<Pair>(){
            @Override
            public boolean apply(Pair input) {
                int count = Iterables.size(
                            Iterables.filter(Tile.getNeighbor(input),
                                             Predicates.in(world)));
                return count ==3;
            }
        };
        Predicate<Pair> ifThereIsSurvivor = new Predicate<Pair>(){
            @Override
            public boolean apply(Pair input) {
                int count = Iterables.size(
                            Iterables.filter(Tile.getNeighbor(input),
                                             Predicates.in(world)));
                return count==3 || count==2;
            }
        };
        
        ImmutableSet.Builder b = ImmutableSet.builder();

        b.addAll(Iterables.filter(Iterables.concat(Iterables.transform(world, getAliveNeighbor)),ifThereIsReproduction));
        b.addAll(Iterables.filter(world, ifThereIsSurvivor));
        
        return b.build();
    }
    
    private static final Logger LOG = Logger.getLogger(TileUciGame.class.getName());
    
    Map <String,Sprite> mapSprite;
    
    Set<Pair> world;
    Collection<Pair> click;
    boolean pause;
    
    @Override
    public void setup()
    {
        window.size(1024, 512);
        window.title("space bar to pause");
        framerate(5);
        canvas.background(0);
        
        String json = Tool.loadFileJson("data/abandonauts.json");
        mapSprite = loadSpriteSheet(json,8,8);
        
        world = ImmutableSet.of();
        click = new ArrayList<>();
        pause=false;
    }

    @Override
    public void draw()
    {
        canvas.clear();
        
        ImmutableSet.Builder b = ImmutableSet.builder();
        for (Pair<Integer,Integer> p : world)
        {
            mapSprite.get("greenBlock").position(p.getValue0()*8, p.getValue1()*8);
            mapSprite.get("greenBlock").draw();
        }

        if(!pause)
        {
            b.addAll(Iterables.filter(GameOfLife.iterate(world), Range.closed(new Pair(8,8), new Pair(120,56))));
        }
        else
        {
            b.addAll(world);
        }
        
        for (Pair p : click)
            b.add(p);
        click.clear();
        world=b.build();
    }

    @Override
    public void onKeyPress()
    {
        if (keyboard.isDown(keyboard.SPACE))
            pause = !pause;
    }
    
    @Override
    public void onMousePressed()
    {
        click.add(new Pair(mouse.x()/8,mouse.y()/8));
    }
 
}
