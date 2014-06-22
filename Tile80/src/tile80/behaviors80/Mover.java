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
import java.util.Set;
import org.javatuples.Pair;
import tile80.tile80.Tile80;
import tile80.world80.World80;

/**
 *
 * @author martin
 */
public class Mover extends Behavior80{
        @Override
        public String getName() {
            return "mover";
        }

        @Override
        public String getDescription() {
            return "the player move with the keyboard";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world,Set<String> event) {
            Tile80 crunching = self;
            for (String e : event){
                if ("up".equals(e) && 
                    world.getTileByPos(new Pair(self.getX(),self.getY()-1)).isNothing())
                    crunching = crunching.movePos(0, -1);
                else if ("down".equals(e) && 
                    world.getTileByPos(new Pair(self.getX(),self.getY()+1)).isNothing())
                    crunching = crunching.movePos(0, 1);
                else if ("left".equals(e) && 
                    world.getTileByPos(new Pair(self.getX()-1,self.getY())).isNothing())
                    crunching = crunching.movePos(-1, 0);
                else if ("right".equals(e) && 
                    world.getTileByPos(new Pair(self.getX()+1,self.getY())).isNothing())
                    crunching = crunching.movePos(1, 0);
            }
            return ImmutableSet.of(crunching);
        }     
}
