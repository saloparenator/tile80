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

package tile80.tile80;

import com.google.common.base.Preconditions;
import tile80.world80.World80;
import tile80.behaviors80.Behavior80;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import org.javatuples.Pair;
import static sun.net.www.http.HttpClient.New;

/**
 *
 * @author martin
 */
public abstract class Tile80 {
    private static final Logger LOG = Logger.getLogger(Tile80.class.getName());
            
    public static Tile80 from(Pair<Integer, Integer> pos, 
                              String id, 
                              Set<String> tagLst,
                              Iterable<Behavior80> behaviorLst,
                              Map<String,String> keyspace){
        return new Tile80Eager(pos, id, tagLst,behaviorLst,keyspace);
    }
    public static Tile80 from(int x, int y, 
                              String id, 
                              Set<String> tagLst,
                              Iterable<Behavior80> behaviorLst,
                              Map<String,String> keyspace){
        return new Tile80Eager(new Pair(x,y), id, tagLst,behaviorLst,keyspace);
    }
    public static Tile80 fromWorld(String id, World80 world){
        return new Tile80Lazy(world, id);
    }
    public static Tile80 newEmpty(String id){
        Set lst = ImmutableSet.of();
        Map map = ImmutableMap.of();
        return new Tile80Eager(new Pair(0,0),id,lst,lst,map);
    }
    /**
     * getter
     * return the position Pair
     * @return pair
     */
    public abstract Pair<Integer,Integer> getPos();
    /**
     * getter
     * return the x pos
     * @return x
     */
    public abstract int getX();
    /**
     * getter
     * return the y pos
     * @return y
     */
    public abstract int getY();
    /**
     * getter
     * return the unique id
     * @return id
     */
    public abstract String getId();
    /**
     * getter
     * return a collection of tag associated to the tile
     * @return iterable<tag>
     */
    public abstract Set<String> getTags();
    /**
     * 
     * @return 
     */
    public abstract Iterable<Behavior80> getBehavior();
    /**
     * 
     * @return 
     */
    public abstract String getFromKeyspace(String key);
    public abstract Map<String,String> getKeyspace();
    /**
     * mutator return a modified copy
     * @param x relative x movement
     * @param y relative y movement
     * @return  new version of the object
     */
    public Tile80 movePos(int x, int y) {
        return new Tile80Eager(new Pair(getX()+x,getY()+y),
                           getId(),
                           getTags(),
                           getBehavior(),
                           getKeyspace());
    }
    /**
     * mutator return a modified copy
     * @param pos relative position Pair movement
     * @return  new version of the object
     */
    public Tile80 movePos(Pair<Integer,Integer> rpos) {
        return new Tile80Eager(new Pair(getX()+rpos.getValue0(),
                                    getY()+rpos.getValue1()),
                           getId(),
                           getTags(),
                           getBehavior(),
                           getKeyspace());
    }
        
    /**
     * mutator return a modified copy
     * @param x new x position
     * @param y new y position
     * @return  new version of the object
     */
    public Tile80 setPos(int x, int y) {
        return new Tile80Eager(new Pair(x,y),
                           getId(),
                           getTags(),
                           getBehavior(),
                           getKeyspace());
    }
        
     /**
     * mutator return a modified copy
     * @param pos new position pair
     * @return  new version of the object
     */
    public Tile80 setPos(Pair<Integer,Integer> npos) {
        return new Tile80Eager(npos,
                           getId(),
                           getTags(),
                           getBehavior(),
                           getKeyspace());
    }    
    /**
     * mutator return a modified copy
     * @param tag the tag to add to the collection
     * @return  new version of the object
     */
    public Tile80 addTag(String tag) {
        Set<String> set = new HashSet<>();
        set.add(tag);
        set.addAll(getTags());
        return new Tile80Eager(getPos(),
                           getId(),
                           ImmutableSet.copyOf(set),
                           getBehavior(),
                           getKeyspace());    
    }
        
    public Tile80 addAllTag(Iterable<String> tags){
        Tile80 t = this;
        for (String tag : tags)
            t = t.addTag(tag);
        return t;
    }
    /**
     * mutator return a modified copy
     * @param tag the tag to remove
     * @return  new version of the object
     */
    public Tile80 removeTag(String tag) {
        return new Tile80Eager(getPos(),
                           getId(),
                           FluentIterable.from(getTags())
                                         .filter(Predicates.not(Predicates.equalTo(tag)))
                                         .toSet(),
                           getBehavior(),
                           getKeyspace());    
    } 
        
    public Tile80 addBehavior(Behavior80 behavior) {
        Preconditions.checkNotNull(behavior);
        return new Tile80Eager(getPos(),
                           getId(),
                           getTags(),
                           Iterables.concat(ImmutableSet.of(behavior),getBehavior()),
                           getKeyspace());
    }
        
