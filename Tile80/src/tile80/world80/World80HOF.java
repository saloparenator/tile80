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

package tile80.world80;

import tile80.behaviors80.Behavior80;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import tile80.tile80.Tile80;
import tool.PredicatesOnList;

/**
 *
 * @author martin
 */
public class World80HOF extends World80 {
    private final ImmutableSet<Tile80> world;
    private World80HOF(ImmutableSet<Tile80> world){
        this.world = world;
    }

    @Override
    public Iterable<Tile80> getTileLst() {
        return world;
    }
    
    private final static Function<Tile80,Pair<Integer,Integer>> onlyCoord = new Function<Tile80, Pair<Integer, Integer>>() {
        @Override
        public Pair<Integer, Integer> apply(Tile80 input) {
            return input.getPos();
        }
    };

    @Override
    public Tile80 getTileByPos(Pair pos) {
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(pos), onlyCoord))
                             .first()
                             .or(Tile80.nothing);
    }

    private final static Function<Tile80,String> onlyId = new Function<Tile80, String>() {
        @Override
        public String apply(Tile80 input) {
            return input.getId();
        }
    };
    
    @Override
    public Tile80 getTileById(String Symbol) {
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(Symbol), onlyId))
                             .first()
                             .or(Tile80.nothing);    }

    private static final Function<Tile80,Iterable<String>> onlyTag = new Function<Tile80,Iterable<String>>(){
        @Override
        public Iterable<String> apply(Tile80 input) {
            return input.getTags();
        }   
    };
    
    @Override
    public Iterable<Tile80> getTileByTag(String tag) {
        return FluentIterable.from(world)
                             .filter(Predicates.compose(PredicatesOnList.contains(tag),onlyTag));          
    }

    @Override
    public Iterable<Tile80> getTileByRect(Pair<Integer, Integer> topLeft, Pair<Integer, Integer> bottomRight) {
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Range.closed(topLeft, bottomRight), onlyCoord));
    }

    @Override
    public Pair<Integer, Integer> getPosById(String id) {
        return getTileById(id).getPos();
    }

    @Override
    public Set<String> getTagById(String id) {
        return getTileById(id).getTags();
    }

    @Override
    public World80 crunch(Set<String> event) {
        World80.Builder nextFrame = World80HOF.builder();
        return crunchWith(nextFrame, event);
    }

    @Override
    public Iterable<Behavior80> getBehaviorById(String id) {
        return getTileById(id).getBehavior();
    }

    @Override
    public Map<String, String> getKeySpaceById(String id) {
        ImmutableMap.Builder b = ImmutableMap.builder();
        Tile80 t = getTileById(id);
        for (String key : t.getKeyspace().keySet())
            b.put(key, t.getFromKeyspace(key));
        return b.build();
    }

    public static Builder builder(){
        return new HOFBuilder();
    }
    public static class HOFBuilder implements World80.Builder{
        private final ImmutableSet.Builder<Tile80> world;
        public HOFBuilder(){
            world = ImmutableSet.builder();
        }
        
        @Override
        public Builder addTile(Tile80 tile){
            world.add(tile);
            return this;
        }

        @Override
        public World80 build(){
            return new World80HOF(world.build());
        }
    }
}