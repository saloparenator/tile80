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

import org.javatuples.Pair;

/**
 * this object will decorate the giventh world
 * @author martin
 */
public class Event80 implements World80{
    private final World80 delegate;
    private Event80(World80 world){
        delegate=world;
    }
    
    @Override
    public Pair getDefaultPos() {
        return delegate.getDefaultPos();
    }

    @Override
    public String getDefaultId() {
        return delegate.getDefaultId();
    }

    @Override
    public Tag80 getDefaultTags() {
        return delegate.getDefaultTags();
    }

    @Override
    public Tile80 getDefaultTile() {
        return delegate.getDefaultTile();
    }

    @Override
    public Iterable<Tile80> getTileLst() {
        return delegate.getTileLst();
    }

    @Override
    public Tile80 getTileByPos(Pair pos) {
        return delegate.getTileByPos(pos);
    }

    @Override
    public Tile80 getTileById(String Symbol) {
        return delegate.getTileById(Symbol);
    }

    @Override
    public Iterable<Tile80> getTileByTag(Tag80 tag) {
        return delegate.getTileByTag(tag);
    }

    @Override
    public Pair getPosById(String symbol) {
        return delegate.getPosById(symbol);
    }

    @Override
    public Iterable<Tag80> getTagById(String symbol) {
        return delegate.getTagById(symbol);
    }

    @Override
    public Iterable<Tile80> getTileByRect(Pair<Integer, Integer> topLeft, Pair<Integer, Integer> bottomRight) {
        return delegate.getTileByRect(topLeft,bottomRight);
    }

}
