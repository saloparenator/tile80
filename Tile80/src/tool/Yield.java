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
            return !end();
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
            this.current=from;
        }
        public static Yield<Integer> size(int size){
            if (size<0)
                return new Range(0,size,-1);
            return new Range(0,size,1);
        }
        public static Yield<Integer> fromTo(int from, int to){
            if (from<to)
                return new Range(from,to,1);
            return new Range(from,to,-1);
        }
        public static Yield<Integer> fromToStepping(int from, int to, int step){
            return new Range(from,to,step);
        }

        @Override
        public boolean end() {
            if (this.step==0)
                return true;
            else if (this.step>0)
                return this.current>=this.to;
            return this.current<=this.to;
        }

        @Override
        public Integer yield() {
            int tmp = this.current;
            this.current+=this.step;
            return tmp;
        }
    }
    
    public static class Immed<T> extends Yield<T>{
        public final T[] array;
        int pos;
        private Immed(T ... arg){
            array = arg;
            pos=0;
        }
        
        public static<T> Yield<T> Of(T ... arg){
            return new Immed(arg);
        }

        @Override
        public boolean end() {
            return pos==array.length;
        }

        @Override
        public T yield() {
            return array[pos++];
        }
    }
}
