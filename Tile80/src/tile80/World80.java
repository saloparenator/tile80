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

import java.util.Collection;
import org.javatuples.Pair;

/**
 *
 * @author martin
 */
public interface World80 {

    Pair getDefaultPos();
    String getDefaultId();
    Tag80 getDefaultTags();
    Tile80 getDefaultTile();
    
    Iterable<Tile80> getTileLst();
    Tile80 getTileByPos(Pair pos);
    Tile80 getTileById(String Symbol);
    Iterable<Tile80> getTileByTag(Tag80 tag);
    Iterable<Tile80> getTileByRect(Pair<Integer,Integer> topLeft,
                                   Pair<Integer,Integer> bottomRight);
    
    Pair<Integer,Integer> getPosById(String id);
    Iterable<Tag80> getTagById(String id);
}
