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
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Range;
import java.util.Collection;
import java.util.Set;
import org.javatuples.Pair;
import tool.ImmutableGraph;

/**
 * Pair<1--1>Symbol<N--N>Tag
 *      
 * 
 * @author martin
 */
public class World80Graph implements World80{
    private final ImmutableGraph<String,Tag80> graph;
    private final ImmutableBiMap<String,Pair<Integer,Integer>> coord;

    private World80Graph(ImmutableGraph<String, Tag80> graph, 
                            ImmutableBiMap<String, Pair<Integer,Integer>> coord) {
        this.graph = graph;
        this.coord = coord;
    }
    
    public static World80Graph.Builder builder(){
        return new World80Graph.Builder();
    }
    
    @Override
    public String getDefaultId() {
        return "";
    }

    @Override
    public Tag80 getDefaultTags() {
        return Tag80.nothing;
    }

    @Override
    public Pair getDefaultPos() {
        return new Pair(null,null);
    }
    
    @Override
    public Pair getPosById(String symbol) {
        Preconditions.checkNotNull(symbol);
        return Objects.firstNonNull(coord.get(symbol),getDefaultPos());
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
    public Iterable<Tag80> getTagById(String symbol) {
        Preconditions.checkNotNull(symbol);
        return graph.neighborLeft(symbol);
    }

    @Override
    public Tile80 getDefaultTile() {
        return Tile80.nothing;
    }

    private final Function<String,Tile80> toTile80 = new Function<String, Tile80>() {
        @Override
        public Tile80 apply(String input) {
            return Tile80.fromWorld(input, World80Graph.this);
        }
    };
    
    @Override
    public Iterable<Tile80> getTileLst() {
        return FluentIterable.from(coord.keySet()).transform(toTile80);
    }

    @Override
    public Tile80 getTileByPos(Pair pos) {
        String id = coord.inverse().get(pos);
        return Tile80.from(pos, id, getTagById(id));
    }

    @Override
    public Tile80 getTileById(String symbol) {
        return Tile80.from(coord.get(this), symbol, getTagById(symbol));
    }

    @Override
    public Iterable<Tile80> getTileByTag(Tag80 tag) {
        return FluentIterable.from(graph.neighborRight(tag)).transform(toTile80);
    }

    @Override
    public Iterable<Tile80> getTileByRect(Pair<Integer, Integer> topLeft, Pair<Integer, Integer> bottomRight) {
        Preconditions.checkNotNull(topLeft,bottomRight);
        return FluentIterable.from(coord.values())
                             .filter(Range.closed(topLeft,bottomRight))
                             .transform(Functions.forMap(coord.inverse()))
                             .transform(toTile80);
    }
    
    /**
     * 
     * @param <S>
     * @param <T> 
     */
    public static class Builder{
        private final ImmutableGraph.Builder<String,Tag80> graph;
        private final ImmutableBiMap.Builder<String,Pair<Integer,Integer>> coord;

        public Builder() {
            graph = ImmutableGraph.builder();
            coord = ImmutableBiMap.builder();
        }
        
        public World80Graph build(){
            return new World80Graph(graph.build(), coord.build());
        }

        public Builder addTile(Tile80 tile){
            coord.put(tile.getId(), tile.getPos());
            for (Tag80 tag : tile.getTags())
                graph.link(tile.getId(), tag);
            return this;
        }
        
        public Builder addTag(String symbol, Tag80 tag) {
            graph.link(symbol, tag);
            return this;
        }

        public Builder addSymbol(String symbol, int x, int y) {
            coord.put(symbol, new Pair(x,y));
            return this;
        }
    }
 
}
