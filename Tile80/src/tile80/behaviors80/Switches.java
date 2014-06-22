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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Set;
import org.javatuples.Pair;
import tile80.tile80.Tile80;
import tile80.world80.World80;

/**
 *
 * @author martin
 */
public class Switches extends Behavior80{
        @Override
        public String getName() {
            return "switch";
        }

        @Override
        public String getDescription() {
            return "your can turn it on and off";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            Pair<Integer,Integer> topLeft = new Pair(self.getX()-1,self.getY()-1),
                                  bottomRight = new Pair(self.getX()+1,self.getY()+1);
            Tile80 swap;
            if (self.getTags().contains("on"))
                swap = self.addTag("off").removeTag("on").addKey("sprite", "powerlightOff");
            else
                swap = self.addTag("on").removeTag("off").addKey("sprite", "powerlightOn");
            for (Tile80 tile:world.getTileByRect(topLeft, bottomRight)){
                if (tile.getTags().contains("activate")){
                    return ImmutableSet.of(swap);
                }
            }
            return ImmutableSet.of(self);
        }
}
