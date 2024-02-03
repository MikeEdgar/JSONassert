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

package org.skyscreamer.jsonassert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONCompare.compareJSON;
import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT;
import static org.skyscreamer.jsonassert.JSONCompareMode.NON_EXTENSIBLE;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import com.github.openjson.JSONException;

/**
 * Unit tests for {@code JSONCompare}.
 */
class JSONCompareTest {
    @Test
    void succeedsWithEmptyArrays() throws JSONException {
        assertTrue(compareJSON("[]", "[]", LENIENT).passed());
    }

    @Test
    void reportsArraysOfUnequalLength() throws JSONException {
        JSONCompareResult result = compareJSON("[4]", "[]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[]: Expected 1 values but got 0")));
    }

    @Test
    void reportsArrayMissingExpectedElement() throws JSONException {
        JSONCompareResult result = compareJSON("[4]", "[7]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[]\nExpected: 4\n     but none found\n ; []\nUnexpected: 7\n")));
        assertEquals(1, result.getFieldMissing().size());
        assertEquals(1, result.getFieldUnexpected().size());
    }

    @Test
    void reportsMismatchedFieldValues() throws JSONException {
        JSONCompareResult result = compareJSON("{\"id\": 3}", "{\"id\": 5}", LENIENT);
        assertThat(result, failsWithMessage(equalTo("id\nExpected: 3\n     got: 5\n")));
        assertThat(result, failsWithMessage(equalTo("id\nExpected: 3\n     got: 5\n")));
    }

    @Test
    void reportsMissingField() throws JSONException {
        JSONCompareResult result = compareJSON("{\"obj\": {\"id\": 3}}", "{\"obj\": {}}", LENIENT);
        assertThat(result, failsWithMessage(equalTo("obj\nExpected: id\n     but none found\n")));
        assertEquals(1, result.getFieldMissing().size());
    }

    @Test
    void reportsUnexpectedArrayWhenExpectingObject() throws JSONException {
        JSONCompareResult result = compareJSON("{}", "[]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("\nExpected: a JSON object\n     got: a JSON array\n")));
    }

    @Test
    void reportsUnexpectedObjectWhenExpectingArray() throws JSONException {
        JSONCompareResult result = compareJSON("[]", "{}", LENIENT);
        assertThat(result, failsWithMessage(equalTo("\nExpected: a JSON array\n     got: a JSON object\n")));
    }

    @Test
    void reportsUnexpectedNull() throws JSONException {
        JSONCompareResult result = compareJSON("{\"id\": 3}", "{\"id\": null}", LENIENT);
        assertThat(result, failsWithMessage(equalTo("id\nExpected: 3\n     got: null\n")));
    }

    @Test
    void reportsUnexpectedNonNull() throws JSONException {
        JSONCompareResult result = compareJSON("{\"id\": null}", "{\"id\": \"abc\"}", LENIENT);
        assertThat(result, failsWithMessage(equalTo("id\nExpected: null\n     got: abc\n")));
    }

    @Test
    void reportsUnexpectedFieldInNonExtensibleMode() throws JSONException {
        JSONCompareResult result = compareJSON("{\"obj\": {}}", "{\"obj\": {\"id\": 3}}", NON_EXTENSIBLE);
        assertThat(result, failsWithMessage(equalTo("obj\nUnexpected: id\n")));
        assertEquals(1, result.getFieldUnexpected().size());
    }

    @Test
    void reportsMismatchedTypes() throws JSONException {
        JSONCompareResult result = compareJSON("{\"arr\":[]}", "{\"arr\":{}}", LENIENT);
        assertThat(result, failsWithMessage(equalTo("arr\nExpected: a JSON array\n     got: a JSON object\n")));
    }

    @Test
    void reportsWrongSimpleValueCountInUnorderedArray() throws JSONException {
        JSONCompareResult result = compareJSON("[5, 5]", "[5, 7]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[]: Expected 2 occurrence(s) of 5 but got 1 occurrence(s) ; []\nUnexpected: 7\n")));
        assertEquals(1, result.getFieldUnexpected().size());
    }

    @Test
    void reportsMissingJSONObjectWithUniqueKeyInUnorderedArray() throws JSONException {
        JSONCompareResult result = compareJSON("[{\"id\" : 3}]", "[{\"id\" : 5}]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[id=3]\nExpected: a JSON object\n     but none found\n ; " +
                "[id=5]\nUnexpected: a JSON object\n")));
        assertEquals(1, result.getFieldMissing().size());
        assertEquals(1, result.getFieldUnexpected().size());
    }

    @Test
    void reportsUnmatchedJSONObjectInUnorderedArray() throws JSONException {
        JSONCompareResult result = compareJSON("[{\"address\" : {\"street\" : \"Acacia Avenue\"}}]", "[{\"age\" : 23}]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[0] Could not find match for element {\"address\":{\"street\":\"Acacia Avenue\"}}")));
    }

    @Test
    void succeedsWithNestedJSONObjectsInUnorderedArray() throws JSONException {
        assertTrue(compareJSON("[{\"address\" : {\"street\" : \"Acacia Avenue\"}}, 5]", "[5, {\"address\" : {\"street\" : \"Acacia Avenue\"}}]", LENIENT).passed());
    }

    @Test
    void succeedsWithJSONObjectsWithNonUniqueKeyInUnorderedArray() throws JSONException {
        String jsonDocument = "[{\"age\" : 43}, {\"age\" : 43}]";
        assertTrue(compareJSON(jsonDocument, jsonDocument, LENIENT).passed());
    }

    @Test
    void succeedsWithSomeNestedJSONObjectsInUnorderedArray() throws JSONException {
        String jsonDocument = "[{\"age\" : 43}, {\"age\" : {\"years\" : 43}}]";
        assertTrue(compareJSON(jsonDocument, jsonDocument, LENIENT).passed());
    }

    @Test
    void reportsUnmatchesIntegerValueInUnorderedArrayContainingJSONObject() throws JSONException {
        JSONCompareResult result = compareJSON("[{\"address\" : {\"street\" : \"Acacia Avenue\"}}, 5]", "[{\"address\" : {\"street\" : \"Acacia Avenue\"}}, 2]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[1] Could not find match for element 5")));
    }

    @Test
    void reportsUnmatchedJSONArrayWhereOnlyExpectedContainsJSONObjectWithUniqueKey() throws JSONException {
        JSONCompareResult result = compareJSON("[{\"id\": 3}]", "[{}]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[0] Could not find match for element {\"id\":3}")));
    }

    @Test
    void reportsUnmatchedJSONArrayWhereExpectedContainsJSONObjectWithUniqueKeyButActualContainsElementOfOtherType() throws JSONException {
        JSONCompareResult result = compareJSON("[{\"id\": 3}]", "[5]", LENIENT);
        assertThat(result, failsWithMessage(equalTo("[0] Could not find match for element {\"id\":3}")));
    }

    private Matcher<JSONCompareResult> failsWithMessage(final Matcher<String> expectedMessage) {
        return new TypeSafeMatcher<JSONCompareResult>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("a failed comparison with message ").appendDescriptionOf(expectedMessage);
            }

            @Override
            public boolean matchesSafely(JSONCompareResult item) {
                return item.failed() && expectedMessage.matches(item.getMessage());
            }
        };
    }
}
