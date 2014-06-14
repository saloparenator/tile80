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

    Pair getPairDefault();
    String getSymbolDefault();
    Tag80 getTagDefault();
    Tile80 getTileDefault();
    
    Iterable<Tile80> getTileLst();
    Tile80 getTileByCoord(Pair pos);
    Tile80 getTileBySymbol(String Symbol);
    Iterable<Tile80> getTileByTag(Tag80 tag);

    /**
     * all coordinate occupied in the world
     * @return
     */
    Iterable<Pair> getCoordLst();
    /**
     * get all tag that exist in this world
     * @return
     */
    Iterable<Tag80> getTagLst();
      /**
     * all symbol in the world
     * @return
     */
    Iterable<String> getSymbolLst();  
    
    /**
     * return the coordinate were the symbol is located
     * @param symbol
     * @return
     */
    Pair getCoordBySymbol(String symbol);
    
    /**
     * get all tag that the given symbol contain
     * @param symbol
     * @return
     */
    Iterable<Tag80> getTagBySymbol(String symbol);

    /**
     * get symbol by coordinate (many can overlap)
     * @param coord
     * @return
     */
    String getSymbolByCoord(Pair pos);

    /**
     * get all symbol were coordinate is between topleft and bottomright
     * @param topLeft
     * @param bottomRight
     * @return
     */
    Iterable<String> getSymbolByRect(Pair topLeft, Pair bottomRight);

    /**
     * all symbol containing this tag
     * @param tag
     * @return
     */
    Iterable<String> getSymbolLstByTag(Tag80 tag);

    /**
     * all symbol adjacent to given one
     * @param symbol
     * @return
     */
    Iterable<String> getSymbolNeighbor(String symbol);





}
