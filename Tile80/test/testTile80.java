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

import org.javatuples.Pair;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import tile80.World80;
import tile80.World80Immutable;

/**
 *
 * @author martin
 */
public class testTile80 {
    World80 tileWorld;
    
    @Before
    public void setUp() {
        tileWorld = World80Immutable.builder()
                                    .addSymbol("player", 20, 20)
                                    .addSymbol("blockUnder", 20, 21)
                                    .addSymbol("blockOver", 20, 19)
                                    .addSymbol("blockintheair", 15, 15)
                                    .addSymbol("blockWall", 22, 20)
                                    .build();
    }
   
    @Test
    public void findBlockUnderPlayer(){
        Pair<Integer,Integer> pos = tileWorld.getCoordBySymbol("player"),
                              under = new Pair(pos.getValue0(),pos.getValue1()+1);
        String blockUnder = tileWorld.getSymbolByCoord(under);
        assertEquals("blockUnder",blockUnder);
    }
    
//    @Test
//    public void movePlayerThenCheckBlockUnder(){
//        tileWorld.moveSymbol("player", 21, 20);
//        Pair<Integer,Integer> pos = tileWorld.getCoordBySymbol("player"),
//                              under = new Pair(pos.getValue0(),pos.getValue1()+1);
//        String blockUnder = tileWorld.getSymbolByCoord(under);
//        assertEquals("",blockUnder);
//    }
    //player on a tile

    //symbol who search by tag
    //symbol who search by coordinate
    //symbol who search by range
    //symbol who search a specific symbol
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
