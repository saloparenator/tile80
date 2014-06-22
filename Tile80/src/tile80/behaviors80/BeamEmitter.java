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
import tile80.tile80.Tile80;
import tile80.world80.World80;

/**
 *
 * @author martin
 */
public class BeamEmitter extends Behavior80{
        @Override
        public String getName() {
            return "beamEmitter";
        }

        @Override
        public String getDescription() {
            return "it emmit a beam and listen to a switch @switch";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            Tile80 sw = world.getTileById(self.getFromKeyspace("switch"));
            if (sw.getTags().contains("on")){
                return ImmutableSet.of(self.addTag("emit"),
                                       Tile80.newEmpty("beam")
                                             .setPos(self.getX()+Integer.parseInt(self.getFromKeyspace("rx")),
                                                     self.getY()+Integer.parseInt(self.getFromKeyspace("ry")))
                                             .addKey("sprite", "beamVertical")
                                             .addBehavior(new Beam()));
            }
            return ImmutableSet.of(self.removeTag("emit"));
        }
}
