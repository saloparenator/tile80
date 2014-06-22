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

package tile80.behaviors80;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.javatuples.Pair;
import tile80.tile80.Tile80;
import tile80.world80.World80;
import tool.Yield;
import ucigame.example.Gof2;

/**
 *
 * @author martin
 */
public class Gof extends Behavior80{
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
    public static Function<Tile80,Pair<Integer,Integer>> onlyCoord = new Function<Tile80, Pair<Integer, Integer>>() {
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
    
    @Override
    public String getName() {
        return "gof";
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
                t.add(Tile80.from(pos, 
                                  ""+pos.hashCode(), 
                                  ImmutableSet.of("gof"),
                                  ImmutableSet.of((Behavior80)this),
                                  ImmutableMap.of("","")));
            }
        }

        return t.build();
    }
}
