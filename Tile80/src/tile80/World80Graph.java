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
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Range;
import com.google.common.collect.Table;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import tool.Graph;
import tool.Link;

/**
 * Pair<1--1>Symbol<N--N>Tag
 *      
 * 
 * @author martin
 */
public class World80Graph implements World80{
    private final Graph<String,String> graph;
    private final Link<String,Pair<Integer,Integer>> coord;
    private final ImmutableMultimap<String,Behavior80> behaviorLst;
    private final ImmutableTable<String,String,String> keyspace;

    private World80Graph(Graph<String, String> graph, 
                         Link<String, Pair<Integer,Integer>> coord,
                         ImmutableMultimap<String,Behavior80> behaviorLst,
                         ImmutableTable<String,String,String> keyspace) {
        this.graph = graph;
        this.coord = coord;
        this.behaviorLst=behaviorLst;
        this.keyspace=keyspace;
    }
    
    public static World80Graph.Builder builder(){
        return new World80Graph.Builder();
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
    public Pair getDefaultPos() {
        return getDefaultTile().getPos();
    }
    
    @Override
    public Pair getPosById(String symbol) {
        Preconditions.checkNotNull(symbol);
        return Objects.firstNonNull(coord.getLeft(symbol),getDefaultPos());
    }

    private Function<String,Set<String>> getTagSymbolFunc(){
        return new Function<String, Set<String>>() {
            @Override
            public Set<String> apply(String input) {
                return graph.neighborRight(input);
            }
        };
    }

    @Override
    public Iterable<String> getTagById(String id) {
        Preconditions.checkNotNull(id);
        return graph.neighborLeft(id);
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
        return FluentIterable.from(coord.leftSet()).transform(toTile80);
    }

    @Override
    public Tile80 getTileByPos(Pair pos) {
        Preconditions.checkNotNull(pos);
        String id = coord.getRight(pos);
        if (id==null || "".equals(id))
            return Tile80.nothing;
        return Tile80.from(pos, id, getTagById(id), getBehaviorById(id), getKeySpaceById(id));
    }

    @Override
    public Tile80 getTileById(String id) {
        Preconditions.checkNotNull(id);
        if (coord.leftSet().contains(id))
            return Tile80.from(coord.getLeft(id),
                               id,
                               getTagById(id), 
                               getBehaviorById(id), 
                               getKeySpaceById(id));
        return Tile80.nothing;
    }

    @Override
    public Iterable<Tile80> getTileByTag(String tag) {
        return FluentIterable.from(graph.neighborRight(tag)).transform(toTile80);
    }

    @Override
    public Iterable<Tile80> getTileByRect(Pair<Integer, Integer> topLeft, Pair<Integer, Integer> bottomRight) {
        Preconditions.checkNotNull(topLeft,bottomRight);
        return FluentIterable.from(coord.rightSet())
                             .filter(Range.closed(topLeft,bottomRight))
                             .transform(Functions.forMap(coord.rightMap()))
                             .transform(toTile80);
    }

    @Override
    public World80 crunch(Set<String> event) {
        World80Graph.Builder nextFrame = World80Graph.builder();
        for (Tile80 tile : getTileLst()){
            for (Tile80 ntile : tile.crunch(this, event)){
                nextFrame.addTile(ntile);
            }
        }
        World80 w = nextFrame.build();
        return w;
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
        return behaviorLst.get(id);
    }

    @Override
    public Map<String, String> getKeySpaceById(String id) {
        return keyspace.row(id);
    }
    
    /**
     * 
     * @param <S>
     * @param <T> 
     */
    public static class Builder{
        private final Graph.Builder<String,String> graph;
        private final Link.Builder<String,Pair<Integer,Integer>> coord;
        private final ImmutableMultimap.Builder<String,Behavior80> behaviorLst;
        private final Table<String,String,String> keyspace;

        public Builder() {
            graph = Graph.builder();
            coord = Link.builder();
            behaviorLst = ImmutableMultimap.builder();
            keyspace = HashBasedTable.create();
        }
        
        public World80Graph build(){
            return new World80Graph(graph.build(), 
                                    coord.build(),
                                    behaviorLst.build(),
                                    ImmutableTable.copyOf(keyspace));
        }

        public Builder addTile(Tile80 tile){
            coord.link(tile.getId(), tile.getPos());
            for (String tag : tile.getTags())
                graph.link(tile.getId(), tag);
            for (Behavior80 behavior : tile.getBehavior())
                behaviorLst.put(tile.getId(), behavior);
            for (String key : tile.getKeyspace())
                keyspace.put(tile.getId(),key , tile.getFromKeyspace(key));
            return this;
        }
       

    }
 
}
