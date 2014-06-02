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

package tile80;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * simple console that show text on screen, all message and information
 */
public abstract class Console80 {
    private List<String> queue;
    private int maxLen;
    
    public Console80(int maxLen){
        queue=new LinkedList<>();
        maxLen=maxLen;
    }
    
    /**
     * add a new message at end of the queue
     * @param message 
     */
    public void addMessage(String message){
        queue.add(message.substring(maxLen));
    }
    
    /**
     * return nth last message
     * @param qty
     * @return 
     */
    public List<String> getLast(int qty){
        int len = queue.size();
        if (queue.size()<=qty)
            return ImmutableList.copyOf(queue);
        return queue.subList(len-qty, len);
    }
    
    /**
     * draw nth last message
     * @param x
     * @param y
     * @param qty 
     */
    public abstract void draw(int x,int y, int qty);
}
