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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import tile80.behaviors80.Behavior80;
import tile80.tile80.Tile80;
import tile80.world80.World80;
import tile80.world80.World80Graph;

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
        Map<String,String> m = ImmutableMap.of();
        player=Tile80.from(20,20,"player",ImmutableSet.of("un","deux"),ImmutableSet.of(un,deux),m);
        blockUnder=Tile80.from(20,21,"blockUnder",ImmutableSet.of("trois"),ImmutableSet.of(trois),m);
        blockOver=Tile80.from(20,19,"blockOver",ImmutableSet.of("trois"),ImmutableSet.of(trois),m);
        blockintheair=Tile80.from(15,15,"blockintheair",ImmutableSet.of("trois"),ImmutableSet.of(trois),m);
        blockWall=Tile80.from(22,20,"blockWall",ImmutableSet.of("trois","deux"),ImmutableSet.of(deux,trois),m);
        tileWorld = World80Graph.builder()
                                .addTile(player)
                                .addTile(blockUnder)
                                .addTile(blockOver)
                                .addTile(blockintheair)
                                .addTile(blockWall)
                                .build();
    }
    
    @Test
    public void testGetTileByTagUn(){
        assertTrue(Iterables.contains(tileWorld.getTileByTag("un"),player));
    }
    
    @Test
    public void testGetTileByTagDeux(){
        assertTrue(Iterables.contains(tileWorld.getTileByTag("deux"),player));
        assertTrue(Iterables.contains(tileWorld.getTileByTag("deux"),blockWall));
    }
    
    @Test
    public void testGetTileByTagTrois(){
        assertTrue(Iterables.contains(tileWorld.getTileByTag("trois"),blockUnder));
        assertTrue(Iterables.contains(tileWorld.getTileByTag("trois"),blockOver));
        assertTrue(Iterables.contains(tileWorld.getTileByTag("trois"),blockintheair));
        assertTrue(Iterables.contains(tileWorld.getTileByTag("trois"),blockWall));
    }
    
    @Test
    public void testGetTileByTagNothing(){
        assertEquals(0,Iterables.size(tileWorld.getTileByTag("quatre")));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetTileByTagNull(){
        assertTrue(Iterables.contains(tileWorld.getTileByTag(null),Tile80.nothing));
    }
    
    @Test
    public void testGetTileById(){
        assertEquals(player,tileWorld.getTileById("player"));
    }
    
    @Test
    public void testGetTileByIdNothing(){
        assertEquals(Tile80.nothing,tileWorld.getTileById("gurenlagann"));
    }
    
    @Test
    public void testGetTileByIdEmpty(){
        assertEquals(Tile80.nothing,tileWorld.getTileById(""));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetTileByIdNull(){
        tileWorld.getTileById(null);
    }
    
    @Test
    public void testGetTileByRect(){
        Pair topLeft = new Pair(15,15);
        Pair bottomRight = new Pair(25,25);
        assertTrue(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), player));
        assertTrue(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockUnder));
        assertTrue(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockOver));
        assertTrue(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockintheair));
        assertTrue(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockWall));
    }
    
    @Test
    public void testGetTileByRectNothing(){
        Pair topLeft = new Pair(25,25);
        Pair bottomRight = new Pair(35,35);
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), player));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockUnder));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockOver));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockintheair));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockWall));
        assertEquals(0,Iterables.size(tileWorld.getTileByRect(topLeft,bottomRight)));
    }
    
    @Test
    public void testGetTileByRectEmpty(){
        Pair topLeft = new Pair(25,25);
        Pair bottomRight = new Pair(25,25);
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), player));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockUnder));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockOver));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockintheair));
        assertFalse(Iterables.contains(tileWorld.getTileByRect(topLeft,bottomRight), blockWall));
        assertEquals(0,Iterables.size(tileWorld.getTileByRect(topLeft,bottomRight)));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetTileByRectNull(){
        Pair topLeft = null;
        Pair bottomRight = new Pair(35,35);
        tileWorld.getTileByRect(topLeft,bottomRight);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetTileByRectNull2(){
        Pair topLeft = new Pair(25,25);
        Pair bottomRight = null;
        tileWorld.getTileByRect(topLeft,bottomRight);
    }
    
    @Test
    public void testGetTileByPos(){
        assertEquals(player,tileWorld.getTileByPos(new Pair(20,20)));
    }
    
    @Test
    public void testGetTileByPosNonExist(){
        assertEquals(Tile80.nothing,tileWorld.getTileByPos(new Pair(1000,1000)));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetTileByPosNull(){
        tileWorld.getTileByPos(null);
    }
    
    @Test
    public void testGetTagById(){
        assertEquals(player.getTags(),tileWorld.getTagById("player"));
    }
    
    @Test
    public void testGetTagByIdNonExist(){
        assertEquals(Tile80.nothing.getTags(),tileWorld.getTagById("trololo"));
    }
    
    @Test
    public void testGetTagByIdEmpty(){
        assertEquals(Tile80.nothing.getTags(),tileWorld.getTagById(""));
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetTagByIdNull(){
        tileWorld.getTagById(null);
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
    
    @Test
    public void testAddTileDuplicateId(){
        Map<String,String> m = ImmutableMap.of();
        World80 test = World80Graph.builder()
                                   .addTile(player)
                                   .addTile(Tile80.from(1, 2, "player",ImmutableSet.of("un"), ImmutableSet.of(un),m))
                                   .build();
        assertEquals(1, Iterables.size(test.getTileLst()));
    }
    
    @Test
    public void testAddTileDuplicatePos(){
        Map<String,String> m = ImmutableMap.of();
        World80 test = World80Graph.builder()
                                   .addTile(player)
                                   .addTile(Tile80.from(20, 20, 
                                                        "id",
                                                        ImmutableSet.of("un"), 
                                                        ImmutableSet.of(un),
                                                        m))
                                   .build();
        assertEquals(1, Iterables.size(test.getTileLst()));
    }
    
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    /*________________________________________________________________________*/
    
    Behavior80 un = new Behavior80(){

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
    Behavior80 deux = new Behavior80(){

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
    Behavior80 trois = new Behavior80(){

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
    Behavior80 quatre = new Behavior80(){

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
