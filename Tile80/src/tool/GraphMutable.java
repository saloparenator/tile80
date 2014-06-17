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

package tool;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Many to Many HashBag
 * It act like a MultiBiMap
 * @author Martin Robinson nosnibor.nitram@gmail.com
 * @param <A> first type
 * @param <B> second type
 */
public class GraphMutable<A,B>{
    private final Multimap<A,B> left;
    private final Multimap<B,A> right;
    
    public GraphMutable(){
        left= HashMultimap.create();
        right=HashMultimap.create();
    }
    
    public Iterable<A> leftSet(){
        return left.keySet();
    }
    
    public Iterable<B> rightSet(){
        return right.keySet();
    }
    /**
     * link two object together
     * @param a first object
     * @param b second object
     * @return true is not already linked
     */
    public boolean link(A a, B b){
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        return left.put(a, b) && right.put(b, a);
    }
    
    /**
     * un link two object
     * @param a first object
     * @param b second object
     * @return true if they were linked
     */
    public boolean unlink(A a, B b){
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        return left.remove(a, b) && right.remove(b, a);
    }
    
    /**
     * get all object linked to the given object of type A
     * @param a as the object of type A
     * @return a set of object or empty set
     */
    public Set<B> neighborLeft(A a){
        Preconditions.checkNotNull(a);
        return ImmutableSet.copyOf(left.get(a));
    }
 
    /**
     * get all object linked to the given object of type B
     * @param b as the object of type B
     * @return a set of object or empty set
     */
    public Set<A> neighborRight(B b){
        Preconditions.checkNotNull(b);
        return ImmutableSet.copyOf(right.get(b));
    }
    
    /**
     * remove an object of Type A and unlink all Type B object who was linked to
     * @param a type A object
     * @return list of type B object that was linked
     */
    public Collection<B> removeLeft(A a){
        Preconditions.checkNotNull(a);
        Collection<B> removed = left.removeAll(a);
        for (B b : removed)
            right.remove(b, a);
        return removed;
    }
 
    /**
     * remove an object of Type B and unlink all Type A object who was linked to
     * @param b type B object
     * @return list of type A object that was linked
     */
    public Collection<A> removeRight(B b){
        Preconditions.checkNotNull(b);
        Collection<A> removed = right.removeAll(b);
        for (A a: removed)
            left.remove(a, b);
        return removed;
    }

    @Override
    public String toString() {
        return "Graph80{" + "left=" + left + ", right=" + right + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.left);
        hash = 41 * hash + Objects.hashCode(this.right);
        return hash;
    }

}
