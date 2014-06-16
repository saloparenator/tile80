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
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

/**
 *
 * @author martin
 */
public class ImmutableGraph<A,B> {
    private final ImmutableMultimap<A,B> left;
    private final ImmutableMultimap<B,A> right;

    private ImmutableGraph(ImmutableMultimap<A, B> left, ImmutableMultimap<B, A> right) {
        this.left = left;
        this.right = right;
    }

    public static<A,B> ImmutableGraph.Builder<A,B> builder(){
        return new ImmutableGraph.Builder();
    }
    
    public Iterable<A> leftSet(){
        return left.keySet();
    }
    
    public Iterable<B> rightSet(){
        return right.keySet();
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

    public static class Builder<A,B>{
        private final ImmutableMultimap.Builder<A,B> left;
        private final ImmutableMultimap.Builder<B,A> right;

        public Builder() {
            this.left = ImmutableMultimap.builder();
            this.right = ImmutableMultimap.builder();
        }
        
        /**
         * link two object together
         * @param a first object
         * @param b second object
         * @return true is not already linked
         */
        public Builder link(A a, B b){
            Preconditions.checkNotNull(a);
            Preconditions.checkNotNull(b);
            left.put(a, b);
            right.put(b, a);
            return this;
        }
        
        public ImmutableGraph<A,B> build(){
            return new ImmutableGraph(left.build(), right.build());
        }
    }
}
