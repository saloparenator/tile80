/*
 * Copyright 2014 martin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ucigame.example;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import tile80.Console80;
import tile80.Behavior80;
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
public class Door extends Ucigame{
    ImmutableMap <String,Sprite80UciGame> mapSprite;
    int col,row;
    Console80 console;
    World80 world;
    Set<String> event;
    int i;
    boolean spacebar;
    
    Behavior80 mover = new Behavior80(){
        @Override
        public String getName() {
            return "player";
        }

        @Override
        public String getDescription() {
            return "the player move with the keyboard";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world,Set<String> event) {
            Tile80 crunching = self;
            for (String e : event){
                if ("up".equals(e) && 
                    world.getTileByPos(new Pair(self.getX(),self.getY()-1)).isNothing())
                    crunching = crunching.movePos(0, -1);
                else if ("down".equals(e) && 
                    world.getTileByPos(new Pair(self.getX(),self.getY()+1)).isNothing())
                    crunching = crunching.movePos(0, 1);
                else if ("left".equals(e) && 
                    world.getTileByPos(new Pair(self.getX()-1,self.getY())).isNothing())
                    crunching = crunching.movePos(-1, 0);
                else if ("right".equals(e) && 
                    world.getTileByPos(new Pair(self.getX()+1,self.getY())).isNothing())
                    crunching = crunching.movePos(1, 0);
            }
            return ImmutableSet.of(crunching);
        }     
    };
    
    Behavior80 activator = new Behavior80() {

        @Override
        public String getName() {
            return "activator";
        }

        @Override
        public String getDescription() {
            return "just for switch";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            if (event.contains("activate"))
                return ImmutableSet.of(self.addTag("activate"));
            return ImmutableSet.of(self.removeTag("activate"));
        }
    };
    
    Behavior80 switche = new Behavior80() {
        @Override
        public String getName() {
            return "switch";
        }

        @Override
        public String getDescription() {
            return "your can turn it on and off";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            Pair<Integer,Integer> topLeft = new Pair(self.getX()-1,self.getY()-1),
                                  bottomRight = new Pair(self.getX()+1,self.getY()+1);
            Tile80 swap;
            if (Iterables.contains(self.getTags(),"on"))
                swap = self.addTag("off").removeTag("on").addKey("sprite", "powerlightOff");
            else
                swap = self.addTag("on").removeTag("off").addKey("sprite", "powerlightOn");
            for (Tile80 tile:world.getTileByRect(topLeft, bottomRight)){
                if (Iterables.contains(tile.getTags(),"activate")){
                    return ImmutableSet.of(swap);
                }
            }
            return ImmutableSet.of(self);
        }
    };
    
    Behavior80 beamEmitter = new Behavior80() {
        @Override
        public String getName() {
            return "beamEmitter";
        }

        @Override
        public String getDescription() {
            return "it emmit a beam and listen to a switch @switch";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            Tile80 sw = world.getTileById(self.getFromKeyspace("switch"));
            if (Iterables.contains(sw.getTags(),"on")){
                return ImmutableSet.of(self.addTag("emit"),
                                       Tile80.newEmpty("beam")
                                             .setPos(self.getX()+Integer.parseInt(self.getFromKeyspace("rx")),
                                                     self.getY()+Integer.parseInt(self.getFromKeyspace("ry")))
                                             .addKey("sprite", "beamVertical")
                                             .addBehavior(beam));
            }
            return ImmutableSet.of(self.removeTag("emit"));
        }
    };
    Behavior80 beam = new Behavior80() {
        @Override
        public String getName() {
            return "beam";
        }

        @Override
        public String getDescription() {
            return "beam";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            return ImmutableSet.of();
        }
    };

    
    @Override
    public void setup()
    {
        spacebar=false;
        col=30;row=30;
        
        window.size(1024, 512);
        window.title("move with arrow");
        window.showFPS();
        framerate(30);
        canvas.background(0);

        ImmutableMap tmp = ImmutableMap.builder()
                                .putAll(Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/tiles.json")))
                                .putAll(Sprite80UciGame.makeSpriteFactoryUciGame(this,Json.loadFileJson("data/astro.json")))
                                .putAll(Sprite80UciGame.makeSpriteFactoryUciGame(this, Json.loadFileJson("data/cursor.json")))
                                .putAll(Sprite80UciGame.makeSpriteFactoryUciGame(this, Json.loadFileJson("data/powerlight.json")))
                                .putAll(Sprite80UciGame.makeSpriteFactoryUciGame(this, Json.loadFileJson("data/beam.json")))
                                .build();
        mapSprite=tmp;

        console = new Console80UciGame(this, "Monospaced", 8, 255, 255, 255, 200);
        
        World80Graph.Builder builder = World80Graph.builder();
        builder.addTile(Tile80.newEmpty("player")
                              .setPos(30, 30)
                              .addBehavior(mover)
                              .addBehavior(activator)
                              .addKey("sprite", "astroWalk"));
        builder.addTile(Tile80.newEmpty("switch")
                              .setPos(25, 39)
                              .addTag("off")
                              .addBehavior(switche)
                              .addKey("sprite", "powerlightOff"));
        builder.addTile(Tile80.newEmpty("beamEmitter")
                              .setPos(10, 29)
                              .addKey("sprite", "blueBlock6")
                              .addKey("switch","switch")
                              .addKey("rx","0")
                              .addKey("ry", "1")
                              .addKey("beam","beam")
                              .addTag("emit")
                              .addBehavior(beamEmitter));
        builder.addTile(Tile80.newEmpty("beam")
                              .setPos(10, 30)
                              .addBehavior(beam)
                              .addKey("sprite", "beamVertical")
                              .addKey("emiter","beamEmitter"));
        //blueBlock6
        for (int x=10;x<=50;x++)
            for(int y=10;y<=40;y++)
                if((x==10&&y!=30&&y!=29) || y==10 || x==50 || y==40){
                    Pair pos = new Pair(x,y);
                    builder = builder.addTile(Tile80.newEmpty(Integer.toHexString(pos.hashCode()))
                                                    .setPos(pos)
                                                    .addTag("wall")
                                                    .addKey("sprite", "greenBlock1"));
                }
        world=builder.build();
        
        event = new HashSet();
        
        i=0;
    }

    @Override
    public void draw()
    {
        world = world.crunch(event);
        event.clear();
        console.addMessage("frame "+(i++));
        canvas.clear();
        for (Tile80 tile : world.getTileLst()){
            mapSprite.get(tile.getFromKeyspace("sprite"))
                     .makeSprite(tile.getX(), tile.getY()).draw();
        }
        console.draw(0, 8, 3);
    }

    @Override
    public void onKeyRelease(){
        if (!keyboard.isDown(keyboard.SPACE)){
            spacebar=false;
        }
    }
    
    @Override
    public void onKeyPress(){
        if (!spacebar && keyboard.isDown(keyboard.SPACE)){
                spacebar=true;
                event.add("activate");
        }
        if (keyboard.isDown(keyboard.UP, keyboard.W))
            event.add("up");
        if (keyboard.isDown(keyboard.DOWN, keyboard.S))
            event.add("down");
        if (keyboard.isDown(keyboard.LEFT, keyboard.A))
            event.add("left");
        if (keyboard.isDown(keyboard.RIGHT, keyboard.D))
            event.add("right");
     }

}
