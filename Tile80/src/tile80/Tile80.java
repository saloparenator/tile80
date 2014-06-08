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
public interface Tile80 {


    /**
     * return the coordinate were the symbol is located
     * @param symbol
     * @return
     */
    Pair getCoordBySymbol(String symbol);

    /**
     * all coordinate occupied in the world
     * @return
     */
    Iterable<Pair> getCoordLst();

    /**
     * get symbol by coordinate
     * @param coord
     * @return
     */
    String getSymbolByCoord(Pair coord);

    /**
     * get all symbol were coordinate is between topleft and bottomright
     * @param topLeft
     * @param bottomRight
     * @return
     */
    Iterable<String> getSymbolByRect(Pair topLeft, Pair bottomRight);

    /**
     * all symbol in the world
     * @return
     */
    Iterable<String> getSymbolLst();

    /**
     * all symbol with at least one of these tag
     * @param tag
     * @return
     */
    Iterable<String> getSymbolLstByAnyTag(Collection<String> tag);

    /**
     * all symbol containing this tag
     * @param tag
     * @return
     */
    Iterable<String> getSymbolLstByTag(String tag);

    /**
     * all symbol containing all tag given
     * @param taglst
     * @return
     */
    Iterable<String> getSymbolLstByTagLst(Collection<String> taglst);

    /**
     * all symbol adjacent to given one
     * @param symbol
     * @return
     */
    Iterable<String> getSymbolNeighbor(String symbol);

    /**
     * get all tag that the given symbol contain
     * @param symbol
     * @return
     */
    Iterable<String> getTagBySymbol(String symbol);

    /**
     * get all tag that exist in this world
     * @return
     */
    Iterable<String> getTagLst();

    /**
     * 
     * @param symbol
     * @param x
     * @param y 
     */
    Tile80 moveSymbol(String symbol, int x, int y);

    /**
     * 
     * @param symbol
     * @param tag 
     */
    Tile80 addTag(String symbol, String tag);
    
    /**
     * 
     * @param symbol
     * @param tag 
     */
    Tile80 removeTag(String symbol, String tag);
    
    /**
     * insert a new symbol in the world at given coordinate
     * @param symbol
     * @param x
     * @param y 
     */
    Tile80 addSymbol(String symbol, int x, int y);
    
    /**
     * remove symbol from the world
     * @param symbol 
     */
    Tile80 removeSymbol(String symbol);
}
