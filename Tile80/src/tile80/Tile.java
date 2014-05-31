/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tile80;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
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

/**
 *
 * @author martin
 */
public class Tile {
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
    private Tile(Pair coord,Pair last, String symbol, Collection<String> tag) {
        this.coord=coord;
        this.last=coord;
        this.symbol = symbol;
        this.tag = tag;
    }
    
    public static Collection<Tile> fromJson(String json){
        ImmutableSet.Builder<Tile> builder = ImmutableSet.builder();
        
        Collection<Map<String,Object>> tileSet = new Gson().fromJson(json,Collection.class);
        for (Map<String,Object> tile : tileSet){
            Pair coord = new Pair(((Double)tile.get("x")).intValue(),
                                  ((Double)tile.get("y")).intValue());
            builder.add(new Tile(coord,
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
        else if (obj instanceof Tile) {
            final Tile other = (Tile) obj;
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
    public Tile move(int x, int y){
        return new Tile(new Pair(x,y),coord,symbol,tag);
    }
    
    /* FUNCTOR */

    public static Iterable<Pair<Integer,Integer>> getNeighbor(final Pair<Integer,Integer> center){
        return new Coord.Neighbor(center);
    }
    
    /*find collection by tag*/
    public static Iterable<Tile> findByTagLst(Collection<Tile>tileLst, final Collection<String> taglst){
        return Iterables.filter(tileLst, new Predicate<Tile>() {
            @Override
            public boolean apply(Tile input) {
                return input.getTag().containsAll(taglst);
            }
        });
    }
    public static Iterable<Tile> findByTag(Collection<Tile>tileLst, final String tag){
        return Iterables.filter(tileLst, new Predicate<Tile>() {
            @Override
            public boolean apply(Tile input) {
                return input.getTag().contains(tag);
            }
        });
    }
    public static Iterable<Tile> findByAnyTag(Collection<Tile>tileLst, final Collection<String> taglst){
        return Iterables.filter(tileLst, new Predicate<Tile>() {
            @Override
            public boolean apply(Tile input) {
                for (String tag : taglst)
                    if (input.getTag().contains(tag))
                            return true;
                return false;
            }
        });
    }

    /* find tile in collection by symbol */
    public static final Iterable<Tile> findBySymbol(Iterable<Tile> from, String withSymbol)
    {
        return Iterables.filter(from, Predicates.compose(Predicates.equalTo(withSymbol), new Function<Tile, String>() {
        @Override
        public String apply(Tile input) {
            return input.getSymbol();
        }
    }));
    }
    
    /* find tile in collection by coordinate */
    private static final Function<Tile, Pair> onlyCoord = new Function<Tile, Pair>() {
        @Override
        public Pair apply(Tile input) {
            return input.getCoord();
        }
    };
    public static final Iterable<Tile> findByCoordLst(Iterable<Tile> from, Collection<Pair> with)
    {
        return Iterables.filter(from, Predicates.compose(Predicates.in(with), onlyCoord));
    }
    public static final Iterable<Tile> findByCoord(Iterable<Tile> from,Pair withPair)
    {
        return Iterables.filter(from, Predicates.compose(Predicates.equalTo(withPair), onlyCoord));
    }
    public static final Iterable<Tile> findInRect(Iterable<Tile> world,Pair topLeft, Pair bottomRight)
    {
        return Iterables.filter(world, Predicates.compose(Range.closed(topLeft, bottomRight), onlyCoord));
    }

    /*test*/

    public static void main(String ... arg) {
        Tile t = new Tile(new Pair(4, 4),null, "hello", Arrays.asList(new String[]{"un","deux",}));
        Tile tt = new Tile(new Pair(3, 4),null, "bye", Arrays.asList(new String[]{"trois",}));
        Tile ttt = new Tile(new Pair(4, 3),null, "fuck", Arrays.asList(new String[]{"deux",}));
        for (Pair p : getNeighbor(t.coord))
            System.out.println(p);
        
        Set<Tile> coll = ImmutableSet.of(t,tt,ttt);
        Set<Pair> coll2 = ImmutableSet.of(new Pair(4,4));
        Set<String> coll3 = ImmutableSet.of("deux","trois");

        for (Tile tttt : Iterables.filter(coll, Predicates.compose(Predicates.in(coll2), onlyCoord)))
            System.out.println(tttt.getSymbol());
        
        for (Tile tl : findByTag(coll, "deux"))
            System.out.println(tl.symbol);
        for (Tile tl : findByAnyTag(coll, coll3))
            System.out.println(tl.symbol);
        for (Tile tl : findByTagLst(coll, coll3))
            System.out.println(tl.symbol);
        
        Pair tl = new Pair(1,1),
                dr = new Pair(2,3);
        
        for (int x=0;x<4;x++)
            for (int y=0;y<4;y++)
                System.out.println(x+" "+y+" "+Range.closed(tl, dr).apply(new Pair(x,y)));
    }
}
