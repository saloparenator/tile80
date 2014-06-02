/*
 * Copyright 2014 martin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ucigame.tile80;

import tile80.Console80;
import ucigame.Ucigame;

/**
 * Console80 implementation for ucigame
 */
public class Console80UciGame extends Console80{
    private final Ucigame host;
    private final int fontsize;
    public Console80UciGame(Ucigame host, 
                          String font, 
                          int fontsize, 
                          int r,int g, int b, 
                          int maxLen) {
        super(maxLen);
        this.host=host;
        this.fontsize=fontsize;
        host.canvas.font(font, 0, fontsize,r,g,b);
    }
    
    @Override
    public void draw(int x,int y, int qty){
        int cnt=0;
        for (String msg : getLast(qty))
            host.canvas.putText(msg, x, y+(cnt++*fontsize));
    }
    
}
