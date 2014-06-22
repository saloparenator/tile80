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
import java.util.HashSet;
import java.util.Set;
import tile80.Console80;
import tile80.tile80.Tile80;
import tile80.world80.World80;
import tile80.world80.World80Graph;
import tile80.behaviors80.Activator;
import tile80.behaviors80.Beam;
import tile80.behaviors80.BeamEmitter;
import tile80.behaviors80.Behavior80;
import tile80.behaviors80.Mover;
import tile80.behaviors80.Switches;
import tile80.world80.World80HOF;
import tool.Json;
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
    
    Behavior80 mover = new Mover();
    Behavior80 activator = new Activator();
    Behavior80 switche = new Switches();
    Behavior80 beamEmitter = new BeamEmitter();
    Behavior80 beam = new Beam();

    
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

        world = World80.load(World80HOF.builder(),Json.loadFileJson("data/doorDemo.json"));        
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
