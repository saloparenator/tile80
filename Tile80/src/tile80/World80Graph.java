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
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Range;
import java.util.Collection;
import java.util.Set;
import org.javatuples.Pair;
import tool.Graph;

/**
 *
 * @author martin
 */
public class World80Graph implements World80{
    private final Graph<String,Tag80> graph;
    private final BiMap<String,Pair> coord;

    public World80Graph() {
        graph = new Graph();
        coord = HashBiMap.create();

    }

    @Override
    public Pair getCoordBySymbol(String symbol) {
        Preconditions.checkNotNull(symbol);
        return Objects.firstNonNull(coord.get(symbol),new Pair(0,0));
    }

    @Override
    public Iterable<Pair> getCoordLst() {
        return coord.values();
    }

    @Override
    public String getSymbolByCoord(Pair pos) {
        Preconditions.checkNotNull(pos);
        return Objects.firstNonNull(coord.inverse().get(pos),getSymbolDefault());
    }

    @Override
    public Iterable<String> getSymbolByRect(Pair topLeft, Pair bottomRight) {
        Preconditions.checkNotNull(topLeft,bottomRight);
        return FluentIterable.from(coord.values())
                             .filter(Range.closed(topLeft,bottomRight))
                             .transform(Functions.forMap(coord.inverse()));
    }

    @Override
    public Iterable<String> getSymbolLst() {
        return graph.leftSet();
    }

    private Function<Tag80,Set<String>> getTagSymbolFunc(){
        return new Function<Tag80, Set<String>>() {
            @Override
            public Set<String> apply(Tag80 input) {
                return graph.neighborRight(input);
            }
        };
    }

    @Override
    public Iterable<String> getSymbolLstByTag(Tag80 tag) {
        Preconditions.checkNotNull(tag);
        return graph.neighborRight(tag);
    }

    @Override
    public Iterable getSymbolNeighbor(String symbol) {
        Preconditions.checkNotNull(symbol);
        Pair<Integer,Integer> center = getCoordBySymbol(symbol),
                              topLeft = new Pair(center.getValue0()-1,center.getValue1()-1),
                              bottomRight = new Pair(center.getValue0()+1,center.getValue1()+1);
        Predicate inRect = Range.closed(topLeft,bottomRight);
        return FluentIterable.from(coord.keySet())
                             .filter(inRect)
                             .transform(Functions.forMap(coord));
    }

    @Override
    public Iterable<Tag80> getTagBySymbol(String symbol) {
        Preconditions.checkNotNull(symbol);
        return graph.neighborLeft(symbol);
    }

    @Override
    public Iterable<Tag80> getTagLst() {
        return graph.rightSet();
    }

    public World80 moveSymbol(String symbol, int x, int y) {
        Preconditions.checkNotNull(symbol);
        coord.forcePut(symbol, new Pair(x,y));
        return this;
    }

    public World80 addTag(String symbol, Tag80 tag) {
        Preconditions.checkNotNull(symbol,tag);
        graph.link(symbol, tag);
        return this;
    }

    public World80 removeTag(String symbol, Tag80 tag) {
        Preconditions.checkNotNull(symbol,tag);
        graph.unlink(symbol, tag);
        return this;
    }

    public World80 addSymbol(String symbol, int x, int y) {
        Preconditions.checkNotNull(symbol);
        coord.forcePut(symbol, new Pair(x,y));
        return this;
    }

    public World80 removeSymbol(String symbol) {
        Preconditions.checkNotNull(symbol);
        coord.remove(symbol);
        graph.removeLeft(symbol);
        return this;
    }

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
        return Tile80.nothing;
    }

    private final Function<String,Tile80> toTile80 = new Function<String, Tile80>() {
        @Override
        public Tile80 apply(String input) {
            return Tile80.from(coord.get(input), input, graph.neighborLeft(input));
        }
    };
    
    @Override
    public Iterable<Tile80> getTileLst() {
        return FluentIterable.from(getSymbolLst()).transform(toTile80);
    }

    @Override
    public Tile80 getTileByCoord(Pair pos) {
        String symbol = getSymbolByCoord(pos);
        return Tile80.from(pos, symbol, getTagBySymbol(symbol));
    }

    @Override
    public Tile80 getTileBySymbol(String Symbol) {
        return Tile80.from(getCoordBySymbol(Symbol), Symbol, getTagBySymbol(Symbol));
    }

    @Override
    public Iterable<Tile80> getTileByTag(Tag80 tag) {
        Iterable<String> symbolLst = getSymbolLstByTag(tag);
        return FluentIterable.from(getSymbolLst()).transform(toTile80);
    }
    
}
