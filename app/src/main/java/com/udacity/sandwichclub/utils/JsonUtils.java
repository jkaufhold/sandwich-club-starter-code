package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = null;
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject name = jsonObject.optJSONObject("name");
            String mainName = null;
            List<String> alsoKnownAs = null;
            if (name != null) {
                mainName = name.optString("mainName");
                alsoKnownAs = getListFromJSONArray(name.optJSONArray("alsoKnownAs"));
            }

            String placeOfOrigin = jsonObject.optString("placeOfOrigin");
            String description = jsonObject.optString("description");
            String image = jsonObject.optString("image");

            List<String> ingredients = getListFromJSONArray(jsonObject.optJSONArray("ingredients"));

            sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sandwich;
    }

    private static List<String> getListFromJSONArray(JSONArray jsonArray) {
        if(jsonArray == null) {
            return null;
        }

        List<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.optString(i);
            if(value != null) {
                list.add(value);
            }
        }

        return list;
    }
}
