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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import tile80.Console80;
import tile80.Tag80;
import tile80.World80;
import tile80.World80Immutable;
import tool.Json;
import tool.Yield;
import ucigame.Ucigame;
import ucigame.tile80.Console80UciGame;
import ucigame.tile80.Sprite80UciGame;

/**
 *
 * @author martin
 */
public class Mover2 extends Ucigame{
    Map <String,Sprite80UciGame> mapTileSprite,
                                 mapAstroSprite,
                                 mapInterface;
    int col,row;
    Console80 console;
    World80 world;
    Set<String> event;
    
    Tag80<Triplet> player = new Tag80<Triplet>(){
        @Override
        public String getName() {
            return "player";
        }

        @Override
        public String getDescription() {
            return "the player move with the keyboard";
        }

        @Override
        public Triplet crunch(Triplet self, World80 world) {
            int x=0,y=0;
            for (String e : event){
                if ("up".equals(e))
                    y--;
                else if ("down".equals(e))
                    y++;
                else if ("left".equals(e))
                    x--;
                else if ("right".equals(e))
                    x++;
            }
            Pair<Integer,Integer> tmp = (Pair<Integer,Integer>) self.getValue(2);
            return new Triplet(self.getValue(0), 
                               self.getValue(1), 
                               new Pair(tmp.getValue0()+x,
                                        tmp.getValue1()+y));
        }

        @Override
        public Iterable<Triplet> spawn(Triplet self, World80 world) {
            return Yield.empty;
        }          
    };
    
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
        
        world = World80Immutable.builder()
                               .addSymbol("player", 30, 30)
                               .addTag("player", player)
                               .build();
        event = new HashSet();
    }

    @Override
    public void draw()
    {
        World80Immutable.Builder b = World80Immutable.builder();
        Triplet t = new Triplet(null,null,null);
        for(Tag80<Triplet> tag : world.getTagBySymbol("player")){
            t = new Triplet("player",
                            tag,
                            world.getCoordBySymbol("player"));
            t = tag.crunch(t, world);
        }
        Pair<Integer,Integer> nupos = (Pair)t.getValue2();
        b.addSymbol("player", nupos.getValue0(), nupos.getValue1());
        b.addTag("player", player);
        world = b.build();
        event.clear();
        
        canvas.clear();
        Pair<Integer,Integer> pos = world.getCoordBySymbol("player");
        mapAstroSprite.get("astroWalk").makeSprite(pos.getValue0(), pos.getValue1()).draw();
        
        console.draw(0, 8, 3);
    }

    @Override
    public void onKeyPress()
    {
        if (keyboard.isDown(keyboard.UP, keyboard.W))
            event.add("up");
        if (keyboard.isDown(keyboard.DOWN, keyboard.S))
            event.add("down");;
        if (keyboard.isDown(keyboard.LEFT, keyboard.A))
            event.add("left");;
        if (keyboard.isDown(keyboard.RIGHT, keyboard.D))
            event.add("right");;
     }

}
