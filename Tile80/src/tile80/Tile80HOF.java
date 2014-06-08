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
public class Tile80HOF implements Tile80 {
    private Set<Tile> world;
    public Tile80HOF(){
        world = new HashSet<>();
    }
    

    @Override
    public Iterable<String> getSymbolLst(){
        return Iterables.transform(world, onlySymbol);
    }
    @Override
    public Iterable<String> getSymbolNeighbor(String symbol){
        Pair<Integer,Integer> center = getCoordBySymbol(symbol);
        Predicate inRect = Range.closed(new Pair(center.getValue0()-1,center.getValue1()-1), 
                                        new Pair(center.getValue0()+1,center.getValue1()+1));
        return FluentIterable.from(world)
                             .filter(Predicates.compose(inRect,onlyCoord))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(Range.closed(new Pair(center.getValue0()-1,center.getValue1()-1), new Pair(center.getValue0()+1,center.getValue1()+1)), onlyCoord)), onlySymbol);
    }
    @Override
    public Iterable<String> getSymbolLstByTagLst(Collection<String> taglst){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(PredicatesOnList.containsAll(taglst),onlyTag))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(PredicatesOnList.containsAll(taglst),onlyTag)), onlySymbol);
    }
    @Override
    public Iterable<String> getSymbolLstByTag(String tag){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(PredicatesOnList.contains(tag), onlyTag))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(PredicatesOnList.contains(tag), onlyTag)),onlySymbol);
    }
    @Override
    public Iterable<String> getSymbolLstByAnyTag(Collection<String> tag){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(PredicatesOnList.containsOne(tag),onlyTag))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(PredicatesOnList.containsOne(tag),onlyTag)),onlySymbol);
    }
    @Override
    public String getSymbolByCoord(Pair coord){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(coord), onlyCoord))
                             .first()
                             .or(TileDefault)
                             .getSymbol();
        //return Iterables.getFirst(Iterables.filter(world, Predicates.compose(Predicates.equalTo(coord), onlyCoord)),TileDefault).getSymbol();
    }
    @Override
    public Iterable<String> getSymbolByRect(Pair topLeft, Pair bottomRight){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Range.closed(topLeft, bottomRight),onlyCoord))
                             .transform(onlySymbol);
        //return Iterables.transform(Iterables.filter(world, Predicates.compose(Range.closed(topLeft, bottomRight), onlyCoord)), onlySymbol);
    }
    @Override
    public Iterable<String> getTagLst(){
        return Sets.newHashSet(FluentIterable.from(world)
                                             .transformAndConcat(onlyTag));
        //return Sets.newHashSet(Iterables.concat(Iterables.transform(world, onlyTag)));
    }
    @Override
    public Iterable<String> getTagBySymbol(String symbol){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(symbol), onlySymbol))
                             .first()
                             .or(TileDefault)
                             .getTag();
        //return Iterables.getFirst(Iterables.filter(world, Predicates.compose(Predicates.equalTo(symbol), onlySymbol)),TileDefault).getTag();
    }
    @Override
    public Iterable<Pair> getCoordLst(){
        return Iterables.transform(world, onlyCoord);
    }
    @Override
    public Pair getCoordBySymbol(String symbol){
        return FluentIterable.from(world)
                             .filter(Predicates.compose(Predicates.equalTo(symbol), onlySymbol))
                             .first()
                             .or(TileDefault)
                             .getCoord();
        //return Iterables.getFirst(Iterables.filter(world, Predicates.compose(Predicates.equalTo(symbol), onlySymbol)), TileDefault).getCoord();
    }

    @Override
    public synchronized Tile80 moveSymbol(String symbol, int x, int y){
        Tile old = getTileBySymbol(symbol);
        world.remove(old);
        world.add(old.move(x, y));
        return this;
    }
    
    @Override
    public synchronized Tile80 addTag(String symbol, String tag){
        Tile old = getTileBySymbol(symbol);
        world.remove(old);
        world.add(old.addTag(tag));
        return this;
    }
    
    @Override
    public synchronized Tile80 removeTag(String symbol, String tag){
        Tile old = getTileBySymbol(symbol);
        world.remove(old);
        world.add(old.removeTag(tag));
        return this;
    }
    
    @Override
    public Tile80 addSymbol(String symbol, int x, int y) {
        if (Iterables.contains(getSymbolLst(),symbol))
                return this;
        List<String> taglst = ImmutableList.of();
        world.add(new Tile(new Pair(x,y), new Pair(x,y), symbol, taglst));
        return this;
    }

    @Override
    public Tile80 removeSymbol(String symbol) {
        world.remove(getTileBySymbol(symbol));
        return this;
    }
    
    /**
     * inner object used for storage
     */
    private static final Tile TileDefault = new Tile(new Pair(0,0), new Pair(0,0),"", ImmutableList.of(""));


    private static final class Tile {
        private final Pair<Integer,Integer> coord;
        private final Pair<Integer,Integer> last;
        private final String symbol;
        private final Collection<String> tag;

        /**
         * constructor
         * @param coord
         * @param symbol
         * @param tag 
         */
        private Tile(Pair coord,Pair last, String symbol, Collection<String> tag) {
            this.coord=coord;
            this.last=coord;
            this.symbol = symbol;
            this.tag = tag;
        }

        public static Collection<Tile> fromJson(String json){
            ImmutableSet.Builder<Tile> builder = ImmutableSet.builder();

            Collection<Map<String,Object>> tileSet = new Gson().fromJson(json,Collection.class);
            for (Map<String,Object> tile : tileSet){
                Pair coord = new Pair(((Double)tile.get("x")).intValue(),
                                      ((Double)tile.get("y")).intValue());
                builder.add(new Tile(coord,
                                     coord,
                                     (String)tile.get("symbol"),
                                     (Collection)tile.get("tag")));
            }

            return builder.build();
        }

        /**
         * current coordinate
         * @return 
         */
        public Pair getCoord(){
            return coord;
        }

        /**
         * last coordinate since translation
         * @return 
         */
        public Pair getLast(){
            return last;
        }

        /**
         * the symbol to identify the tile
         * @return 
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * all tag for this tile
         * @return 
         */
        public Collection<String> getTag() {
            return tag;
        }

            @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + Objects.hashCode(this.coord);
            hash = 53 * hash + Objects.hashCode(this.last);
            hash = 53 * hash + Objects.hashCode(this.symbol);
            hash = 53 * hash + Objects.hashCode(this.tag);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            else if(obj==this){
                return true;
            }
            else if (obj instanceof Tile) {
                final Tile other = (Tile) obj;
                return other.coord.equals(coord)&&
                        other.symbol.equals(symbol)&&
                        other.tag.containsAll(tag);
            }
            return false;
        }

        @Override
        public String toString(){
            return "Tile{" + "coord=" + coord + ", symbol=" + symbol + ", tag=" + tag + '}';
        }

        /**
         * change coordinate, keep old one
         * @param x
         * @param y
         * @return 
         */
        public Tile move(int x, int y){
            return new Tile(new Pair(x,y),coord,symbol,tag);
        }
        
        public Tile addTag(String tag){
            return new Tile(getCoord(),
                            getLast(),
                            getSymbol(),
                            new ImmutableSet.Builder<String>()
                                            .add(tag)
                                            .addAll(this.tag)
                                            .build());
        }
        
        public Tile removeTag(String tag){
            return new Tile(getCoord(),
                            getLast(),
                            getSymbol(),
                            FluentIterable.from(this.tag)
                                          .filter(Predicates.not(Predicates.equalTo(tag)))
                                          .toSet());
        }
               
    }
    
    private Tile getTileBySymbol(String symbol){
        return FluentIterable.from(world).filter(Predicates.compose(Predicates.equalTo(symbol), onlySymbol)).first().or(TileDefault);
    }
    
    /* FUNCTOR */
    private static final Function<Tile,Collection<String>> onlyTag = new Function<Tile,Collection<String>>(){
        @Override
        public Collection<String> apply(Tile input) {
            return input.getTag();
        }
    };

    private static final Function<Tile,String> onlySymbol = new Function<Tile,String>(){
        @Override
        public String apply(Tile input) {
            return input.getSymbol();
        }
    };

    private static final Function<Tile, Pair> onlyCoord = new Function<Tile, Pair>() {
        @Override
        public Pair apply(Tile input) {
            return input.getCoord();
        }
    };
}