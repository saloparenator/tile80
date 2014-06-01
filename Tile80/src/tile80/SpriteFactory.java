/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tile80;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.javatuples.Pair;
import ucigame.Image;
import ucigame.Sprite;
import ucigame.Ucigame;

/**
 * Simple container that keep all information required from JSON into object
 * Implementation will declare makeSprite(x,y) that will (according to library)
 * build a sprite from sprite sheet with animation/reflection/translation
 */
public abstract class SpriteFactory<T> {


    //example of factory
//    public Map<String,SpriteFactoryUciGame> makeSpriteFactoryUciGame(Ucigame host, String json, int w, int h){
//        ImmutableMap.Builder<String,SpriteFactoryUciGame> mapSpriteBuilder = ImmutableMap.builder();
//        
//        Map<String,Object> spriteSheetStructure = new Gson().fromJson(json, Map.class);
//        Image spriteSheetImage = host.getImage(spriteSheetStructure.get("file").toString());
//        for (Map<String,Object> frameStructure : (Collection<Map>)spriteSheetStructure.get("sheet"))
//        {
//            ImmutableList.Builder<Pair<Integer,Integer>> frameListBuilder = ImmutableList.builder();
//            for (Map<String, Double> box : (Collection<Map>)frameStructure.get("frame")){
//               frameListBuilder.add(new Pair(box.get("x").intValue(), 
//                                             box.get("y").intValue()));
//            }
//            mapSpriteBuilder.put(frameStructure.get("name").toString(),            
//                                   new SpriteFactoryUciGame(spriteSheetImage,
//                                                            host,
//                                                            frameStructure.get("name").toString(), 
//                                                            frameStructure.containsKey("mirror"), 
//                                                            w, 
//                                                            h, 
//                                                            frameListBuilder.build()));
//        }
//        
//        return mapSpriteBuilder.build();
//    }

    private final String name;
    private final boolean mirror;
    private final int w,h;
    private final List<Pair<Integer,Integer>> frameLst;

    public SpriteFactory(String name, boolean mirror, int w, int h, List<Pair<Integer,Integer>> frameLst) {
        this.name = name;
        this.mirror = mirror;
        this.w = w;
        this.h = h;
        this.frameLst = frameLst;
    }

    /**
     * name to identify the sprite
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * width of the sprite
     * @return 
     */
    public int getW() {
        return w;
    }

    /**
     * height of the sprite
     * @return 
     */
    public int getH() {
        return h;
    }

    /**
     * true if the sprite is flip horizontally from original
     * @return 
     */
    public boolean idMirror(){
        return mirror;
    }

    /**
     * immutable list of all frame the sprite will animate
     * @return 
     */
    public List<Pair<Integer,Integer>> getFrameList(){
        return ImmutableList.copyOf(frameLst);
    }
    
    /**
     * build in place the sprite using all information stored here
     * @param x consider this coordinate as col of getW() width
     * @param y same for height, your sprite cant be between tile
     * @return 
     */
    public abstract T makeSprite(int x, int y);
}
