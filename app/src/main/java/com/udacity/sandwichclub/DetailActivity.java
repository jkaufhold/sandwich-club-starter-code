package com.udacity.sandwichclub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private Sandwich sandwich;

    private TextView alsoKnownView;
    private TextView alsoKnownLabel;
    private TextView placeOfOriginView;
    private TextView placeOfOriginLabel;
    private TextView descriptionView;
    private TextView descriptionLabel;
    private TextView ingredientsView;
    private TextView ingredientsLabel;
    private ImageView ingredientsIv;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.alsoKnownView = findViewById(R.id.also_known_tv);
        this.alsoKnownLabel = findViewById(R.id.also_known_label);
        this.placeOfOriginView = findViewById(R.id.origin_tv);
        this.placeOfOriginLabel = findViewById(R.id.origin_label);
        this.descriptionView = findViewById(R.id.description_tv);
        this.descriptionLabel = findViewById(R.id.description_label);
        this.ingredientsView = findViewById(R.id.ingredients_tv);
        this.ingredientsLabel = findViewById(R.id.ingredients_label);
        this.ingredientsIv = findViewById(R.id.image_iv);
        this.progressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        new LoadJSONDataTask().execute(json);
    }

    private void updateUI(Sandwich sandwich) {
        this.sandwich = sandwich;
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        populateText(alsoKnownLabel, alsoKnownView, sandwich.getAlsoKnownAs());
        populateText(placeOfOriginLabel, placeOfOriginView, sandwich.getPlaceOfOrigin());
        populateText(descriptionLabel, descriptionView, sandwich.getDescription());
        populateText(ingredientsLabel, ingredientsView, sandwich.getIngredients());
    }

    private void populateText(TextView label, TextView tv, List<String> texts) {
        String text = null;
        if(texts != null && !texts.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for(String t : texts) {
                if(builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(t);
            }
            text = builder.toString();
        }
        populateText(label, tv, text);
    }

    private void populateText(TextView label, TextView tv, String text) {
        int visibility = (text == null || text.trim().isEmpty())
            ? View.GONE
            : View.VISIBLE;
        tv.setText(text);
        tv.setVisibility(visibility);
        label.setVisibility(visibility);
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadJSONDataTask extends AsyncTask<String, Void, Sandwich> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Sandwich doInBackground(String... strings) {
            String json = strings[0];
            return JsonUtils.parseSandwichJson(json);
        }

        @Override
        protected void onPostExecute(Sandwich sandwich) {
            progressBar.setVisibility(View.GONE);
            updateUI(sandwich);
        }
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
