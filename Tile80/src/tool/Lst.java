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

package tool;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 *
 * @author martin
 */
public class Lst {

    public static<T> Iterable<T> cons(T first, Iterable<T> rest){
        return Iterables.concat(ImmutableList.of(first),rest);
    }
    
    public static<T> T car(Iterable<T> lst, T nil){
        return Iterables.getFirst(lst, nil);
    }
    
    public static<T> Iterable<T> cdr(Iterable<T> lst){
        return Iterables.skip(lst, 1);
    }
}
