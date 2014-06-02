/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tile80;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.javatuples.Pair;

/**
 * Simple container that keep all information required from JSON into object
 * Implementation will declare makeSprite(x,y) that will (according to library)
 * build a sprite from sprite sheet with animation/reflection/translation
 */
public abstract class Sprite80<T> {
    private final String name;
    private final int w,h,x,y;

    public Sprite80(String name, int w, int h, int x, int y){
        this.name = name;
        this.w = w;
        this.h = h;
        this.x=x;
        this.y=y;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * build in place the sprite using all information stored here
     * @param x consider this coordinate as col of getW() width
     * @param y same for height, your sprite cant be between tile
     * @return 
     */
    public abstract T makeSprite(int x, int y);
}
