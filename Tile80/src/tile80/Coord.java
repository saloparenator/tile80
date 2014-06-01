/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tile80;

import tool.Yield;
import org.javatuples.Pair;

/**
 *
 * @author martin
 */
public final class Coord {
    public static class Neighbor extends Yield<Pair<Integer,Integer>>{
        private final Pair<Integer,Integer> center;
        int i;
        public Neighbor(Pair<Integer,Integer> center){
            this.center=center;
            i=0;
        }
        @Override
        public boolean end() {
            return i<9;
        }
        @Override
        public Pair<Integer, Integer> yield() {
            Pair p = new Pair(center.getValue0()+i%3-1,
                              center.getValue1()+i/3-1);
            i += 1+(i==3?1:0);
            return p;
        }
        
    };
}
