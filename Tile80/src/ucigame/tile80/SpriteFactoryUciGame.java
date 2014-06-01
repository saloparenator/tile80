/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ucigame.tile80;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.javatuples.Pair;
import tile80.SpriteFactory;
import ucigame.Image;
import ucigame.Sprite;
import ucigame.Ucigame;

/**
 * implementation of the Spritefactory for the ucigame library
 * will produce sprite for ucigame in place, we will use short lived sprite at every frame
 */
public class SpriteFactoryUciGame extends SpriteFactory<Sprite>{
    private static final Logger LOG = Logger.getLogger(SpriteFactoryUciGame.class.getName());

    private final Image spriteSheet;
    private final Ucigame host;

    public SpriteFactoryUciGame(Image spriteSheet, Ucigame host, String name, boolean mirror, int w, int h, List<Pair<Integer, Integer>> frameLst) {
        super(name, mirror, w, h, frameLst);
        this.spriteSheet = spriteSheet;
        this.host = host;
    }

    public static Map<String,SpriteFactoryUciGame> makeSpriteMapFactoryUciGame(Ucigame host, 
                                                                               String json, 
                                                                               int w, 
                                                                               int h){
        ImmutableMap.Builder<String,SpriteFactoryUciGame> mapSpriteBuilder = ImmutableMap.builder();

        Map<String,Object> spriteSheetStructure = new Gson().fromJson(json, Map.class);
        Image spriteSheetImage = host.getImage(spriteSheetStructure.get("file").toString());
        for (Map<String,Object> frameStructure : (Collection<Map>)spriteSheetStructure.get("sheet"))
        {
            ImmutableList.Builder<Pair<Integer,Integer>> frameListBuilder = ImmutableList.builder();
            for (Map<String, Double> box : (Collection<Map>)frameStructure.get("frame")){
               frameListBuilder.add(new Pair(box.get("x").intValue(), 
                                             box.get("y").intValue()));
            }
            mapSpriteBuilder.put(frameStructure.get("name").toString(),            
                                 new SpriteFactoryUciGame(spriteSheetImage,
                                                          host,
                                                          frameStructure.get("name").toString(), 
                                                          frameStructure.containsKey("mirror"), 
                                                          w, 
                                                          h, 
                                                          frameListBuilder.build()));
        }

        return mapSpriteBuilder.build();
    }

    public Sprite makeSprite(int x, int y) {
        Sprite tmp = host.makeSprite(getW(), getH());
        for (Pair<Integer,Integer> p : getFrameList())
            tmp.addFrame(spriteSheet, p.getValue0(), p.getValue1());
        if (this.idMirror())
            tmp.flipHorizontal();
        tmp.position(x, y);
        return tmp;
    }

    
    public Map<String,SpriteFactoryUciGame> makeSpriteFactoryUciGame(Ucigame host, String json, int w, int h){
        ImmutableMap.Builder<String,SpriteFactoryUciGame> mapSpriteBuilder = ImmutableMap.builder();
        
        Map<String,Object> spriteSheetStructure = new Gson().fromJson(json, Map.class);
        Image spriteSheetImage = host.getImage(spriteSheetStructure.get("file").toString());
        for (Map<String,Object> frameStructure : (Collection<Map>)spriteSheetStructure.get("sheet"))
        {
            ImmutableList.Builder<Pair<Integer,Integer>> frameListBuilder = ImmutableList.builder();
            for (Map<String, Double> box : (Collection<Map>)frameStructure.get("frame")){
               frameListBuilder.add(new Pair(box.get("x").intValue(), 
                                             box.get("y").intValue()));
            }
            mapSpriteBuilder.put(frameStructure.get("name").toString(),            
                                   new SpriteFactoryUciGame(spriteSheetImage,
                                                            host,
                                                            frameStructure.get("name").toString(), 
                                                            frameStructure.containsKey("mirror"), 
                                                            w, 
                                                            h, 
                                                            frameListBuilder.build()));
        }
        
        return mapSpriteBuilder.build();
    }
}
