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

package tile80;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import tool.PredicatesOnList;

/**
 *
 * @author martin
 */
public class World80HOF implements World80 {
    private final ImmutableSet<Tile80> world;
    private World80HOF(ImmutableSet<Tile80> world){
        this.world = world;
    }

    @Override
    public Pair getDefaultPos() {
        return getDefaultTile().getPos();
    }

    @Override
    public String getDefaultId() {
        return "";
    }

    @Override
    public Behavior80 getDefaultBehavior() {
        return Behavior80.nothing;
    }

    @Override
    public Tile80 getDefaultTile() {
        return Tile80.nothing;
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
    public Iterable<String> getTagById(String id) {
        return getTileById(id).getTags();
    }
    
    @Override
    public World80 crunch(Set<String> event) {
        World80HOF.Builder nextFrame = new World80HOF.Builder();
        for (Tile80 tile : getTileLst())
            for (Tile80 ntile : tile.crunch(this, event))
                if (!ntile.isNothing())
                    nextFrame.addTile(ntile);
        return nextFrame.build();
    }

    @Override
    public String getDefaultTag() {
        return "";
    }

    @Override
    public Map<String, String> getDefaultKeySpace() {
        return ImmutableMap.of();
    }

    @Override
    public Iterable<Behavior80> getBehaviorById(String id) {
        return getTileById(id).getBehavior();
    }

    @Override
    public Map<String, String> getKeySpaceById(String id) {
        ImmutableMap.Builder b = ImmutableMap.builder();
        Tile80 t = getTileById(id);
        for (String key : t.getKeyspace())
            b.put(key, t.getFromKeyspace(key));
        return b.build();
    }

    public static class Builder{
        private final ImmutableSet.Builder<Tile80> world;
        public Builder(){
            world = ImmutableSet.builder();
        }
        
        public Builder addTile(Tile80 tile){
            world.add(tile);
            return this;
        }

        public World80 build(){
            return new World80HOF(world.build());
        }
    }
}