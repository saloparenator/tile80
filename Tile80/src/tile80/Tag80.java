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

import org.javatuples.Tuple;

/**
 *
 * @author martin
 */
public abstract class Tag80 {
    public abstract String getName();
    public abstract String getDescription();
    public abstract Tile80 crunch(Tile80 self, World80 world);
    
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
        public Tile80 crunch(Tile80 self, World80 world) {
            throw new UnsupportedOperationException("nothing to support, its nothing");
        }
    };
}
