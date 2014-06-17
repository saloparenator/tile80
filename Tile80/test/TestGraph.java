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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import tool.Graph;
import tool.GraphMutable;

/**
 *
 * @author martin robinson
 */
public class TestGraph {
    Graph.Builder<String,Integer> graph;
    
    @Before
    public void setUp() {
        graph = Graph.builder();
        
        long a = System.currentTimeMillis();
        for (String s : new String[]{"un","deux","trois","quatre","cinq","six","sept","huit","neuf"})
            for (int i=0;i<10;i++)
                graph.link(s, i);
        long b = System.currentTimeMillis();

        System.out.println("insertion : "+(b-a));
        System.out.println(graph.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testLinkValeurAnull(){
        graph.link(null, 999999999);
    }
    @Test(expected = NullPointerException.class)
    public void testLinkValeurBnull(){
        graph.link("B==D", null);
    }
//    @Test
//    public void testLinkValeur(){
//        graph.link("B==D", 69);
//        assertEquals(1122400448,graph.build().hashCode());
//    }
    
//    @Test(expected = NullPointerException.class) 
//    public void testUnlinkValeurAnull(){
//        graph.unlink(null, 3);
//    }
//    @Test(expected = NullPointerException.class) 
//    public void testUnlinkValeurBnull(){
//        graph.unlink("trois", null);
//    }
//    @Test
//    public void testUnlinkValeur(){
//        graph.unlink("trois", 3);
//        assertEquals(926637688,graph.hashCode());
//    }
//    @Test
//    public void testUnlinkValeurAnonExistante(){
//        graph.unlink("douze", 3);
//        assertEquals(1037272538,graph.hashCode());
//    }
//    @Test
//    public void testUnlinkValeurBnonExistante(){
//        graph.unlink("trois", 12);
//        assertEquals(1037272538,graph.hashCode());
//    }
    
    @Test(expected = NullPointerException.class) 
    public void testNeighborAValeurNull(){
        graph.build().neighborLeft(null);
    }
    @Test
    public void testNeighborAValeurNonExistante(){
        int sum=0;
        for(Integer i : graph.build().neighborLeft("douze"))
            sum+=i;
        assertEquals(sum,0);
    }
    @Test
    public void testNeighborAValeur(){
        int sum=0;
        for(Integer i : graph.build().neighborLeft("trois"))
            sum+=i;
        assertEquals(sum,45);
    }
    @Test(expected = NullPointerException.class) 
    public void testNeighborBValeurNull(){
        graph.build().neighborRight(null);
    }
    @Test
    public void testNeighborBValeurNonExistante(){
        List<String> obtenu = new ArrayList<>();
        for (String s : graph.build().neighborRight(12))
            obtenu.add(s);
        assertTrue(obtenu.size()==0);
    }
    @Test
    public void testNeighborBValeur(){
        List<String> attendu = Arrays.asList(new String[]{"un","deux","trois","quatre","cinq","six","sept","huit","neuf"});
        List<String> obtenu = new ArrayList<>();
        for (String s : graph.build().neighborRight(3))
            obtenu.add(s);
        assertTrue(obtenu.containsAll(attendu) && attendu.containsAll(obtenu));
    }
    
//    @Test(expected = NullPointerException.class) 
//    public void testRemoveLeftValeurNull(){
//        graph.removeLeft(null);
//    }
//    @Test
//    public void testRemoveLeftValeurInexistante(){
//        graph.removeLeft("douze");
//        assertEquals(1037272538,graph.hashCode());
//    }
//    @Test
//    public void testRemoveLeftValeur(){
//        graph.removeLeft("trois");
//        assertEquals(-310128480, graph.hashCode());
//    }
//    @Test(expected = NullPointerException.class) 
//    public void testRemoveRightValeurNull(){
//        graph.removeRight(null);
//    }
//    @Test
//    public void testRemoveRightValeurInexistante(){
//        graph.removeRight(12);
//        assertEquals(1037272538,graph.hashCode());
//    }
//    @Test
//    public void testRemoveRightValeur(){
//        graph.removeRight(3);
//        assertEquals(1859083709, graph.hashCode());
//    }
    
    @Test
    public void bench(){
        long a=System.currentTimeMillis();
        for (int i=1000;i<2000;i++)
            for (int j=2000;j<3000;j++)
                graph.link(Integer.toString(i), j);
        long b=System.currentTimeMillis();
        for(Integer i : graph.build().neighborLeft("1333"))
            graph.link("douze", i);
        long c=System.currentTimeMillis();
        for(String s : graph.build().neighborRight(2333))
            graph.link(s, 12);
        long d=System.currentTimeMillis();
//        graph.build().removeLeft("douze");
//        long e=System.currentTimeMillis();
//        graph.build().removeRight(12);
//        long f=System.currentTimeMillis();
//        for (int i=1000;i<2000;i++)
//            graph.unlink(Integer.toString(i), 1000+i);
//        long g=System.currentTimeMillis();
        
        System.out.println("insert 1000x1000 "+(b-a)+"ms");
        System.out.println("insert douze to all 1333 "+(c-b)+"ms");
        System.out.println("insert 12 to all 2333 "+(d-c)+"ms");
//        System.out.println("remove all douze (1000) "+(e-d)+"ms");
//        System.out.println("remove all 12 (1000) "+(f-e)+"ms");
//        System.out.println("unlink all numbre where N is linked to N+1000 and N>1000 "+(g-f)+"ms");
        
    }
}
