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

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import tile80.behaviors80.Behavior80;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import tile80.tile80.Tile80;
import static tile80.world80.World80Graph.builder;

/**
 *
 * @author martin
 */
public abstract class World80 {
    /**
     * null replacement
     * @return Pair(0,0)
     */
    public Pair getDefaultPos(){
        return new Pair(0,0);
    }
    /**
     * null replacement
     * @return ""
     */
    public String getDefaultId(){
        return "";
    }
    /**
     * null replacement
     * @return Behavior80.nothing
     */
    public Behavior80 getDefaultBehavior(){
        return Behavior80.nothing;
    }
    /**
     * 
     * @return 
     */
    public String getDefaultTag(){
        return "";
    }
    /**
     * 
     * @return 
     */
    public Map<String,String> getDefaultKeySpace(){
        return ImmutableMap.of();
    }
    /**
     * tile that act if there is nothing
     * @return Tile80.nothing
     */
    public Tile80 getDefaultTile(){
        return Tile80.nothing;
    }
    
    /**
     * all tile in world
     * @return 
     */
    public abstract Iterable<Tile80> getTileLst();
    /**
     * find tile at giventh position
     * @param pos
     * @return 
     */
    public abstract Tile80 getTileByPos(Pair pos);
    /**
     * find tile by id
     * @param Symbol
     * @return 
     */
    public abstract Tile80 getTileById(String id);
    /**
     * find all tile with giventh tag
     * @param tag
     * @return 
     */
    public abstract Iterable<Tile80> getTileByTag(String tag);
    /**
     * find all tile within rect
     * @param topLeft
     * @param bottomRight
     * @return 
     */
    public abstract Iterable<Tile80> getTileByRect(Pair<Integer,Integer> topLeft,
                                   Pair<Integer,Integer> bottomRight);
    
    /**
     * return position of giventh id
     * @param id
     * @return 
     */
    public abstract Pair<Integer,Integer> getPosById(String id);
    /**
     * return all tag linked to giventh id
     * @param id
     * @return 
     */
    public abstract Set<String> getTagById(String id);
    /**
     * all behavior associated to the id
     * @param id
     * @return 
     */
    public abstract Iterable<Behavior80> getBehaviorById(String id);
    /**
     * the keyspace associated to the id
     * @param id
     * @return 
     */
    public abstract Map<String,String> getKeySpaceById(String id);
    
    /**
     * apply crunch on every tile with all related tag
     * @param event
     * @return 
     */
    public abstract World80 crunch(Set<String> event);
    protected World80 crunchWith(Builder builder, Set<String> event) {
        for (Tile80 tile : getTileLst()){
            for (Tile80 ntile : tile.crunch(this, event)){
                if (!ntile.isNothing())
                    builder.addTile(ntile);
            }
        }
        World80 w = builder.build();
        return w;
    }
    
    public static World80 load(Builder b, String json){
       
        Map<String,Object> data = new Gson().fromJson(json,Map.class);
        for (Map<String,Object> tile : (Collection<Map>)data.get("tiles")){
            Set<Behavior80> behaviors = new HashSet<>();
            for (String bs : (Collection<String>)tile.get("behaviors")){
                behaviors.add((Behavior80) Behavior80.byName(bs));
            }
            b.addTile(Tile80.newEmpty((String) tile.get("id"))
                            .setPos(((Double)tile.get("x")).intValue(), ((Double)tile.get("y")).intValue())
                            .addAllTag((Collection)tile.get("tags"))
                            .addAllBehavior(behaviors)
                            .addAllKey((Map)tile.get("keyspace")));
        }
        
        return b.build();
    }
    
    public static String store(World80 world){
        Map wmap = new HashMap<>();
        List ltile = new ArrayList<>();
        for (Tile80 tile : world.getTileLst()){
            Set<String> behaviors = new HashSet<>();
            for (Behavior80 bs : tile.getBehavior())
                    behaviors.add(bs.getName());
            Map <String,Object> tmap = new HashMap<>();
            tmap.put("id", tile.getId());
            tmap.put("x", tile.getX());
            tmap.put("y", tile.getY());
            tmap.put("tags", tile.getTags());
            tmap.put("keyspace", tile.getKeyspace());
            tmap.put("behaviors",behaviors);
            ltile.add(tmap);
        }
        wmap.put("type", "world");
        wmap.put("tiles", ltile);
        return new Gson().toJson(wmap, Map.class);
    }
    
    public static interface Builder {

        Builder addTile(Tile80 tile);

        World80 build();

    }
}
