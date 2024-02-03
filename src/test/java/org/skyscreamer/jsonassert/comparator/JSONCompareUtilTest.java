/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.skyscreamer.jsonassert.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Test JSONCompareUtil
 *
 * @author Carter Page <carter@skyscreamer.org>
 */
class JSONCompareUtilTest {
    @Test
    void testGetCardinalityMap() {
        final int NUM_A = 76;
        final int NUM_B = 3;
        final int NUM_C = 0;
        final int NUM_D = 1;
        final int NUM_E = 2;

        List<String> listToTest = new ArrayList<String>(NUM_A + NUM_B + NUM_C + NUM_D + NUM_E);
        for (int i = 0; i < NUM_A; ++i) listToTest.add("A");
        for (int i = 0; i < NUM_B; ++i) listToTest.add("B");
        for (int i = 0; i < NUM_C; ++i) listToTest.add("C");
        for (int i = 0; i < NUM_D; ++i) listToTest.add("D");
        for (int i = 0; i < NUM_E; ++i) listToTest.add("E");
        Collections.shuffle(listToTest);

        Map<String, Integer> cardinalityMap = JSONCompareUtil.getCardinalityMap(listToTest);
        assertEquals(NUM_A, cardinalityMap.get("A").intValue());
        assertEquals(NUM_B, cardinalityMap.get("B").intValue());
        assertNull(cardinalityMap.get("C"));
        assertEquals(NUM_D, cardinalityMap.get("D").intValue());
        assertEquals(NUM_E, cardinalityMap.get("E").intValue());
    }
}
