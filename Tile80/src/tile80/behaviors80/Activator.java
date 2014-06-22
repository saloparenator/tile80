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
import tile80.tile80.Tile80;
import tile80.world80.World80;

/**
 *
 * @author martin
 */
public class Activator extends Behavior80{
        @Override
        public String getName() {
            return "activator";
        }

        @Override
        public String getDescription() {
            return "just for switch";
        }

        @Override
        public Iterable<Tile80> crunch(Tile80 self, World80 world, Set<String> event) {
            if (event.contains("activate"))
                return ImmutableSet.of(self.addTag("activate"));
            return ImmutableSet.of(self.removeTag("activate"));
        }
}
