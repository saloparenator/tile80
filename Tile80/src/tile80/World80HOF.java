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
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.javatuples.Pair;
import tool.PredicatesOnList;

/**
 *
 * @author martin
 */
public class World80HOF implements World80 {
    private Set<Tile80> world;
    public World80HOF(){
        world = new HashSet<>();
    }
    

    @Override
    public Iterable<String> getSymbolLst(){
        return Iterables.transform(world, onlySymbol);
    }
    @Override
    public Iterable<String> getSymbolNeighbor(String symbol){
        Preconditions.checkNotNull(symbol);
        Pair<Integer,Integer> center = getCoordBySymbol(symbol);
        Predicate inRect = Range.closed(new Pair(center.getValue0()-1,center.getValue1()-1), 
                                        new Pair(center.getValue0()+1,center.getValue1()+1));
        return FluentIterable.from(world)
                             .filter(Predicates.compose(inRect,onlyCoord))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(Range.closed(new Pair(center.getValue0()-1,center.getValue1()-1), new Pair(center.getValue0()+1,center.getValue1()+1)), onlyCoord)), onlySymbol);
    }
 
    @Override
    public Iterable<String> getSymbolLstByTag(Tag80 tag){
        Preconditions.checkNotNull(tag);
        return FluentIterable.from(world)
                             .filter(Predicates.compose(PredicatesOnList.contains(tag), onlyTag))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(PredicatesOnList.contains(tag), onlyTag)),onlySymbol);
    }

    @Override
    public String getSymbolByCoord(Pair coord){
        Preconditions.checkNotNull(coord);
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(coord), onlyCoord))
                             .first()
                             .or(Tile80.nothing)
                             .getId();
        //return Iterables.getFirst(Iterables.filter(world, Predicates.compose(Predicates.equalTo(coord), onlyCoord)),TileDefault).getSymbol();
    }
    @Override
    public Iterable<String> getSymbolByRect(Pair topLeft, Pair bottomRight){
        Preconditions.checkNotNull(topLeft,bottomRight);
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Range.closed(topLeft, bottomRight),onlyCoord))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(Range.closed(topLeft, bottomRight), onlyCoord)), onlySymbol);
    }
    @Override
    public Iterable<Tag80> getTagLst(){
        return Sets.newHashSet(FluentIterable.from(world)
                                             .transformAndConcat(onlyTag));
        //return Sets.newHashSet(Iterables.concat(Iterables.transform(world, onlyTag)));
    }
    @Override
    public Iterable<Tag80> getTagBySymbol(String symbol){
        Preconditions.checkNotNull(symbol);
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(symbol), onlySymbol))
                             .first()
                             .or(Tile80.nothing)
                             .getTags();
        //return Iterables.getFirst(Iterables.filter(world, Predicates.compose(Predicates.equalTo(symbol), onlySymbol)),TileDefault).getTag();
    }
    @Override
    public Iterable<Pair> getCoordLst(){
        return Iterables.transform(world, onlyCoord);
    }
    @Override
    public Pair getCoordBySymbol(String symbol){
        Preconditions.checkNotNull(symbol);
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(symbol), onlySymbol))
                             .first()
                             .or(Tile80.nothing)
                             .getPos();
        //return Iterables.getFirst(Iterables.filter(world, Predicates.compose(Predicates.equalTo(symbol), onlySymbol)), TileDefault).getCoord();
    }

    public World80 addTag(String symbol, Tag80 tag){
        Preconditions.checkNotNull(symbol,tag);
        Tile80 old = getTileBySymbol(symbol);
        world.remove(old);
        world.add(old.addTag(tag));
        return this;
    }

    public World80 removeTag(String symbol, Tag80 tag){
        Preconditions.checkNotNull(symbol,tag);
        Tile80 old = getTileBySymbol(symbol);
        world.remove(old);
        world.add(old.removeTag(tag));
        return this;
    }

    public World80 addSymbol(String symbol, int x, int y) {
        Preconditions.checkNotNull(symbol);
        if (Iterables.contains(getSymbolLst(),symbol))
                return this;
        List<Tag80> taglst = ImmutableList.of();
        world.add(Tile80.from(new Pair(x,y), symbol, taglst));
        return this;
    }

    public World80 removeSymbol(String symbol) {
        Preconditions.checkNotNull(symbol);
        world.remove(getTileBySymbol(symbol));
        return this;
    }
    
    /**
     * inner object used for storage
     */

    @Override
    public Pair getPairDefault() {
        return new Pair(null,null);
    }

    @Override
    public String getSymbolDefault() {
        return "";
    }

    @Override
    public Tag80 getTagDefault() {
        return Tag80.nothing;
    }

    @Override
    public Tile80 getTileDefault() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Tile80> getTileLst() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tile80 getTileByCoord(Pair pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tile80 getTileBySymbol(String Symbol) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Tile80> getTileByTag(Tag80 tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* FUNCTOR */
    private static final Function<Tile80,Iterable<Tag80>> onlyTag = new Function<Tile80,Iterable<Tag80>>(){
        @Override
        public Iterable<Tag80> apply(Tile80 input) {
            return input.getTags();
        }
    };

    private static final Function<Tile80,String> onlySymbol = new Function<Tile80,String>(){
        @Override
        public String apply(Tile80 input) {
            return input.getId();
        }
    };

    private static final Function<Tile80, Pair> onlyCoord = new Function<Tile80, Pair>() {
        @Override
        public Pair apply(Tile80 input) {
            return input.getPos();
        }
    };
}