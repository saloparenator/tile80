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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Set;
import org.javatuples.Pair;

/**
 *
 * @author martin
 */
public abstract class Tile80 {
            
    public static Tile80 from(Pair<Integer, Integer> pos, String id, Iterable<Tag80> tagLst){
        return new byValue(pos, id, tagLst);
    }
    public static Tile80 from(int x, int y, String id, Iterable<Tag80> tagLst){
        return new byValue(new Pair(x,y), id, tagLst);
    }
    public static Tile80 fromWorld(String id, World80 world){
        return new lazy(world, id);
    }
    public static Tile80 newEmpty(String id){
        Set<Tag80> tags = ImmutableSet.of();
        return new byValue(new Pair(0,0),id,tags);
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
    public abstract Iterable<Tag80> getTags();
    
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
    public abstract Tile80 addTag(Tag80 tag);
    /**
     * mutator return a modified copy
     * @param tag the tag to remove
     * @return  new version of the object
     */
    public abstract Tile80 removeTag(Tag80 tag);
    
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
            for (Tag80 tag : t.getTags())
                if (!Iterables.contains(this.getTags(), tag))
                    return false;
            for(Tag80 tag:this.getTags())
                if (!Iterables.contains(t.getTags(), tag))
                    return false;
            return true;
        }
        return false;
    }
    
   public static Tile80 nothing = new Tile80(){
        @Override
        public Pair<Integer, Integer> getPos() {
            return new Pair(null,null);
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
        public Iterable<Tag80> getTags() {
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
        public Tile80 addTag(Tag80 tag) {
            return this;
        }

        @Override
        public Tile80 removeTag(Tag80 tag) {
            return this;
        }
        
    };
   
    private static class byValue extends Tile80{
        private final Pair<Integer,Integer> pos;
        private final String id;
        private final Iterable<Tag80> tagLst;

        public byValue(Pair<Integer, Integer> pos, String id, Iterable<Tag80> tagLst) {
            this.pos = pos;
            this.id = id;
            this.tagLst = tagLst;
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
        public Iterable<Tag80> getTags() {
            return tagLst;
        }

        @Override
        public Tile80 movePos(int x, int y) {
            return new byValue(new Pair(getX()+x,getY()+y),
                               getId(),
                               getTags());
        }

        @Override
        public Tile80 movePos(Pair<Integer,Integer> rpos) {
            return new byValue(new Pair(getX()+rpos.getValue0(),
                                        getY()+rpos.getValue1()),
                               getId(),
                               getTags());
        }

        @Override
        public Tile80 setPos(int x, int y) {
            return new byValue(new Pair(x,y),
                               getId(),
                               getTags());
        }

        @Override
        public Tile80 setPos(Pair<Integer,Integer> npos) {
            return new byValue(npos,
                               getId(),
                               getTags());
        }

        @Override
        public Tile80 addTag(Tag80 tag) {
            return new byValue(getPos(),
                               getId(),
                               Iterables.concat(ImmutableSet.of(tag),getTags()));    
        }

        @Override
        public Tile80 removeTag(Tag80 tag) {
            return new byValue(getPos(),
                               getId(),
                               FluentIterable.from(getTags())
                                             .filter(Predicates.not(Predicates.equalTo(tag))));    
        }
    };
    
    private static class lazy extends Tile80{
        private final World80 world;
        private final String id;
        private Pair<Integer,Integer> pos;
        private Iterable<Tag80> tags;

        public lazy(World80 world, String id) {
            this.world = world;
            this.id = id;
            pos=null;
            tags=null;
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
        public Iterable<Tag80> getTags() {
            if (tags==null)
                tags=world.getTagById(id);
            return tags;
        }

        @Override
        public Tile80 movePos(int x, int y) {
            return Tile80.from(new Pair(getX()+x,getY()+y), getId(), getTags());
        }

        @Override
        public Tile80 movePos(Pair<Integer, Integer> pos) {
            return Tile80.from(new Pair(getX()+pos.getValue0(),getY()+pos.getValue1()), getId(), getTags());
        }

        @Override
        public Tile80 setPos(int x, int y) {
            return Tile80.from(new Pair(x,y), getId(), getTags());
        }

        @Override
        public Tile80 setPos(Pair<Integer, Integer> pos) {
            return Tile80.from(pos, getId(), getTags());
        }

        @Override
        public Tile80 addTag(Tag80 tag) {
            return new byValue(getPos(),
                               getId(),
                               Iterables.concat(ImmutableSet.of(tag),getTags()));    
        }

        @Override
        public Tile80 removeTag(Tag80 tag) {
            return new byValue(getPos(),
                               getId(),
                               FluentIterable.from(getTags())
                                             .filter(Predicates.not(Predicates.equalTo(tag))));    
        }
    };
    
}
