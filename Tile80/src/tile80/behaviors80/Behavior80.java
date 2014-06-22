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

package tile80.behaviors80;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import tile80.tile80.Tile80;
import tile80.world80.World80;

/**
 *
 * @author martin
 */
public abstract class Behavior80 {
    private static Map suite = ImmutableMap.builder()
                                          .put("activator", new Activator())
                                          .put("beam", new Beam())
                                          .put("beamEmitter",new BeamEmitter())
                                          .put("mover",new Mover())
                                          .put("switch",new Switches())
                                          .put("gof", new Gof())
                                          .build();
    public static Behavior80 byName(String name){
        if (suite.containsKey(name))
            return (Behavior80) suite.get(name);
        return nothing;
    }
    
    public static Behavior80 makeTag(String name, String description){
        return new Behavior(name,description);
    }
    /**
     * return name
     * @return 
     */
    public abstract String getName();
    /**
     * return description
     * @return 
     */
    public abstract String getDescription();
    /**
     * modify the tile in a frame
     * @param self
     * @param world
     * @param event
     * @return 
     */
    public abstract Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event);
    
    @Override
    public String toString(){
        return getName();
    }
    @Override
    public int hashCode(){
        return getName().hashCode();
    }
    @Override
    public boolean equals(Object o){
        return o==this;
    }
    
    private static final class Behavior extends Behavior80{
        private final String name;
        private final String description;

        private Behavior(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            return ImmutableSet.of(self);
        }
        
    }
    
    public static final Behavior80 nothing = new Behavior80(){
        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getDescription() {
            return "its nothing not even null";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world,Set<String> event) {
            return ImmutableSet.of(self);
        }
    };
}
