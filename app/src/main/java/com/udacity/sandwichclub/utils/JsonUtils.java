package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = null;

        try {
            String mainName;
            List<String> alsoKnownAs = new ArrayList<>();
            String placeOfOrigin;
            String description;
            String image;
            List<String> ingredients = new ArrayList<>();

            JSONObject sandwichJson = new JSONObject(json);

            // Name
            JSONObject sandwichName = sandwichJson.getJSONObject("name");
            mainName = sandwichName.getString("mainName");
            JSONArray alsoKnownAsArray = sandwichName.getJSONArray("alsoKnownAs");
            for (int x = 0; x < alsoKnownAsArray.length(); x++) {
                alsoKnownAs.add(alsoKnownAsArray.getString(x));
            }

            // Place of origin
            placeOfOrigin = sandwichJson.getString("placeOfOrigin");

            // Description
            description = sandwichJson.getString("description");

            // Image
            image = sandwichJson.getString("image");

            // Ingredients
            JSONArray ingredientsArray = sandwichJson.getJSONArray("ingredients");
            for (int x = 0; x < ingredientsArray.length(); x++) {
                ingredients.add(ingredientsArray.getString(x));
            }

            sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image,
                    ingredients);


        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return sandwich;
    }
}