    public Tile80 addAllBehavior(Iterable<Behavior80> behaviors){
        Tile80 t = this;
        for (Behavior80 b : behaviors)
            t = t.addBehavior(b);
        return t;
    }
    
    public Tile80 removeBehavior(Behavior80 behavior) {
        return new Tile80Eager(getPos(),
                           getId(),
                           getTags(),
                           FluentIterable.from(getBehavior()).filter(Predicates.not(Predicates.equalTo(behavior))),
                           getKeyspace());
    }
    
    public Tile80 addKey(String key, String value) {
        Map map = new HashMap();
        map.putAll(getKeyspace());
        map.put(key, value);
        return new Tile80Eager(getPos(),
                           getId(),
                           getTags(),
                           getBehavior(),
                           ImmutableMap.copyOf(map));
    }
    
    public Tile80 addAllKey(Map<String,String> map){
        Tile80 t = this;
        for (Entry<String,String> entry : map.entrySet())
            t = t.addKey(entry.getKey(), entry.getValue());
        return t;
    }
    
    public Tile80 removeKey(String key) {
        return new Tile80Eager(getPos(),
                           getId(),
                           getTags(),
                           getBehavior(),
                           Maps.filterKeys(getKeyspace(), Predicates.equalTo(key)));
    }
    
    public boolean isNothing(){
        return false;
    }
    /**
     * Self crunch with all tag it contains
     * @param world
     * @param event
     * @return 
     */
    public Iterable<Tile80> crunch(World80 world, Set<String> event) {
        ImmutableSet.Builder<Tile80> ret = ImmutableSet.builder();
        Tile80 newTile = this;
        for (Behavior80 tag : getBehavior()){
            Iterable<Tile80> tileLst = tag.crunch(newTile, world, event);
            newTile = findSelf(tileLst);
            for (Tile80 tile : FluentIterable.from(tileLst).filter(Predicates.not(Predicates.equalTo(newTile))))
                ret.add(tile);
            if (newTile.equals(nothing))
                break;
        }
        return ret.add(newTile).build();
    }    
    /**
     * 
     * @param tileLst
     * @return 
     */
    protected Tile80 findSelf(Iterable<Tile80> tileLst){
        for (Tile80 tile : tileLst)
            if (tile.getId().equals(getId()))
                return tile;
        return nothing;
    }
    
    @Override
    public int hashCode(){
        int h = 13 * getPos().hashCode();
        h += h*17 + 13*getId().hashCode();
        h += h*17 + 13*getTags().hashCode();
        return h;
    }
    
    public boolean equals(Object o){
        if (o==null || !(o instanceof Tile80))
            return false;
        if (o==this)
            return true;
        Tile80 t = (Tile80)o;
        if (t.getId().equals(this.getId()) &&
            t.getPos().equals(this.getPos())){
            for (String tag : t.getTags())
                if (!Iterables.contains(this.getTags(), tag))
                    return false;
            for(String tag:this.getTags())
                if (!Iterables.contains(t.getTags(), tag))
                    return false;
            return true;
        }
        return false;
    }
    
   public static Tile80 nothing = new Tile80(){
        @Override
        public Pair<Integer, Integer> getPos() {
            return new Pair(0,0);
        }

        @Override
        public boolean isNothing(){
            return true;
        }
        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }

        @Override
        public String getId() {
            return "";
        }

        @Override
        public Set<String> getTags() {
            return ImmutableSet.of();
        }

        @Override
        public Tile80 movePos(int x, int y) {
            return this;
        }

        @Override
        public Tile80 movePos(Pair<Integer,Integer> pos) {
            return this;
        }

        @Override
        public Tile80 setPos(int x, int y) {
            return this;
        }

        @Override
        public Tile80 setPos(Pair<Integer,Integer> pos) {
            return this;
        }

        @Override
        public Tile80 addTag(String tag) {
            return this;
        }

        @Override
        public Tile80 removeTag(String tag) {
            return this;
        }

        @Override
        public Iterable<Tile80> crunch(World80 world, Set<String> event) {
            return ImmutableSet.of();
        }

        @Override
        public Iterable<Behavior80> getBehavior() {
            return ImmutableSet.of();
        }

        @Override
        public String getFromKeyspace(String key) {
            return "";
        }

        @Override
        public Tile80 addBehavior(Behavior80 behavior) {
            return this;
        }

        @Override
        public Tile80 removeBehavior(Behavior80 behavior) {
            return this;
        }

        @Override
        public Tile80 addKey(String key, String value) {
            return this;
        }

        @Override
        public Tile80 removeKey(String key) {
            return this;
        }

        @Override
        public Map<String,String> getKeyspace() {
            return ImmutableMap.of();
        }
       
    };
    
}
