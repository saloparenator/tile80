/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tile80;

import example.GameOfLife;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Range;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.javatuples.Pair;
import ucigame.*;

/**
 *
 * @author martin
 */
public class TileUciGame extends Ucigame{
    private static final Logger LOG = Logger.getLogger(TileUciGame.class.getName());

    /**
     * load json containing spritesheet and build spriteMap
     * @param spriteSheetJson
     * @param w
     * @param h
     * @return 
     */
    public Map<String,Sprite> loadSpriteSheet(String spriteSheetJson,int w, int h)
    {
        ImmutableMap.Builder<String,Sprite> spriteSheetBuilder = ImmutableMap.builder();
        
        Map<String,Object> spriteSheetStructure = new Gson().fromJson(spriteSheetJson, Map.class);
        Image spriteSheetImage = getImage(spriteSheetStructure.get("file").toString());
        for (Map<String,Object> frameStructure : (Collection<Map>)spriteSheetStructure.get("sheet"))
        {
                Sprite tmp = makeSprite(w, h);
                for (Map<String, Double> box : (Collection<Map>)frameStructure.get("frame"))
                   tmp.addFrame(spriteSheetImage, box.get("x").intValue(), box.get("y").intValue());
                spriteSheetBuilder.put(frameStructure.get("name").toString(), tmp);
        }
        
        return spriteSheetBuilder.build();
    }
}
