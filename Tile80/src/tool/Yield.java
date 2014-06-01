/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tool;

import java.util.Iterator;

/**
 *
 * @author martin
 */
public abstract class Yield<T> implements Iterable<T>{
    private final Iterator<T> iterator = new Iterator<T>() {
        @Override
        public boolean hasNext() {
            return end();
        }
        @Override
        public T next() {
            return yield();
        }
        @Override
        public void remove() {}
    };
    
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }
    
    public abstract boolean end();
    
    public abstract T yield();
    
    public static class Range extends Yield<Integer>{
        private final int from,to,step;
        private int current;
        private Range(int from, int to,int step){
            this.from=from;
            this.to=to;
            this.step=step;
            current=from;
        }
        public static Yield<Integer> size(int size){
            return new Range(0,size,1);
        }
        public static Yield<Integer> fromTo(int from, int to){
            return new Range(from,to,1);
        }
        public static Yield<Integer> fromToStepping(int from, int to, int step){
            return new Range(from,to,step);
        }

        @Override
        public boolean end() {
            if (step>0)
                return current>=to;
            return current<=from;
        }

        @Override
        public Integer yield() {
            int tmp = current;
            current+=step;
            return tmp;
        }
    }
}
