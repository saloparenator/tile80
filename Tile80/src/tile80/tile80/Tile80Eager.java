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
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import tile80.behaviors80.Behavior80;

/**
 *
 * @author martin
 */
public class Tile80Eager extends Tile80{
        private final Pair<Integer,Integer> pos;
        private final String id;
        private final Set<String> tagLst;
        private final Iterable<Behavior80> behaviorLst;
        private final Map<String,String> keyspace;

        public Tile80Eager(Pair<Integer, Integer> pos, 
                       String id, 
                       Set<String> tagLst,
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
        public Set<String> getTags() {
            return tagLst;
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
        public Map<String,String> getKeyspace() {
            return keyspace;
        }
 
}
