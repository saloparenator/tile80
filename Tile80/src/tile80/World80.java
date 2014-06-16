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

import java.util.Set;
import org.javatuples.Pair;

/**
 *
 * @author martin
 */
public interface World80 {
    /**
     * null replacement
     * @return Pair(0,0)
     */
    Pair getDefaultPos();
    /**
     * null replacement
     * @return ""
     */
    String getDefaultId();
    /**
     * null replacement
     * @return Tag80.nothing
     */
    Tag80 getDefaultTags();
    /**
     * tile that act if there is nothing
     * @return Tile80.nothing
     */
    Tile80 getDefaultTile();
    
    /**
     * all tile in world
     * @return 
     */
    Iterable<Tile80> getTileLst();
    /**
     * find tile at giventh position
     * @param pos
     * @return 
     */
    Tile80 getTileByPos(Pair pos);
    /**
     * find tile by id
     * @param Symbol
     * @return 
     */
    Tile80 getTileById(String id);
    /**
     * find all tile with giventh tag
     * @param tag
     * @return 
     */
    Iterable<Tile80> getTileByTag(Tag80 tag);
    /**
     * find all tile within rect
     * @param topLeft
     * @param bottomRight
     * @return 
     */
    Iterable<Tile80> getTileByRect(Pair<Integer,Integer> topLeft,
                                   Pair<Integer,Integer> bottomRight);
    
    /**
     * return position of giventh id
     * @param id
     * @return 
     */
    Pair<Integer,Integer> getPosById(String id);
    /**
     * return all tag linked to giventh id
     * @param id
     * @return 
     */
    Iterable<Tag80> getTagById(String id);
    
    /**
     * apply crunch on every tile with all related tag
     * @param event
     * @return 
     */
    World80 crunch(Set<String> event);
}
