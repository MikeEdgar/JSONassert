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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;

/**
 * @author <a href="mailto:aiveeen@gmail.com">Ivan Zaytsev</a>
 *         2013-01-04
 */
class CustomComparatorTest {

    private static class ArrayOfJsonObjectsComparator extends DefaultComparator {
        ArrayOfJsonObjectsComparator(JSONCompareMode mode) {
            super(mode);
        }

        @Override
        public void compareJSONArray(String prefix, JSONArray expected, JSONArray actual, JSONCompareResult result) throws JSONException {
            compareJSONArrayOfJsonObjects(prefix, expected, actual, result);
        }
    }

    @Test
    void testFullArrayComparison() throws Exception {
        JSONCompareResult compareResult = JSONCompare.compareJSON(
                "[{id:1}, {id:3}, {id:5}]",
                "[{id:1}, {id:3}, {id:6}, {id:7}]", new ArrayOfJsonObjectsComparator(JSONCompareMode.LENIENT)
        );

        assertTrue(compareResult.failed());
        String message = compareResult.getMessage().replaceAll("\n", "");
        assertTrue(message.matches(".*id=5.*Expected.*id=6.*Unexpected.*id=7.*Unexpected.*"), message);
    }
}
