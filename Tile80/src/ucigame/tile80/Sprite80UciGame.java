/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ucigame.tile80;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import tile80.Sprite80;
import ucigame.Image;
import ucigame.Sprite;
import ucigame.Ucigame;

/**
 * implementation of the Spritefactory for the ucigame library
 * will produce sprite for ucigame in place, we will use short lived sprite at every frame
 * 
 * note: i first tried to use inner api facility like sprite animation, but using it complexified the engine alot
 * and imply lot of side effect, so i dropped it and let the FSM engine manage animation frame
 */
public class Sprite80UciGame extends Sprite80<Sprite>{
    private static final Logger LOG = Logger.getLogger(Sprite80UciGame.class.getName());

    private final Image spriteSheet;
    private final Ucigame host;

    public Sprite80UciGame(Image spriteSheet, 
                                Ucigame host, 
                                String name, 
                                int w, 
                                int h, 
                                int x,
                                int y) {
        super(name, w, h, x,y);
        this.spriteSheet = spriteSheet;
        this.host = host;
    }

    @Override
    public Sprite makeSprite(int col, int row) {
        Sprite tmp = host.makeSprite(getW(), getH());
        tmp.addFrame(spriteSheet, getX(), getY());
        tmp.position(col*getW(), row*getH());
        return tmp;
    }

    /**
     * 
     * @param host
     * @param json
     * @return 
     */
    public static Map<String,Sprite80UciGame> makeSpriteFactoryUciGame(Ucigame host, 
                                                                     String json){
        ImmutableMap.Builder<String,Sprite80UciGame> mapSpriteBuilder = ImmutableMap.builder();
        
        Map<String,Object> spriteSheetStructure = new Gson().fromJson(json, Map.class);
        int w = ((Double)spriteSheetStructure.get("w")).intValue();
        int h = ((Double)spriteSheetStructure.get("h")).intValue();
        Image spriteSheetImage = host.getImage(spriteSheetStructure.get("file").toString());
        for (Map<String,Object> frameStructure : (Collection<Map>)spriteSheetStructure.get("sheet")){
            mapSpriteBuilder.put(frameStructure.get("name").toString(),            
                                 new Sprite80UciGame(spriteSheetImage,
                                                     host,
                                                     frameStructure.get("name").toString(), 
                                                     w, 
                                                     h, 
                                                     ((Double)frameStructure.get("x")).intValue(),
                                                     ((Double)frameStructure.get("y")).intValue()));
        }
        
        return mapSpriteBuilder.build();
    }
}
