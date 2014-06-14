/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tool;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.Collection;

/**
 *
 * @author martin
 */
public final class PredicatesOnList {
    private static class ContainsAll<T> implements Predicate<Collection<T>>{
        private final Collection<T> from;
        public ContainsAll(Collection<T> from) {
            this.from=from;
        }
        
        @Override
        public boolean apply(Collection<T> input) {
            return input.containsAll(from);
        }
    };
    public static<T> Predicate<Collection<T>> containsAll(Collection<T> from){
        return new ContainsAll<>(from);
    }
    
    private static class ContainsOne<T> implements Predicate<Collection<T>>{
        private final Collection<T> from;
        public ContainsOne(Collection<T> from) {
            this.from=from;
        }

        @Override
        public boolean apply(Collection<T> input) {
            for (T tag : from)
                if (input.contains(tag))
                        return true;
            return false;
        }
    }
    public static<T> Predicate<Collection<T>> containsOne(Collection<T> from){
        return new ContainsOne<>(from);
    }
    
    private static class Contains<T> implements Predicate<Iterable<T>>{
        private final T from;
        public Contains(T from) {
            this.from=from;
        }

        @Override
        public boolean apply(Iterable<T> input) {
            return Iterables.contains(input,from);
        }
    }
    public static<T> Predicate<Iterable<T>> contains(T from){
        return new Contains<>(from);
    }
}
