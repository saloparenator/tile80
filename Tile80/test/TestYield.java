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

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;
import tool.Yield;

/**
 *
 * @author martin
 */
public class TestYield {
  
    @Test
    public void testRangeSize(){
        Integer j=0;
        for (Integer i : Yield.Range.size(10))
            assertEquals(j++,i);
        assertEquals(new Integer(10),j);
    }
    
    @Test
    public void testRangeFromTo(){
        Integer j=10;
        for (Integer i : Yield.Range.fromTo(10, 20))
            assertEquals(j++,i);
        assertEquals(new Integer(20),j);
    }
    
    @Test
    public void testRangeFromToStep(){
        Integer j=10;
        for (Integer i : Yield.Range.fromToStepping(10, 20,1))
            assertEquals(j++,i);
        assertEquals(new Integer(20),j);
    }
    
    @Test
    public void testRangeSizeReverse(){
        Integer j=0;
        for (Integer i : Yield.Range.size(-10))
            assertEquals(j--,i);
        assertEquals(new Integer(-10),j);
    }
    
    @Test
    public void testRangeFromToReverse(){
        Integer j=10;
        for (Integer i : Yield.Range.fromTo(10, 0))
            assertEquals(j--,i);
        assertEquals(new Integer(0),j);
    }
    
    @Test
    public void testRangeFromToStepReverse(){
        Integer j=10;
        for (Integer i : Yield.Range.fromToStepping(10, 0,-1))
            assertEquals(j--,i);
        assertEquals(new Integer(0),j);
    }
    
    @Test
    public void testImmedOfVarArg(){
        int sum=0;
        for (Integer i : Yield.Immed.Of(1,2,3,4,5))
            sum+=i;
        assertEquals(15,sum);
    }
    
    @Test
    public void testImmedOfArray(){
        int sum=0;
        for (Integer i : Yield.Immed.Of(new Integer[]{1,2,3,4,5}))
            sum+=i;
        assertEquals(15,sum);
    }

}
