package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int DEFAULT_POSITION = -1;

    private TextView alsoKnownAsTv, placeOfOriginTv, descriptionTv, ingredientsTv,
            alsoKnownAsLabelTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Bind UI
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        alsoKnownAsLabelTv = findViewById(R.id.also_known_as_label_tv);
        alsoKnownAsTv = findViewById(R.id.also_known_tv);
        placeOfOriginTv = findViewById(R.id.origin_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_POSITION)) {
            closeOnError();
        } else {
            int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
            if (position == DEFAULT_POSITION) {
                // EXTRA_POSITION not found in intent
                closeOnError();
                return;
            }

            String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
            String json = sandwiches[position];
            Sandwich sandwich = JsonUtils.parseSandwichJson(json);

            Log.d(TAG, String.format("Sandwich chosen: %s", sandwich));

            if (sandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }

            populateUI(sandwich);
            Picasso.with(this)
                    .load(sandwich.getImage())
                    .error(R.drawable.ic_no_image)
                    .into(ingredientsIv);

            setTitle(sandwich.getMainName());
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        // If not AKA for sandwich, make the corresponding views gone
        if (sandwich.getAlsoKnownAs().size() <= 0) {
            alsoKnownAsLabelTv.setVisibility(View.GONE);
            alsoKnownAsTv.setVisibility(View.GONE);
        } else {
            for (String aka : sandwich.getAlsoKnownAs()) {
                alsoKnownAsTv.append("- " + aka + "\n");
            }
        }

        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        if(!TextUtils.isEmpty(placeOfOrigin)) {
            placeOfOriginTv.setText(placeOfOrigin);
        }

        String description = sandwich.getDescription();
        if(!TextUtils.isEmpty(description)) {
            descriptionTv.setText(description);
        }

        // Populate ingredients
        if(sandwich.getIngredients().size() > 0) {
            ingredientsTv.setText("");
            for (String ingredient : sandwich.getIngredients()) {
                ingredientsTv.append("- " + ingredient + "\n");
            }
        }
    }
}
