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

import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import tile80.behaviors80.Behavior80;
import tile80.world80.World80;

/**
 *
 * @author martin
 */
public class Tile80Lazy extends Tile80{
        private final World80 world;
        private final String id;
        private Pair<Integer,Integer> pos;
        private Set<String> tags;
        private Iterable<Behavior80> behaviorLst;
        private Map<String,String> keyspace;

        public Tile80Lazy(World80 world, 
                    String id) {
            this.world = world;
            this.id = id;
            pos=null;
            tags=null;
            behaviorLst=null;
            keyspace=null;
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
        public Set<String> getTags() {
            if (tags==null)
                tags=world.getTagById(id);
            return tags;
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
        public Map<String,String> getKeyspace() {
            if(keyspace==null)
                keyspace=world.getKeySpaceById(id);
            return keyspace;
        }
}
