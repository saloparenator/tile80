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

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author martin
 */
public class Link<A,B> {
    private final ImmutableMap<A,B> left;
    private final ImmutableMap<B,A> right;

    public Link(ImmutableMap<A, B> left, ImmutableMap<B, A> right) {
        this.left = left;
        this.right = right;
    }
    
    public static<A,B> Builder<A,B> builder(){
        return new Builder();
    }

    public B getLeft(A a){
        return left.get(a);
    }
    
    public A getRight(B b){
        return right.get(b);
    }
    
    public Set<A> leftSet(){
        return left.keySet();
    }
    
    public Set<B> rightSet(){
        return right.keySet();
    }
    
    public Map<A,B> leftMap(){
        return left;
    }
    
    public Map<B,A> rightMap(){
        return right;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.left);
        hash = 23 * hash + Objects.hashCode(this.right);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Link<?, ?> other = (Link<?, ?>) obj;
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        return true;
    }
    
    
    
    public static class Builder<A,B>{
        private final Map<A,B> left;
        private final Map<B,A> right;
        
        public Builder(){
            left=new HashMap<>();
            right=new HashMap<>();
        }
        
        public Builder link(A a, B b){
            if(!left.containsKey(a) && !right.containsKey(b)){
                left.put(a, b);
                right.put(b, a);
            }  
            return this;
        }
        
        public Link<A,B> build(){
            return new Link(ImmutableMap.copyOf(left),ImmutableMap.copyOf(right));
        }
    }
}
