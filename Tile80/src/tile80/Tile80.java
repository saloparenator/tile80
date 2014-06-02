/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tile80;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.javatuples.Pair;
import tool.Coord;
import tool.PredicatesOnList;

/**
 *
 * @author martin
 */
public class Tile80 {
    private final Pair<Integer,Integer> coord;
    private final Pair<Integer,Integer> last;
    private final String symbol;
    private final Collection<String> tag;

    /**
     * constructor
     * @param coord
     * @param symbol
     * @param tag 
     */
    private Tile80(Pair coord,Pair last, String symbol, Collection<String> tag) {
        this.coord=coord;
        this.last=coord;
        this.symbol = symbol;
        this.tag = tag;
    }
    
    public static Collection<Tile80> fromJson(String json){
        ImmutableSet.Builder<Tile80> builder = ImmutableSet.builder();
        
        Collection<Map<String,Object>> tileSet = new Gson().fromJson(json,Collection.class);
        for (Map<String,Object> tile : tileSet){
            Pair coord = new Pair(((Double)tile.get("x")).intValue(),
                                  ((Double)tile.get("y")).intValue());
            builder.add(new Tile80(coord,
                                 coord,
                                 (String)tile.get("symbol"),
                                 (Collection)tile.get("tag")));
        }
        
        return builder.build();
    }
    
    /**
     * current coordinate
     * @return 
     */
    public Pair getCoord(){
        return coord;
    }
    
    /**
     * last coordinate since translation
     * @return 
     */
    public Pair getLast(){
        return last;
    }

    /**
     * the symbol to identify the tile
     * @return 
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * all tag for this tile
     * @return 
     */
    public Collection<String> getTag() {
        return tag;
    }
    
        @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.coord);
        hash = 53 * hash + Objects.hashCode(this.last);
        hash = 53 * hash + Objects.hashCode(this.symbol);
        hash = 53 * hash + Objects.hashCode(this.tag);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        else if(obj==this){
            return true;
        }
        else if (obj instanceof Tile80) {
            final Tile80 other = (Tile80) obj;
            return other.coord.equals(coord)&&
                    other.symbol.equals(symbol)&&
                    other.tag.containsAll(tag);
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "Tile{" + "coord=" + coord + ", symbol=" + symbol + ", tag=" + tag + '}';
    }
    
    /* closure */
    
    /**
     * change coordinate, keep old one
     * @param x
     * @param y
     * @return 
     */
    public Tile80 move(int x, int y){
        return new Tile80(new Pair(x,y),coord,symbol,tag);
    }
    
    /* FUNCTOR */
    private static final Function<Tile80,Collection<String>> onlyTag = new Function<Tile80,Collection<String>>(){
        @Override
        public Collection<String> apply(Tile80 input) {
            return input.getTag();
        }
    };
    
    private static final Function<Tile80,String> onlySymbol = new Function<Tile80,String>(){
        @Override
        public String apply(Tile80 input) {
            return input.getSymbol();
        }
    };
    
    private static final Function<Tile80, Pair> onlyCoord = new Function<Tile80, Pair>() {
        @Override
        public Pair apply(Tile80 input) {
            return input.getCoord();
        }
    };

    /* function on Collection<Tile80> */
    public static Iterable<Pair<Integer,Integer>> getNeighbor(final Pair<Integer,Integer> center){
        return new Coord.Neighbor(center);
    }
    public static Iterable<Tile80> findByTagLst(Collection<Tile80>tileLst, final Collection<String> taglst){
        return Iterables.filter(tileLst, Predicates.compose(PredicatesOnList.containsAll(taglst),onlyTag));
    }
    public static Iterable<Tile80> findByTag(Collection<Tile80>tileLst, final String tag){
        return Iterables.filter(tileLst, Predicates.compose(PredicatesOnList.contains(tag), onlyTag));
    }
    public static Iterable<Tile80> findByAnyTag(Collection<Tile80>tileLst, final Collection<String> taglst){
        return Iterables.filter(tileLst, Predicates.compose(PredicatesOnList.containsOne(taglst),onlyTag));
    }
    public static final Iterable<Tile80> findBySymbol(Iterable<Tile80> from, String withSymbol)
    {
        return Iterables.filter(from, Predicates.compose(Predicates.equalTo(withSymbol), onlySymbol));
    }
    public static final Iterable<Tile80> findByCoordLst(Iterable<Tile80> from, Collection<Pair> with)
    {
        return Iterables.filter(from, Predicates.compose(Predicates.in(with), onlyCoord));
    }
    public static final Iterable<Tile80> findByCoord(Iterable<Tile80> from,Pair withPair)
    {
        return Iterables.filter(from, Predicates.compose(Predicates.equalTo(withPair), onlyCoord));
    }
    public static final Iterable<Tile80> findInRect(Iterable<Tile80> world,Pair topLeft, Pair bottomRight)
    {
        return Iterables.filter(world, Predicates.compose(Range.closed(topLeft, bottomRight), onlyCoord));
    }

    /*test*/

    public static void main(String ... arg) {
        Tile80 t = new Tile80(new Pair(4, 4),null, "hello", Arrays.asList(new String[]{"un","deux",}));
        Tile80 tt = new Tile80(new Pair(3, 4),null, "bye", Arrays.asList(new String[]{"trois",}));
        Tile80 ttt = new Tile80(new Pair(4, 3),null, "fuck", Arrays.asList(new String[]{"deux",}));
        for (Pair p : getNeighbor(t.coord))
            System.out.println(p);
        
        Set<Tile80> coll = ImmutableSet.of(t,tt,ttt);
        Set<Pair> coll2 = ImmutableSet.of(new Pair(4,4));
        Set<String> coll3 = ImmutableSet.of("deux","trois");

        for (Tile80 tttt : Iterables.filter(coll, Predicates.compose(Predicates.in(coll2), onlyCoord)))
            System.out.println(tttt.getSymbol());
        
        for (Tile80 tl : findByTag(coll, "deux"))
            System.out.println(tl.symbol);
        for (Tile80 tl : findByAnyTag(coll, coll3))
            System.out.println(tl.symbol);
        for (Tile80 tl : findByTagLst(coll, coll3))
            System.out.println(tl.symbol);
        
        Pair tl = new Pair(1,1),
                dr = new Pair(2,3);
        
        for (int x=0;x<4;x++)
            for (int y=0;y<4;y++)
                System.out.println(x+" "+y+" "+Range.closed(tl, dr).apply(new Pair(x,y)));
    }
}
