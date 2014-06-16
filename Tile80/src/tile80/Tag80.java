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

import com.google.common.collect.ImmutableSet;
import java.util.Set;

/**
 *
 * @author martin
 */
public abstract class Tag80 {
    public static Tag80 makeTag(String name, String description){
        return new Tag(name,description);
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
    
    private static final class Tag extends Tag80{
        private final String name;
        private final String description;

        private Tag(String name, String description) {
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
    
    public static final Tag80 nothing = new Tag80(){
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
