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

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import java.util.Set;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tile80.Tag80;
import tile80.Tile80;
import tile80.World80;
import tile80.World80Graph;

/**
 *
 * @author martin
 */
public class testTile80 {
    World80 tileWorld;
    Tile80 player,
           blockUnder,
           blockOver,
           blockintheair,
           blockWall;

        
    @Before
    public void setUp() {
        player=Tile80.from(20,20,"player",ImmutableSet.of(un,deux));
        blockUnder=Tile80.from(20,21,"blockUnder",ImmutableSet.of(trois));
        blockOver=Tile80.from(20,19,"blockOver",ImmutableSet.of(trois));
        blockintheair=Tile80.from(15,15,"blockintheair",ImmutableSet.of(trois));
        blockWall=Tile80.from(22,20,"blockWall",ImmutableSet.of(deux,trois));
        tileWorld = World80Graph.builder()
                                .addTile(player)
                                .addTile(blockUnder)
                                .addTile(blockOver)
                                .addTile(blockintheair)
                                .addTile(blockWall)
                                .build();
    }

    @Test
    public void text(){
        Predicate r = Range.closed(new Pair(2,2),new Pair(4,4));
        for (int x=0;x<6;x++)
            for(int y=0;y<6;y++)
                System.out.println(x+","+y+" "+r.apply(new Pair(x,y)));
    }
    
    public void t(){
        tileWorld.getTagById(null);
        tileWorld.getTileByPos(null);
        tileWorld.getTileByRect(null, null);
        tileWorld.getTileById(null);
        tileWorld.getTileByTag(Tag80.nothing);
        tileWorld.getTileLst();
    }
    
    @Test
    public void testGetPosById(){
        Assert.assertEquals(player.getPos(),tileWorld.getPosById("player"));
    }
    
    @Test
    public void testGetPosByIdNonExist(){
        Assert.assertEquals(Tile80.nothing.getPos(),tileWorld.getPosById("kamina"));
    }
    
    @Test
    public void testGetPosByIdEmpty(){
        Assert.assertEquals(Tile80.nothing.getPos(),tileWorld.getPosById(""));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetPosByIdNull(){
        tileWorld.getPosById(null);
    }
    
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    
    Tag80 un = new Tag80(){

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world,Set<String> event) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
           
    };
    Tag80 deux = new Tag80(){

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world,Set<String> event) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    Tag80 trois = new Tag80(){

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world,Set<String> event) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    };
    
}
