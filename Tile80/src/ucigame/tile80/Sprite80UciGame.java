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
import tile80.Sprite80;
import ucigame.Image;
import ucigame.Sprite;
import ucigame.Ucigame;

/**
 * implementation of the Spritefactory for the ucigame library
 * will produce sprite for ucigame in place, we will use short lived sprite at every frame
 */
public class Sprite80UciGame extends Sprite80<Sprite>{
    private static final Logger LOG = Logger.getLogger(Sprite80UciGame.class.getName());

    private final Image spriteSheet;
    private final Ucigame host;

    /**
     * 
     * @param spriteSheet
     * @param host
     * @param name
     * @param mirror
     * @param w
     * @param h
     * @param frameLst 
     */
    public Sprite80UciGame(Image spriteSheet, 
                                Ucigame host, 
                                String name, 
                                boolean mirror, 
                                int w, 
                                int h, 
                                List<Pair<Integer, Integer>> frameLst) {
        super(name, mirror, w, h, frameLst);
        this.spriteSheet = spriteSheet;
        this.host = host;
    }

    @Override
    public Sprite makeSprite(int col, int row) {
        Sprite tmp = host.makeSprite(getW(), getH());
        for (Pair<Integer,Integer> p : getFrameList())
            tmp.addFrame(spriteSheet, p.getValue0(), p.getValue1());
        if (!this.isLoop())
            tmp.play("All", Ucigame.ONCE);
        tmp.position(col*getW(), row*getH());
        return tmp;
    }

    /**
     * 
     * @param host
     * @param json
     * @param w
     * @param h
     * @return 
     */
    public static Map<String,Sprite80UciGame> makeSpriteFactoryUciGame(Ucigame host, 
                                                                     String json, 
                                                                     int w, 
                                                                     int h){
        ImmutableMap.Builder<String,Sprite80UciGame> mapSpriteBuilder = ImmutableMap.builder();
        
        Map<String,Object> spriteSheetStructure = new Gson().fromJson(json, Map.class);
        Image spriteSheetImage = host.getImage(spriteSheetStructure.get("file").toString());
        for (Map<String,Object> frameStructure : (Collection<Map>)spriteSheetStructure.get("sheet")){
            ImmutableList.Builder<Pair<Integer,Integer>> frameListBuilder = ImmutableList.builder();
            for (Map<String, Double> box : (Collection<Map>)frameStructure.get("frame")){
               frameListBuilder.add(new Pair(box.get("x").intValue(), 
                                             box.get("y").intValue()));
            }
            mapSpriteBuilder.put(frameStructure.get("name").toString(),            
                                   new Sprite80UciGame(spriteSheetImage,
                                                            host,
                                                            frameStructure.get("name").toString(), 
                                                            frameStructure.containsKey("loop"), 
                                                            w, 
                                                            h, 
                                                            frameListBuilder.build()));
        }
        
        return mapSpriteBuilder.build();
    }
}
