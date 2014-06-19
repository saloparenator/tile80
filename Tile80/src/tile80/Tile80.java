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

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;

/**
 *
 * @author martin
 */
public abstract class Tile80 {
            
    public static Tile80 from(Pair<Integer, Integer> pos, 
                              String id, 
                              Iterable<String> tagLst,
                              Iterable<Behavior80> behaviorLst,
                              Map<String,String> keyspace){
        return new byValue(pos, id, tagLst,behaviorLst,keyspace);
    }
    public static Tile80 from(int x, int y, 
                              String id, 
                              Iterable<String> tagLst,
                              Iterable<Behavior80> behaviorLst,
                              Map<String,String> keyspace){
        return new byValue(new Pair(x,y), id, tagLst,behaviorLst,keyspace);
    }
    public static Tile80 fromWorld(String id, World80 world){
        return new lazy(world, id);
    }
    public static Tile80 newEmpty(String id){
        Set lst = ImmutableSet.of();
        Map map = ImmutableMap.of();
        return new byValue(new Pair(0,0),id,lst,lst,map);
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
    public abstract Iterable<String> getTags();
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
    public abstract Iterable<String> getKeyspace();
    /**
     * mutator return a modified copy
     * @param x relative x movement
     * @param y relative y movement
     * @return  new version of the object
     */
    public abstract Tile80 movePos(int x, int y);
    /**
     * mutator return a modified copy
     * @param pos relative position Pair movement
     * @return  new version of the object
     */
    public abstract Tile80 movePos(Pair<Integer,Integer> pos);
    /**
     * mutator return a modified copy
     * @param x new x position
     * @param y new y position
     * @return  new version of the object
     */
    public abstract Tile80 setPos(int x, int y);
    /**
     * mutator return a modified copy
     * @param pos new position pair
     * @return  new version of the object
     */
    public abstract Tile80 setPos(Pair<Integer,Integer> pos);
    
    /**
     * mutator return a modified copy
     * @param tag the tag to add to the collection
     * @return  new version of the object
     */
    public abstract Tile80 addTag(String tag);
    /**
     * mutator return a modified copy
     * @param tag the tag to remove
     * @return  new version of the object
     */
    public abstract Tile80 removeTag(String tag);
    
    public abstract Tile80 addBehavior(Behavior80 behavior);
    public abstract Tile80 removeBehavior(Behavior80 behavior);
    public abstract Tile80 addKey(String key, String value);
    public abstract Tile80 removeKey(String key);
    
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
        public Iterable<String> getTags() {
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
        public Iterable<String> getKeyspace() {
            return ImmutableSet.of();
        }
       
    };
   
    private static class byValue extends Tile80{
        private final Pair<Integer,Integer> pos;
        private final String id;
        private final Iterable<String> tagLst;
        private final Iterable<Behavior80> behaviorLst;
        private final Map<String,String> keyspace;

        public byValue(Pair<Integer, Integer> pos, 
                       String id, 
                       Iterable<String> tagLst,
                       Iterable<Behavior80> behaviorLst,
                       Map<String,String> keyspace) {
            this.pos = pos;
            this.id = id;
            this.tagLst = tagLst;
            this.behaviorLst=behaviorLst;
            this.keyspace=keyspace;
        }
        
        public Map<String,String> getMap(){
            return keyspace;
        }
        
        @Override
        public Pair<Integer, Integer> getPos() {
            return pos;
        }

        @Override
        public int getX() {
            return pos.getValue0();
        }

        @Override
        public int getY() {
            return pos.getValue1();
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public Iterable<String> getTags() {
            return tagLst;
        }

        @Override
        public Tile80 movePos(int x, int y) {
            return new byValue(new Pair(getX()+x,getY()+y),
                               getId(),
                               getTags(),
                               getBehavior(),
                               keyspace);
        }

        @Override
        public Tile80 movePos(Pair<Integer,Integer> rpos) {
            return new byValue(new Pair(getX()+rpos.getValue0(),
                                        getY()+rpos.getValue1()),
                               getId(),
                               getTags(),
                               getBehavior(),
                               keyspace);
        }

        @Override
        public Tile80 setPos(int x, int y) {
            return new byValue(new Pair(x,y),
                               getId(),
                               getTags(),
                               getBehavior(),
                               keyspace);
        }

        @Override
        public Tile80 setPos(Pair<Integer,Integer> npos) {
            return new byValue(npos,
                               getId(),
                               getTags(),
                               getBehavior(),
                               keyspace);
        }

        @Override
        public Tile80 addTag(String tag) {
            return new byValue(getPos(),
                               getId(),
                               Iterables.concat(ImmutableSet.of(tag),getTags()),
                               getBehavior(),
                               keyspace);    
        }

        @Override
        public Tile80 removeTag(String tag) {
            return new byValue(getPos(),
                               getId(),
                               FluentIterable.from(getTags())
                                             .filter(Predicates.not(Predicates.equalTo(tag))),
                               getBehavior(),
                               keyspace);    
        }

        @Override
        public Iterable<Behavior80> getBehavior() {
            return behaviorLst;
        }

        @Override
        public String getFromKeyspace(String key) {
            return keyspace.get(key);
        }

        @Override
        public Tile80 addBehavior(Behavior80 behavior) {
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               Iterables.concat(ImmutableSet.of(behavior),behaviorLst),
                               getMap());
        }

        @Override
        public Tile80 removeBehavior(Behavior80 behavior) {
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               FluentIterable.from(behaviorLst).filter(Predicates.not(Predicates.equalTo(behavior))),
                               getMap());
        }

        @Override
        public Tile80 addKey(String key, String value) {
            Map map = new HashMap();
            map.putAll(getMap());
            map.put(key, value);
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               getBehavior(),
                               ImmutableMap.copyOf(map));
        }

