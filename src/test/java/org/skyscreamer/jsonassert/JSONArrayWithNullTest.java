package org.skyscreamer.jsonassert;

import org.junit.jupiter.api.Test;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;

class JSONArrayWithNullTest {
    @Test
    void testJSONArrayWithNullValue() throws JSONException {
        JSONArray jsonArray1 = getJSONArray1();
        JSONArray jsonArray2 = getJSONArray2();

        JSONAssert.assertEquals(jsonArray1, jsonArray2, true);
        JSONAssert.assertEquals(jsonArray1, jsonArray2, false);
    }

    @Test
    void testJSONArrayWithNullValueAndJsonObject() throws JSONException {
        JSONArray jsonArray1 = getJSONArray1();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("hey", "value");

        JSONArray jsonArray2 = getJSONArray2();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("hey", "value");

        JSONAssert.assertEquals(jsonArray1, jsonArray2, true);
        JSONAssert.assertEquals(jsonArray1, jsonArray2, false);
    }

    private JSONArray getJSONArray1() {
        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.put(1);
        jsonArray1.put(null);
        jsonArray1.put(3);
        jsonArray1.put(2);
        return jsonArray1;
    }

    private JSONArray getJSONArray2() {
        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.put(1);
        jsonArray1.put(null);
        jsonArray1.put(3);
        jsonArray1.put(2);
        return jsonArray1;
    }
}