        @Override
        public Tile80 removeKey(String key) {
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               getBehavior(),
                               Maps.filterKeys(getMap(), Predicates.equalTo(key)));
        }

        @Override
        public Iterable<String> getKeyspace() {
            return keyspace.keySet();
        }

    };
    
    private static class lazy extends Tile80{
        private final World80 world;
        private final String id;
        private Pair<Integer,Integer> pos;
        private Iterable<String> tags;
        private Iterable<Behavior80> behaviorLst;
        private Map<String,String> keyspace;

        public lazy(World80 world, 
                    String id) {
            this.world = world;
            this.id = id;
            pos=null;
            tags=null;
            behaviorLst=null;
            keyspace=null;
        }
        
        private Map<String,String> getMap(){
            if (keyspace==null)
                keyspace=world.getKeySpaceById(id);
            return keyspace;
        }

        @Override
        public Pair<Integer, Integer> getPos() {
            if (pos==null)
                pos=world.getPosById(id);
            return pos;
        }

        @Override
        public int getX() {
            if (pos==null)
                pos=world.getPosById(id);
            return pos.getValue0();
        }

        @Override
        public int getY() {
            if (pos==null)
                pos=world.getPosById(id);   
            return pos.getValue1();
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public Iterable<String> getTags() {
            if (tags==null)
                tags=world.getTagById(id);
            return tags;
        }

        @Override
        public Tile80 movePos(int x, int y) {
            return Tile80.from(new Pair(getX()+x,getY()+y), 
                               getId(), 
                               getTags(),
                               getBehavior(),
                               getMap());
        }

        @Override
        public Tile80 movePos(Pair<Integer, Integer> pos) {
            return Tile80.from(new Pair(getX()+pos.getValue0(),getY()+pos.getValue1()), 
                               getId(), 
                               getTags(),
                               getBehavior(),
                               getMap());
        }

        @Override
        public Tile80 setPos(int x, int y) {
            return Tile80.from(new Pair(x,y), 
                               getId(), 
                               getTags(),
                               getBehavior(),
                               getMap());
        }

        @Override
        public Tile80 setPos(Pair<Integer, Integer> pos) {
            return Tile80.from(pos, 
                               getId(), 
                               getTags(),
                               getBehavior(),
                               getMap());
        }

        @Override
        public Tile80 addTag(String tag) {
            return new byValue(getPos(),
                               getId(),
                               Iterables.concat(ImmutableSet.of(tag),getTags()),
                               getBehavior(),
                               getMap());    
        }

        @Override
        public Tile80 removeTag(String tag) {
            return new byValue(getPos(),
                               getId(),
                               FluentIterable.from(getTags())
                                             .filter(Predicates.not(Predicates.equalTo(tag))),
                               getBehavior(),
                               getMap());    
        }

        @Override
        public Iterable<Behavior80> getBehavior() {
            if (behaviorLst==null)
                behaviorLst=world.getBehaviorById(id);
            return behaviorLst;
        }

        @Override
        public String getFromKeyspace(String key) {
            if(keyspace==null)
                keyspace=world.getKeySpaceById(id);
            return keyspace.get(key);
        }

        @Override
        public Tile80 addBehavior(Behavior80 behavior) {
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               Iterables.concat(ImmutableSet.of(behavior),behaviorLst),
                               getMap());
        }

        @Override
        public Tile80 removeBehavior(Behavior80 behavior) {
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               FluentIterable.from(behaviorLst).filter(Predicates.not(Predicates.equalTo(behavior))),
                               getMap());
        }

        @Override
        public Tile80 addKey(String key, String value) {
            Map map = new HashMap();
            map.putAll(getMap());
            map.put(key, value);
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               getBehavior(),
                               ImmutableMap.copyOf(map));
        }

        @Override
        public Tile80 removeKey(String key) {
            return new byValue(getPos(),
                               getId(),
                               getTags(),
                               getBehavior(),
                               Maps.filterKeys(getMap(), Predicates.equalTo(key)));
        }

        @Override
        public Iterable<String> getKeyspace() {
            if(keyspace==null)
                keyspace=world.getKeySpaceById(id);
            return keyspace.keySet();
        }

    };
    
}
